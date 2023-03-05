package cc.carm.outsource.plugin.fishcontest.command.sub;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.command.MainCommand;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import cc.carm.outsource.plugin.fishcontest.data.FishContestData;
import cc.carm.outsource.plugin.fishcontest.data.RankingRule;
import cc.carm.outsource.plugin.fishcontest.ui.GameLeaderboardGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StatsCommand extends SubCommand<MainCommand> {

    public StatsCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        FishContestData contestData = null;

        if (args.length == 0) {
            contestData = Main.getContestManager().getRunningContest();
        } else if (args.length == 1) {
            contestData = Main.getContestManager().getHistoryContest(args[0]);
        }

        if (contestData == null) {
            PluginMessages.COMMAND.CONTEST.NOT_FOUND.send(sender);
            return null;
        }

        if (!(sender instanceof Player player)) {
            RankingRule rule = contestData.getRankingRule();
            contestData.getRankedData().forEach(data -> sender.sendMessage(data.getName() + " -> " + data.getScoreString(rule)));
            return null;
        }

        GameLeaderboardGUI.open(player, contestData);
        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.text(args[args.length - 1], Main.getContestManager().getHistoryContests().keySet());
        } else return null;
    }
}
