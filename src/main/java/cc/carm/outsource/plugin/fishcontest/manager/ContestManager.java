package cc.carm.outsource.plugin.fishcontest.manager;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.conf.PluginConfig;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import cc.carm.outsource.plugin.fishcontest.conf.prize.ConfiguredPrize;
import cc.carm.outsource.plugin.fishcontest.data.FishContestData;
import cc.carm.outsource.plugin.fishcontest.data.RankingRule;
import cc.carm.outsource.plugin.fishcontest.data.UserContestData;
import cc.carm.outsource.plugin.fishcontest.utils.TimeDateUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class ContestManager {

    protected BukkitRunnable timer;

    protected @Nullable FishContestData runningContest;
    protected final @NotNull Map<String, FishContestData> historyContests = new HashMap<>();

    public void startTimer(EasyPlugin plugin) {
        this.timer = new BukkitRunnable() {
            @Override
            public void run() {
                checkSchedule();
                checkContest();
            }
        };
        this.timer.runTaskTimer(plugin, 20L, 20L);
    }

    public void shutdown() {
        this.timer.cancel();
        this.timer = null;
    }

    protected void checkSchedule() {
        if (!PluginConfig.SCHEDULE.ENABLE.getNotNull()) return; // 未开启自动开启比赛功能
        if (runningContest != null) return; //  当前已有比赛

        LocalTime now = LocalTime.now().withNano(0);
        if (PluginConfig.SCHEDULE.TIME.stream().noneMatch(s -> s.equals(now))) return; // 当前时间并非比赛开始的时间

        if (Bukkit.getOnlinePlayers().size() < PluginConfig.SCHEDULE.REQUIREMENTS.MINIMUM_PLAYERS.getNotNull()) {
            Main.info("[钓鱼大赛日程] 当前在线玩家(" + Bukkit.getOnlinePlayers().size() + ")不足以开启一场比赛，跳过此时间段。");
            return;
        }

        startContest(null); // 尝试开启比赛
    }

    protected void checkContest() {
        if (runningContest == null) return; // 没有任何需要处理的内容
        if (!runningContest.isEnded()) return; // 检测比赛是否结束

        stopContest();
    }

    public @Nullable FishContestData getRunningContest() {
        return runningContest;
    }

    public @Nullable FishContestData getHistoryContest(String id) {
        return historyContests.get(id);
    }

    public @NotNull Map<String, FishContestData> getHistoryContests() {
        return historyContests;
    }

    public void startContest(@Nullable Player operator) {
        startContest(PluginConfig.CONTEST.RULE.getNotNull(), operator, PluginConfig.CONTEST.DURATION.getNotNull());
    }

    public void startContest(@NotNull RankingRule type, @Nullable Player operator, Duration duration) {
        if (this.runningContest != null) throw new RuntimeException("已经有一场正在执行的比赛了。");

        UUID operatorUUID = Optional.ofNullable(operator).map(Entity::getUniqueId).orElse(null);
        String operatorName = Optional.ofNullable(operator).map(HumanEntity::getName).orElse("CONSOLE");

        this.runningContest = new FishContestData(type, operatorUUID, duration); // 创建一个新的比赛

        Bukkit.getOnlinePlayers().forEach(p -> {
            Object[] paramValues = new Object[]{
                    runningContest.getID(), operatorName, TimeDateUtils.toDHMSStyle(duration.toSeconds()),
                    type.getName(p), String.join("\n", type.getDescription(p))
            };
            PluginMessages.CONTEST.START.SOUND.playTo(p);
            PluginMessages.CONTEST.START.TITLE.send(p, paramValues);
            PluginMessages.CONTEST.START.MESSAGE.send(p, paramValues);
        });

    }

    public void suspendContest() {
        if (this.runningContest == null) return; // 没有任何需要处理的内容

        Object[] paramValues = new Object[]{runningContest.getID()};
        Bukkit.getOnlinePlayers().forEach(p -> {
            PluginMessages.CONTEST.SUSPEND.SOUND.playTo(p);
            PluginMessages.CONTEST.SUSPEND.TITLE.send(p, paramValues);
            PluginMessages.CONTEST.SUSPEND.MESSAGE.send(p, paramValues);
        });

        // 缓存旧数据，便于玩家查询本次开服依赖的比赛情况
        this.historyContests.put(this.runningContest.getID(), this.runningContest);
        this.runningContest = null;
    }

    public void stopContest() {
        if (this.runningContest == null) return; // 没有任何需要处理的内容

        FishContestData contest = this.runningContest;
        // 发放奖励
        ConfiguredPrize prize = PluginConfig.CONTEST.PRIZES.getNotNull();
        List<UserContestData> ranked = contest.getRankedData();
        List<Object[]> rankedValues = new ArrayList<>();

        int index = 1;
        for (UserContestData user : ranked) {
            Player player = user.getPlayer();
            if (player != null) prize.execute(index, player); // 离线玩家视为放弃领奖
            rankedValues.add(new Object[]{
                    index, user.getName(),
                    user.getScoreString(contest.getRankingRule()),
                    user.getItems().size()
            });
            index++;
        }

        Object[] contestValues = new Object[]{contest.getID(), ranked.size()};
        Bukkit.getOnlinePlayers().forEach(p -> {

            int playerRank = 1;

            @Nullable UserContestData playerData = null;

            for (UserContestData userContestData : ranked) {
                if (userContestData.getUUID().equals(p.getUniqueId())) {
                    playerData = userContestData;
                } else {
                    playerRank++;
                }
            }

            PluginMessages.CONTEST.STOP.SOUND.playTo(p);
            PluginMessages.CONTEST.STOP.TITLE.send(p, contestValues);

            PluginMessages.CONTEST.STOP.HEADER.send(p, contestValues);
            rankedValues.stream().limit(5).forEach(values -> PluginMessages.CONTEST.STOP.RANK.send(p, values));
            PluginMessages.CONTEST.STOP.FOOTER.send(p, contestValues);

            if (playerData != null) { // 只有玩家参赛了才发个人相关信息
                Object[] playerValues = new Object[]{
                        playerRank, playerData.getScoreString(contest.getRankingRule()), playerData.getItems().size()
                };
                if (prize.commands().containsKey(playerRank)) {
                    PluginMessages.CONTEST.STOP.MINE_REWARDED.send(p, playerValues);
                } else {
                    PluginMessages.CONTEST.STOP.MINE_NOT_REWARD.send(p, playerValues);
                }
            }

        });


        // 缓存旧数据，便于玩家查询本次开服依赖的比赛情况
        this.historyContests.put(this.runningContest.getID(), this.runningContest);

        // 转化为LOG形式并记录LOG存档

        this.runningContest = null; // 清空比赛数据
    }


}
