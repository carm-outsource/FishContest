package cc.carm.outsource.plugin.fishcontest.conf.fish;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.mineconfiguration.bukkit.data.ItemConfig;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.impl.FixedQuantity;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.impl.GaussianQuantity;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.impl.RangeQuantity;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record ConfiguredFishes(@Unmodifiable Map<String, FishConfig> configs) {

    public FishConfig getFish(String id) {
        return configs.get(id);
    }

    public @Nullable FishConfig randomFish(@Nullable Biome biome) {

        Map<FishConfig, Integer> weightMap = new LinkedHashMap<>();
        configs.forEach((id, config) -> weightMap.put(config, config.getWeight(biome)));

        int totalWeight = weightMap.values().stream().mapToInt(Integer::intValue).sum();
        int randomWeight = (int) (Math.random() * totalWeight);

        int currentWeight = 0;
        for (Map.Entry<FishConfig, Integer> entry : weightMap.entrySet()) {
            currentWeight += entry.getValue();
            if (currentWeight >= randomWeight) {
                return entry.getKey();
            }
        }

        return null;
    }


    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        configs.forEach((id, config) -> data.put(id, config.serialize()));
        return data;
    }

    public static @NotNull ConfiguredFishes defaults() {
        Map<String, FishConfig> data = new LinkedHashMap<>();

        ItemConfig fishA = new ItemConfig(Material.SALMON, "鲑鱼", List.of(
                " ",
                " &7重量 &a%(quantity)",
                " &7渔者 &a%player_name%",
                " ",
                "这是一条神奇的鲑鱼"
        ));
        data.put("SALMON", new FishConfig(
                "SALMON", fishA,
                new RandomWeight(50, new LinkedHashMap<>()),
                new FishQuantity(new RangeQuantity(200, 500), Map.of(
                        Biome.DESERT, new FixedQuantity(50),
                        Biome.RIVER, new RangeQuantity(200, 2000),
                        Biome.OCEAN, new GaussianQuantity(50, 400, 4000)
                ))
        ));

        ItemConfig fishB = new ItemConfig(Material.PUFFERFISH, "&e&l滑稽果", List.of(
                "&e滑稽树上滑稽果",
                "&e滑稽树下你和我",
                "&e滑稽树旁做游戏",
                "&e欢乐多又多"
        ));
        data.put("PUFFERFISH", new FishConfig(
                "PUFFERFISH", fishB,
                new RandomWeight(1, new LinkedHashMap<>()),
                null)
        );


        return new ConfiguredFishes(data);
    }

    public static @Nullable ConfiguredFishes parse(@Nullable ConfigurationWrapper<?> rootSection) {
        if (rootSection == null) return null;

        Map<String, FishConfig> data = new LinkedHashMap<>();
        for (String id : rootSection.getKeys(false)) {
            ConfigurationWrapper<?> section = rootSection.getConfigurationSection(id);
            if (section == null) continue;
            try {
                FishConfig config = FishConfig.parse(id, section);
                if (config != null) data.put(id, config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ConfiguredFishes(data);
    }

}
