package cc.carm.outsource.plugin.fishcontest.hook;

import cc.carm.lib.easyplugin.papi.EasyPlaceholder;
import cc.carm.lib.easyplugin.papi.handler.PlaceholderHandler;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.data.FishContestData;
import cc.carm.outsource.plugin.fishcontest.utils.TimeDateUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class PluginExpansion extends EasyPlaceholder {

    public PluginExpansion(@NotNull JavaPlugin plugin, @NotNull String rootIdentifier) {
        super(plugin, rootIdentifier);

        handle("version", (player, args) -> getVersion());

        handleSection("remain", section -> {
            section.handle("seconds", contest(s -> s.getRemainMillis() / 1000));
            section.handle("time", contest(s -> TimeDateUtils.toDHMSStyle(s.getRemainMillis() / 1000)));
            section.handle("percentage", contest(s -> (double) s.getRemainMillis() / (double) s.getDuration().toMillis()));
        });

    }

    public PlaceholderHandler contest(Function<FishContestData, Object> function) {
        return (player, args) -> {
            FishContestData contest = Main.getContestManager().getRunningContest();
            if (contest == null) return "";
            else return function.apply(contest);
        };
    }

}
