package cc.carm.outsource.plugin.fishcontest.data;

import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.ToIntFunction;

public enum RankingRule {

    SUM(
            comparor(UserContestData::getTotalQuantity),
            PluginMessages.CONTEST.RULE.SUM.NAME, PluginMessages.CONTEST.RULE.SUM.DESCRIPTION
    ),
    MAX(
            comparor(UserContestData::getMaxQuantity),
            PluginMessages.CONTEST.RULE.MAX.NAME, PluginMessages.CONTEST.RULE.MAX.DESCRIPTION
    );

    private final Comparator<UserContestData> comparator;
    private final ConfiguredMessage<String> name;
    private final ConfiguredMessageList<String> description;


    RankingRule(@NotNull Comparator<UserContestData> comparator,
                @NotNull ConfiguredMessage<String> name, @NotNull ConfiguredMessageList<String> description) {
        this.comparator = comparator;
        this.name = name;
        this.description = description;
    }

    public @NotNull ConfiguredMessage<String> getName() {
        return name;
    }

    public @NotNull String getName(Player player) {
        return Optional.ofNullable(getName().parse(player)).orElse(name());
    }

    public @NotNull ConfiguredMessageList<String> getDescription() {
        return description;
    }

    public @NotNull List<String> getDescription(Player player) {
        return Optional.ofNullable(getDescription().parse(player)).orElse(Collections.emptyList());
    }

    @Unmodifiable
    public @NotNull List<UserContestData> sort(Map<UUID, UserContestData> data) {
        List<UserContestData> values = new ArrayList<>();
        data.values().stream().sorted((o1, o2) -> {
            int compared = comparator.compare(o1, o2);
            if (compared != 0) return compared;
            else return compare(u -> u.getItems().size(), o1, o2);
        }).forEachOrdered(values::add);
        return Collections.unmodifiableList(values);
    }

    private static int compare(ToIntFunction<UserContestData> intFunction, UserContestData u1, UserContestData u2) {
        return Integer.compare(intFunction.applyAsInt(u1), intFunction.applyAsInt(u2));
    }

    private static @NotNull Comparator<UserContestData> comparor(ToIntFunction<UserContestData> intFunction) {
        return (o1, o2) -> compare(intFunction, o1, o2);
    }

    public static RankingRule parse(String v, RankingRule d) {
        return Arrays.stream(RankingRule.values())
                .filter(r -> r.name().equalsIgnoreCase(v))
                .findFirst().orElse(d);
    }

}
