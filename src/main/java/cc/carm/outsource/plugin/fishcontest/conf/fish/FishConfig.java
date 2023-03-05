package cc.carm.outsource.plugin.fishcontest.conf.fish;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.mineconfiguration.bukkit.data.ItemConfig;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.QuantityValue;
import cc.carm.outsource.plugin.fishcontest.data.FishItemData;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public record FishConfig(
        @NotNull String id, @NotNull ItemConfig item,
        @Nullable RandomWeight weight, @Nullable FishQuantity quantity
) {

    public ItemStack getItem(@Nullable Player player, int amount, int quantity) {
        return item.getItemStack(player, amount, Map.of("%(quantity)", quantity));
    }

    public ItemStack getItem(@Nullable Player player, int amount, FishItemData data) {
        return item.getItemStack(player, amount, Map.of("%(quantity)", data.getQuantityString()));
    }

    public @NotNull FishItemData createData(@NotNull Biome biome) {
        return FishItemData.create(this, biome);
    }

    public int getWeight(@Nullable Biome biome) {
        return Optional.ofNullable(weight).map(w -> w.get(biome)).orElse(50);
    }

    public @Nullable QuantityValue getQuantity(@Nullable Biome biome) {
        return Optional.ofNullable(quantity).map(q -> q.get(biome)).orElse(null);
    }


    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("item", item.serialize());
        if (weight != null) data.put("weight", weight.serialize());
        if (quantity != null) data.put("quantity", quantity.serialize());
        return data;
    }

    public static @Nullable FishConfig parse(@NotNull String id, @Nullable ConfigurationWrapper<?> rootSection) throws Exception {
        if (rootSection == null) return null;

        ConfigurationWrapper<?> itemSection = rootSection.getConfigurationSection("item");
        if (itemSection == null) throw new NullPointerException("渔物 #" + id + " 未配置物品内容。");

        ItemConfig item = ItemConfig.deserialize(itemSection);
        FishQuantity quantity = FishQuantity.parse(rootSection.getConfigurationSection("quantity"));
        RandomWeight weight = RandomWeight.parse(rootSection.getConfigurationSection("weight"));

        return new FishConfig(id, item, weight, quantity);
    }
}
