package cc.carm.outsource.plugin.fishcontest.conf.fish;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.QuantityValue;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public record FishQuantity(
        @NotNull QuantityValue defaults,
        @NotNull Map<Biome, QuantityValue> biomes
) {

    public @NotNull QuantityValue get(@Nullable Biome biome) {
        return Optional.ofNullable(biome).map(b -> biomes.getOrDefault(b, defaults)).orElse(defaults);
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("defaults", defaults.serialize());
        biomes.forEach((biome, v) -> data.put(biome.name(), v.serialize()));
        return data;
    }

    public static @Nullable FishQuantity parse(@Nullable ConfigurationWrapper<?> rootSection) {
        if (rootSection == null) return null;

        QuantityValue defaults = QuantityValue.parse(rootSection.getString("defaults", "200;500"));
        if (defaults == null) defaults = QuantityValue.defaults();

        Map<Biome, QuantityValue> biomes = new LinkedHashMap<>();

        ConfigurationWrapper<?> biomesSection = rootSection.getConfigurationSection("biomes");
        if (biomesSection != null) {
            for (String key : biomesSection.getKeys(false)) {
                try {
                    Biome biome = Arrays.stream(Biome.values()).filter(v -> v.name().equalsIgnoreCase(key)).findFirst().orElse(null);
                    if (biome == null) throw new NullPointerException("不存在ID为 " + key + " 的生物群系");

                    QuantityValue quantityValue = QuantityValue.parse(biomesSection.getString(key));
                    if (quantityValue == null) {
                        throw new NullPointerException("群系 #" + biome.name() + " 的数量配置错误，请检查！");
                    }
                    biomes.put(biome, quantityValue);
                } catch (Exception ex) {
                    Main.severe("渔物配额配置错误: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }

        return new FishQuantity(defaults, biomes);
    }

}
