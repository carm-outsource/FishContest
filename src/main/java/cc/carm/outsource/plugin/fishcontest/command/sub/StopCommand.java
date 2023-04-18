package cc.carm.outsource.plugin.fishcontest.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.command.MainCommand;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class StopCommand extends SubCommand<MainCommand> {

    public StopCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {

        if (Main.getContestManager().getRunningContest() == null) {
            PluginMessages.COMMAND.CONTEST.NO_RUNNING_CONTEST.send(sender);
            return null;
        }

        Main.getContestManager().getRunningContest().setEndMillis(System.currentTimeMillis());
        Main.getContestManager().stopContest();
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return getParent().hasAdminPermission(sender);
    }

}
