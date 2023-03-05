package cc.carm.outsource.plugin.fishcontest.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserContestData {

    protected final UUID uuid;
    protected final String name;

    protected List<FishItemData> items = new ArrayList<>();

    public UserContestData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    public List<FishItemData> getItems() {
        return items;
    }

    public void addFish(FishItemData data) {
        items.add(data);
    }

    public int getScore(RankingRule rule) {
        return switch (rule) {
            case MAX -> getMaxQuantity();
            case SUM -> getTotalQuantity();
        };
    }

    public String getScoreString(RankingRule rule) {
        return FishItemData.getQuantityString(getScore(rule));
    }

    public int getMaxQuantity() {
        return getItems().stream().mapToInt(FishItemData::quantity).max().orElse(0);
    }

    public String getMaxQuantityString() {
        return FishItemData.getQuantityString(getMaxQuantity());
    }

    public int getTotalQuantity() {
        return getItems().stream().mapToInt(FishItemData::quantity).sum();
    }

    public String getTotalQuantityString() {
        return FishItemData.getQuantityString(getTotalQuantity());
    }


}
