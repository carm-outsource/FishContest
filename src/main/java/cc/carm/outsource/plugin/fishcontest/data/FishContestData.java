package cc.carm.outsource.plugin.fishcontest.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

public class FishContestData {

    public static final @NotNull SimpleDateFormat ID_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    protected final String id;
    protected final @NotNull RankingRule type;
    protected final @NotNull Map<UUID, UserContestData> dataMap = new HashMap<>();

    protected final @Nullable UUID operator;

    protected final long startMillis;
    protected long endMillis;

    public FishContestData(@NotNull RankingRule type, @Nullable UUID operator, Duration duration) {
        this.id = ID_FORMAT.format(new Date());
        this.type = type;
        this.operator = operator;
        this.startMillis = System.currentTimeMillis();
        this.endMillis = this.startMillis + duration.toMillis();
    }

    public String getID() {
        return id;
    }

    public @NotNull RankingRule getRankingRule() {
        return type;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    public void setEndMillis(long endMillis) {
        this.endMillis = endMillis;
    }

    public boolean isEnded() {
        return System.currentTimeMillis() > getEndMillis();
    }

    public @NotNull Duration getDuration() {
        return Duration.ofMillis(getEndMillis() - getStartMillis());
    }

    @Unmodifiable
    public @NotNull Map<UUID, UserContestData> getData() {
        return Collections.unmodifiableMap(dataMap);
    }

    @Unmodifiable
    public @NotNull List<UserContestData> getRankedData() {
        return type.sort(dataMap);
    }

    public @Nullable UserContestData getUser(@NotNull UUID uuid) {
        return dataMap.get(uuid);
    }

    public void modifyUser(@NotNull Player player, Consumer<UserContestData> dataConsumer) {
        UUID uuid = player.getUniqueId();
        if (dataMap.containsKey(uuid)) {
            dataConsumer.accept(dataMap.get(uuid));
        } else {
            UserContestData data = new UserContestData(player.getUniqueId(), player.getName());
            dataConsumer.accept(data);
            dataMap.put(uuid, data);
        }
    }


}
