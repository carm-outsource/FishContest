package cc.carm.outsource.plugin.fishcontest.conf.prize;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.outsource.plugin.fishcontest.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public record ConfiguredPrize(@Unmodifiable Map<Integer, List<String>> commands) {

    public void execute(int index, @NotNull Player player) {
        List<String> commands = this.commands.get(index);
        if (commands == null) return;

        for (String command : commands) {
            try {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageUtils.setPlaceholders(player, command));
            } catch (Exception ex) {
                Main.severe("在为玩家 " + player.getName() + " 发放奖励时出现错误: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public @NotNull Map<String, Object> serialize() {
        return commands.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().toString(),
                Map.Entry::getValue,
                (a, b) -> b, TreeMap::new
        ));
    }

    public static @NotNull ConfiguredPrize defaults() {
        Map<Integer, List<String>> data = new TreeMap<>();
        data.put(3, List.of("say %player_name% 排第三名！")); // TEST AUTO SORT
        data.put(1, List.of("say %player_name% 排第一名！"));
        data.put(2, List.of("say %player_name% 排第二名！"));
        return new ConfiguredPrize(data);
    }

    public static @Nullable ConfiguredPrize parse(@Nullable ConfigurationWrapper<?> rootSection) {
        if (rootSection == null) return null;

        Map<Integer, List<String>> data = new TreeMap<>();
        for (String key : rootSection.getKeys(false)) {
            try {
                int index = Integer.parseInt(key);
                List<String> commands = rootSection.getStringList(key);
                if (commands.isEmpty()) continue;
                data.put(index, commands);
            } catch (Exception ex) {
                Main.severe("奖励配置错误: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return new ConfiguredPrize(data);
    }
    
}
