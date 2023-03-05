package cc.carm.outsource.plugin.fishcontest.data;

import cc.carm.outsource.plugin.fishcontest.conf.PluginConfig;
import cc.carm.outsource.plugin.fishcontest.conf.fish.FishConfig;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.QuantityValue;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public record FishItemData(@NotNull String linkedID, int quantity) {

    public static FishItemData create(@NotNull FishConfig config, @Nullable Biome biome) {
        int quantity = Optional.ofNullable(config.getQuantity(biome)).map(QuantityValue::randomValue).orElse(-1);
        return new FishItemData(config.id(), quantity);
    }

    public FishConfig getLinkedFish() {
        return PluginConfig.ITEMS.getNotNull().getFish(linkedID);
    }

    public @NotNull String getQuantityString() {
        return getQuantityString(quantity());
    }

    public static String getQuantityString(int quantity) {
        // The measurement map from PluginConfig is an order-reversed TreeMap
        Map<Integer, String> measurement = PluginConfig.MEASUREMENT.LEVELS;

        if (quantity <= 0 || measurement.keySet().stream().allMatch(i -> i > quantity)) {
            // All measurement units are larger than the quantity
            // so return the empty messages.
            return PluginConfig.MEASUREMENT.EMPTY.getNotNull();
        }

        int cache = quantity;
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<Integer, String> entry : measurement.entrySet()) {
            int unit = entry.getKey();
            String name = entry.getValue();

            if (cache > unit) {
                int count = cache / unit;
                cache = cache % unit;
                builder.append(count).append(name);
            }
        }

        return builder.toString();
    }

}
