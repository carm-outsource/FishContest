package cc.carm.outsource.plugin.fishcontest.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.outsource.plugin.fishcontest.command.sub.StartCommand;
import cc.carm.outsource.plugin.fishcontest.command.sub.StatsCommand;
import cc.carm.outsource.plugin.fishcontest.command.sub.StopCommand;
import cc.carm.outsource.plugin.fishcontest.command.sub.SuspendCommand;
import cc.carm.outsource.plugin.fishcontest.command.sub.ReloadCommand;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends CommandHandler {

    public MainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);

        registerSubCommand(new StatsCommand(this, "stats", "rank", "top"));
        registerSubCommand(new StartCommand(this, "start", "begin"));
        registerSubCommand(new StopCommand(this, "stop", "end"));
        registerSubCommand(new SuspendCommand(this, "suspend"));
        registerSubCommand(new ReloadCommand(this, "reload"));
    }

    public boolean hasAdminPermission(CommandSender sender) {
        return sender.hasPermission("fishcontest.admin");
    }

    @Override
    public Void noArgs(CommandSender sender) {
        if (hasAdminPermission(sender)) {
            PluginMessages.COMMAND.USAGE.ADMIN.send(sender);
        } else {
            PluginMessages.COMMAND.USAGE.USER.send(sender);
        }
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        PluginMessages.COMMAND.NO_PERMISSION.send(sender);
        return null;
    }

}
