package cc.carm.outsource.plugin.fishcontest;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.easyplugin.updatechecker.GHUpdateChecker;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.outsource.plugin.fishcontest.command.MainCommand;
import cc.carm.outsource.plugin.fishcontest.conf.PluginConfig;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import cc.carm.outsource.plugin.fishcontest.listener.FishListener;
import cc.carm.outsource.plugin.fishcontest.manager.ContestManager;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.event.Listener;

public class Main extends EasyPlugin implements Listener {
    private static Main instance;

    public Main() {
        instance = this;
    }

    protected ConfigurationProvider<?> configProvider;
    protected ConfigurationProvider<?> messageProvider;

    protected ContestManager contestManager;

    @Override
    public boolean initialize() {

        log("加载插件配置文件...");
        this.configProvider = MineConfiguration.from(this, "config.yml");
        this.configProvider.initialize(PluginConfig.class);

        this.messageProvider = MineConfiguration.from(this, "messages.yml");
        this.messageProvider.initialize(PluginMessages.class);

        log("加载比赛管理器...");
        this.contestManager = new ContestManager();
        this.contestManager.startTimer(this);

        log("初始化GUI管理器...");
        GUI.initialize(this);
        AutoPagedGUI.defaultNextPage = (PluginConfig.GUI.BOTTOMS.NEXT_PAGE::getItem);
        AutoPagedGUI.defaultPreviousPage = (PluginConfig.GUI.BOTTOMS.PREV_PAGE::getItem);

        log("注册监听器...");
        registerListener(new FishListener(this));

        log("注册指令...");
        registerCommand(getName(), new MainCommand(this));

        if (PluginConfig.METRICS.getNotNull()) {
            log("启用统计数据...");
            Metrics metrics = new Metrics(this, 17847);
            metrics.addCustomChart(new SimplePie("log_enabled", () -> PluginConfig.LOGGER.ENABLE.getNotNull().toString()));
            metrics.addCustomChart(new SimplePie("log_method", PluginConfig.LOGGER.METHOD::getNotNull));
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull()) {
            log("开始检查更新...");
            GHUpdateChecker.run(this);
        } else {
            log("已禁用检查更新，跳过。");
        }

        return true;
    }

    @Override
    protected void shutdown() {

        log("结束比赛管理器...");
        this.contestManager.stopContest();
        this.contestManager.shutdown();

    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public static void info(String... message) {
        getInstance().log(message);
    }

    public static void debugging(String... message) {
        getInstance().debug(message);
    }

    public static void severe(String... message) {
        getInstance().error(message);
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigurationProvider<?> getMessageProvider() {
        return messageProvider;
    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public static ContestManager getContestManager() {
        return getInstance().contestManager;
    }


}
