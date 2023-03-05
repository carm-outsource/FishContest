package cc.carm.outsource.plugin.fishcontest.conf;


import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.annotation.InlineComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import cc.carm.outsource.plugin.fishcontest.conf.fish.ConfiguredFishes;
import cc.carm.outsource.plugin.fishcontest.conf.prize.ConfiguredPrize;
import cc.carm.outsource.plugin.fishcontest.data.RankingRule;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.TreeMap;

public class PluginConfig extends ConfigurationRoot {

    public static final ConfigValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({
            "统计数据设定",
            "该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            "当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    public static final ConfigValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    public static final ConfigValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({"", "可捕获的物品配置"})
    public static final ConfigValue<ConfiguredFishes> ITEMS = ConfiguredValue.builder(ConfiguredFishes.class)
            .fromSection().defaults(ConfiguredFishes::defaults)
            .parseValue((data, defaultValue) -> ConfiguredFishes.parse(data))
            .serializeValue(ConfiguredFishes::serialize)
            .build();

    @HeaderComment("插件音效配置")
    public static final class SOUND extends ConfigurationRoot {

        @HeaderComment("打开GUI时播放的声音")
        public static final ConfiguredSound GUI_OPEN = ConfiguredSound.of(Sound.BLOCK_NOTE_BLOCK_PLING);

    }

    @HeaderComment({"", "计量单位配置。"})
    public static final class MEASUREMENT extends ConfigurationRoot {

        @HeaderComment("当不满足最低单位或未配置份额时显示的名称")
        public static final ConfigValue<String> EMPTY = ConfiguredValue.of(String.class, "难以计量");

        @HeaderComment({"单位等级配置，满则进位，不满最低单位则不记。", "如 206 记为 4两1钱"})
        public static final ConfiguredMap<Integer, String> LEVELS = ConfiguredMap
                .builder(Integer.class, String.class).fromString()
                .supplier(() -> new TreeMap<>(Comparator.reverseOrder()))
                .parseKey(Integer::parseInt)
                .defaults(v -> {
                    v.put(5, "钱");
                    v.put(50, "两");
                    v.put(500, "斤");
                }).build();

    }

    @HeaderComment({"", "记录存储相关配置", "注意：存储配置不会通过重载指令生效，如有修改请重新启动服务器。"})
    public static final class LOGGER extends ConfigurationRoot {

        @InlineComment("是否启用")
        public static final ConfigValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, true);

        @HeaderComment("存储方式，可选 [ yaml | mysql ]")
        public static final ConfigValue<String> METHOD = ConfiguredValue.of(String.class, "YAML");

    }

    @HeaderComment("自动开启比赛功能")
    public static final class SCHEDULE extends ConfigurationRoot {

        @InlineComment("是否启用")
        public static final ConfigValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

        @HeaderComment({
                "每天尝试开启比赛的时间，格式为 HH:mm:ss (不足两位请补0)",
                "如果到达时间但此时已有比赛在进行，则会在下一个时间点尝试开启。"
        })
        public static final ConfiguredList<LocalTime> TIME = ConfiguredList.builder(LocalTime.class).fromString()
                .parseValue(LocalTime::parse).serializeValue(LocalTime::toString)
                .defaults(LocalTime.of(20, 0, 0))
                .build();

        @HeaderComment("自动开启比赛需要满足的条件")
        public static final class REQUIREMENTS extends ConfigurationRoot {

            @InlineComment("最少需要多少名玩家在线")
            public static final ConfigValue<Integer> MINIMUM_PLAYERS = ConfiguredValue.of(Integer.class, 5);

        }

    }

    @HeaderComment({"", "比赛相关配置"})
    public static final class CONTEST extends ConfigurationRoot {

        @HeaderComment("默认的比赛规则，可选 [ SUM(总量排行) | MAX(最值排行) ]")
        public static final ConfigValue<RankingRule> RULE = ConfiguredValue
                .builder(RankingRule.class).fromString()
                .parseValue(RankingRule::parse).serializeValue(RankingRule::name)
                .defaults(RankingRule.SUM)
                .build();

        @HeaderComment("默认的比赛持续时间(单位：秒)")
        public static final ConfigValue<Duration> DURATION = ConfiguredValue.builder(Duration.class).fromString()
                .parseValue((v, d) -> Duration.ofSeconds(Long.parseLong(v)))
                .serializeValue(v -> Long.toString(v.getSeconds()))
                .defaults(Duration.ofSeconds(300))
                .build();

        @HeaderComment("比赛奖励结算内容")
        public static final ConfigValue<ConfiguredPrize> PRIZES = ConfiguredValue.builder(ConfiguredPrize.class)
                .fromSection().defaults(ConfiguredPrize::defaults)
                .parseValue((data, defaultValue) -> ConfiguredPrize.parse(data))
                .serializeValue(ConfiguredPrize::serialize)
                .build();
    }

    @HeaderComment({"", "GUI相关配置"})
    public static final class GUI extends ConfigurationRoot {

        @HeaderComment("GUI标题")
        public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&6&l钓鱼大赛 &8| %(id)")
                .params("id", "rule")
                .build();

        public static final class ICONS extends ConfigurationRoot {
            @HeaderComment("占位符物品")
            public static final ConfiguredItem EMPTY = ConfiguredItem.create()
                    .defaultType(Material.PLAYER_HEAD)
                    .defaultName("&8#%(index) &f&o虚位以待~")
                    .params("index").build();

            @HeaderComment("比赛信息")
            public static final ConfiguredItem CONTEST = ConfiguredItem.create()
                    .defaultType(Material.CLOCK)
                    .defaultName("&a&l比赛信息")
                    .defaultLore(
                            " ",
                            " &e&l比赛序号: &f%(id)",
                            " &e&l比赛规则: &f%(rule)",
                            " &e&l比赛时长: &f%(duration)",
                            " &e&l开始时间: &f%(start_time)",
                            " &e&l结束时间: &f%(end_time)",
                            " &e&l参与人数: &f%(participants)",
                            " ",
                            " &6我的排名: &f%(rank)",
                            " &6我的得分: &f%(score)"
                    ).params(
                            "id", "rule", "duration",
                            "start_time", "end_time", "participants",
                            "rank", "score"
                    ).build();

            @HeaderComment("用于显示玩家的信息")
            public static final class USER extends ConfigurationRoot {

                public static final ConfiguredMessage<String> NAME = ConfiguredMessage.asString()
                        .defaults("&8#%(index) &6&l%(name) &8- &f%(score)")
                        .params("name", "index", "score", "amount", "total_quantity", "max_quantity")
                        .build();

                public static final ConfiguredMessageList<String> LORE = ConfiguredMessageList.asStrings()
                        .defaults(
                                " ",
                                " &e排名 &f&l%(index)",
                                " ",
                                " &e总钓鱼数 &f%(amount)",
                                " &e全部重量 &f%(total_quantity)",
                                " &e最大重量 &f%(max_quantity)"
                        ).params("name", "index", "score", "amount", "total_quantity", "max_quantity")
                        .build();

            }
        }

        @HeaderComment("GUI中的基本按钮物品")
        public static final class BOTTOMS extends ConfigurationRoot {

            @HeaderComment("前往下一页的物品 (只有存在下一页时才会显示)")
            public static final ConfiguredItem NEXT_PAGE = ConfiguredItem.create()
                    .defaultType(Material.OAK_BUTTON)
                    .defaultName("下一页")
                    .defaultLore("&7&o右键可前往最后一页哦")
                    .build();

            @ConfigPath("previous-page")
            @HeaderComment({"前往上一页时的物品 (只有当前页不是第一页时才会显示)"})
            public static final ConfiguredItem PREV_PAGE = ConfiguredItem.create()
                    .defaultType(Material.OAK_BUTTON)
                    .defaultName("上一页")
                    .defaultLore("&7&o右键可前往第一页哦")
                    .build();

        }

    }

}
