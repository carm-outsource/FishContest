package cc.carm.outsource.plugin.fishcontest.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.command.MainCommand;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<MainCommand> {

    public ReloadCommand(@NotNull MainCommand parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {

        if (Main.getContestManager().getRunningContest() == null) {
            PluginMessages.COMMAND.CONTEST.HAS_RUNNING_CONTEST.send(sender);
            return null;
        }

        PluginMessages.COMMAND.RELOAD.START.send(sender);
        long s1 = System.currentTimeMillis();

        try {
            Main.getInstance().getConfigProvider().reload();
            Main.getInstance().getMessageProvider().reload();

            PluginMessages.COMMAND.RELOAD.COMPLETE.send(sender, System.currentTimeMillis() - s1);
        } catch (Exception ex) {
            PluginMessages.COMMAND.RELOAD.ERROR.send(sender);
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return getParent().hasAdminPermission(sender);
    }

}
