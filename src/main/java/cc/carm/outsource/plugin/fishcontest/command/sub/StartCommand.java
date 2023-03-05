package cc.carm.outsource.plugin.fishcontest.command.sub;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.command.MainCommand;
import cc.carm.outsource.plugin.fishcontest.conf.PluginConfig;
import cc.carm.outsource.plugin.fishcontest.data.RankingRule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class StartCommand extends SubCommand<MainCommand> {

    public StartCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {

        RankingRule rule = PluginConfig.CONTEST.RULE.getNotNull();
        Duration duration = PluginConfig.CONTEST.DURATION.getNotNull();
        if (args.length >= 1) {
            rule = RankingRule.parse(args[0], rule);
        }
        if (args.length >= 2) {
            try {
                duration = Duration.of(Long.parseLong(args[1]), ChronoUnit.SECONDS);
            } catch (NumberFormatException e) {
                throw new Exception("错误的时间配置，输入一个整秒数！");
            }
        }

        Main.getContestManager().startContest(rule, sender instanceof Player player ? player : null, duration);
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return getParent().hasAdminPermission(sender);
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.objects(args[args.length - 1], Arrays.stream(RankingRule.values()).map(Enum::name));
        } else if (args.length == 2) {
            return SimpleCompleter.text(args[args.length - 1], "60", "300");
        } else return null;
    }

}
