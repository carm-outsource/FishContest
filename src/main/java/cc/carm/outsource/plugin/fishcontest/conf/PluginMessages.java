package cc.carm.outsource.plugin.fishcontest.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageListBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.title.TitleConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredTitle;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;


@HeaderComment({
        "钓鱼大赛插件的消息配置文件",
        "如特定的消息不需要任何提示，可直接留下单行空内容消息。",
        "支持 &+颜色代码(原版颜色)、§(#XXXXXX)(RGB颜色) 与 &<#XXXXXX>(前后标注RGB颜色渐变)。",
        "支持 MineDown 语法，详见 https://github.com/Phoenix616/MineDown 。",
        " "
})
public class PluginMessages extends ConfigurationRoot {

    public static @NotNull CraftMessageListBuilder<BaseComponent[]> list() {
        return ConfiguredMessageList.create(getParser())
                .whenSend((sender, message) -> message.forEach(m -> sender.spigot().sendMessage(m)));
    }

    public static @NotNull CraftMessageValueBuilder<BaseComponent[]> value() {
        return ConfiguredMessage.create(getParser())
                .whenSend((sender, message) -> sender.spigot().sendMessage(message));
    }

    public static @NotNull TitleConfigBuilder title() {
        return ConfiguredTitle.create().whenSend((player, in, stay, out, line1, line2) -> player.sendTitle(line1, line2, in, stay, out));
    }

    public static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> {
            if (sender instanceof Player) message = PlaceholderAPI.setPlaceholders((Player) sender, message);
            return MineDown.parse(ColorParser.parse(message));
        };
    }

    public static final ConfiguredMessage<String> NONE = ConfiguredMessage.ofString("&8暂无数据");

    public static class COMMAND extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> NO_PERMISSION = list().defaults(
                "&c&l抱歉！&f但您没有足够的权限使用该指令。"
        ).build();

        public static class USAGE extends ConfigurationRoot {

            public static final ConfiguredMessageList<BaseComponent[]> USER = list().defaults(
                    "&<#101f31>&l钓鱼大赛&<#103731> &f您可以输入 &a/FishContest stats &f查看当前进行中的比赛排行数据。"
            ).build();

            public static final ConfiguredMessageList<BaseComponent[]> ADMIN = list().defaults(
                    "&6&l钓鱼大赛 &f指令帮助",
                    "&8#&f start [种类] [持续时间(秒)]",
                    "&8-&7 开始一场钓鱼大赛。",
                    "&8#&f stats",
                    "&8-&7 查看当前的比赛进度。",
                    "&8#&f stop",
                    "&8-&7 结束并结算目前正在进行的钓鱼大赛。",
                    "&8#&f reload",
                    "&8-&7 重载插件的配置文件。",
                    "&8#&f suspend",
                    "&8-&7 终止目前正在进行的钓鱼大赛。",
                    "&8-&7&o 此操作将不会结算比赛奖励！"
            ).build();

        }

        public static class RELOAD extends ConfigurationRoot {

            public static final ConfiguredMessageList<BaseComponent[]> START = list().defaults(
                    "&f正在重载配置文件..."
            ).build();

            public static final ConfiguredMessageList<BaseComponent[]> ERROR = list().defaults(
                    "&f配置文件&c重载失败！&f详细原因详见后台输出。"
            ).build();

            public static final ConfiguredMessageList<BaseComponent[]> COMPLETE = list().defaults(
                    "&f配置文件重载完成，共耗时 &d%(time)&fms 。"
            ).params("time").build();

        }

        public static class CONTEST extends ConfigurationRoot {

            public static final ConfiguredMessageList<BaseComponent[]> HAS_RUNNING_CONTEST = list().defaults(
                    "&f当前有一场正在进行的钓鱼大赛。"
            ).build();

            public static final ConfiguredMessageList<BaseComponent[]> NO_RUNNING_CONTEST = list().defaults(
                    "&f当前没有正在进行的钓鱼大赛。"
            ).build();

            public static final ConfiguredMessageList<BaseComponent[]> NOT_FOUND = list().defaults(
                    "&f未找到指定的大赛数据。"
            ).build();

        }

    }

    public static class CONTEST extends ConfigurationRoot {

        public static class RULE extends ConfigurationRoot {

            public static final class SUM extends ConfigurationRoot {

                public static final ConfiguredMessage<String> NAME = ConfiguredMessage.ofString("总量排行");
                public static final ConfiguredMessageList<String> DESCRIPTION = ConfiguredMessageList.ofStrings(
                        "以钓鱼大赛期间钓到的鱼的重量之和为排名标准，排名越靠前，奖励越多。"
                );

            }

            public static final class MAX extends ConfigurationRoot {

                public static final ConfiguredMessage<String> NAME = ConfiguredMessage.ofString("最重排行");
                public static final ConfiguredMessageList<String> DESCRIPTION = ConfiguredMessageList.ofStrings(
                        "以钓鱼大赛期间所钓出的最重的鱼的重量为排名标准，排名越靠前，奖励越多。"
                );

            }

        }


        public static class START extends ConfigurationRoot {

            public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.ENTITY_PLAYER_LEVELUP);

            public static final ConfiguredTitle TITLE = title().defaults(
                            "&e&l钓鱼大赛",
                            "&f&l比 赛 开 始"
                    ).fadeIn(10).stay(40).fadeOut(10)
                    .params("id", "operator", "rule", "description").build();

            public static final ConfiguredMessageList<BaseComponent[]> MESSAGE = list().defaults(
                    "&e&l钓鱼大赛 &f比赛开始！本次比赛时长为 &e%(duration) &f。",
                    "&f本次比赛的规则为 [&f%(rule)](hover=%(description)) ，赶快开始钓鱼叭~",
                    "&7&o请注意： 奖励将在比赛结束后发放，发放时请保持在线状态，否则视为放弃奖励。"
            ).params("id", "operator", "duration", "rule", "description").build();

        }

        public static class SUSPEND extends ConfigurationRoot {

            public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.BLOCK_NOTE_BLOCK_BIT);

            public static final ConfiguredTitle TITLE = title().defaults(
                            "&e&l钓鱼大赛",
                            "&c&l!! &f比赛被中止 &c&l!!"
                    ).fadeIn(10).stay(40).fadeOut(10)
                    .params("id").build();

            public static final ConfiguredMessageList<BaseComponent[]> MESSAGE = list().defaults(
                    "&c&l注意！&f本次 [&e&l钓鱼大赛](hover=&7ID &8#&F%(id)) &f由于特殊原因已被中止。"
            ).params("id").build();

        }


        public static class STOP extends ConfigurationRoot {
            public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.ENTITY_PLAYER_LEVELUP);

            public static final ConfiguredTitle TITLE = title().defaults(
                            "&e&l钓鱼大赛",
                            "&f&l比赛结束"
                    ).fadeIn(10).stay(40).fadeOut(10)
                    .params("id", "participants").build();

            public static final ConfiguredMessageList<BaseComponent[]> HEADER = list().defaults(
                    "&<#cc3366>-------------------------&<#cc6600>-------------------------&<#cc3366>",
                    "[&e&l钓鱼大赛](hover=&7ID &8#&F%(id)) &f比赛结束，本次共有 &e%(participants) &f人参与其中。"
            ).params("id", "participants").build();

            public static final ConfiguredMessageList<BaseComponent[]> RANK = list().defaults(
                    " &8#%(index) &f%(player) &8- &f%(score) &7(%(count)条)"
            ).params("index", "player", "score", "count").build();

            public static final ConfiguredMessageList<BaseComponent[]> FOOTER = list().defaults(
                    "&f您可以 &e&l[[点击这里]](hover=点击查看信息 run_command=/fishcontest stats %(id)) &f来查看本次大赛的详细信息。",
                    "&<#cc3366>-------------------------&<#cc6600>-------------------------&<#cc3366>"
            ).params("id", "participants").build();

            public static final ConfiguredMessageList<BaseComponent[]> MINE_NOT_REWARD = list().defaults(
                    "&f本次大赛中，您的成绩为 &e%(score)&7(%(count)条) &f，排名第 &e%(index) &f名。"
            ).params("index", "score", "count").build();

            public static final ConfiguredMessageList<BaseComponent[]> MINE_REWARDED = list().defaults(
                    "&f本次大赛中，您的成绩为 &e%(score)&7(%(count)条) &f，排名第 &e%(index) &f名。",
                    "&f相关奖励已发放到您的账户中，请注意查收。"
            ).params("index", "score", "count").build();
        }


    }


}
