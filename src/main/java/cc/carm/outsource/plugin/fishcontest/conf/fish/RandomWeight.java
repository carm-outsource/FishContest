package cc.carm.outsource.plugin.fishcontest.conf.fish;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.outsource.plugin.fishcontest.Main;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public record RandomWeight(int defaults, Map<Biome, Integer> biomes) {

    public int get(@Nullable Biome biome) {
        return Optional.ofNullable(biome).map(b -> biomes.getOrDefault(b, defaults)).orElse(defaults);
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("defaults", defaults);
        biomes.forEach((biome, integer) -> data.put(biome.name(), integer));
        return data;
    }

    public static @Nullable RandomWeight parse(@Nullable ConfigurationWrapper<?> rootSection) {
        if (rootSection == null) return null;

        int weight = rootSection.getInt("defaults", 50);
        Map<Biome, Integer> biomes = new LinkedHashMap<>();

        ConfigurationWrapper<?> biomesSection = rootSection.getConfigurationSection("biomes");
        if (biomesSection != null) {
            for (String key : biomesSection.getKeys(false)) {
                try {
                    Biome biome = Arrays.stream(Biome.values()).filter(v -> v.name().equalsIgnoreCase(key)).findFirst().orElse(null);
                    if (biome == null) throw new NullPointerException("不存在ID为 " + key + " 的生物群系");

                    biomes.put(biome, biomesSection.getInt(key, weight));
                } catch (Exception ex) {
                    Main.severe("渔物权重配置错误: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }

        return new RandomWeight(weight, biomes);
    }

}
