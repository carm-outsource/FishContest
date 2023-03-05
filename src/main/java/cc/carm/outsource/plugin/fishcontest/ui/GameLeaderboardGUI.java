package cc.carm.outsource.plugin.fishcontest.ui;

import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.outsource.plugin.fishcontest.conf.PluginConfig;
import cc.carm.outsource.plugin.fishcontest.conf.PluginMessages;
import cc.carm.outsource.plugin.fishcontest.data.FishContestData;
import cc.carm.outsource.plugin.fishcontest.data.UserContestData;
import cc.carm.outsource.plugin.fishcontest.utils.TimeDateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class GameLeaderboardGUI extends AutoPagedGUI {

    public static void open(@NotNull Player player, @NotNull FishContestData contest) {
        GameLeaderboardGUI gui = new GameLeaderboardGUI(player, contest);
        PluginConfig.SOUND.GUI_OPEN.playTo(player);
        gui.openGUI(player);
    }

    protected final @NotNull Player player;
    protected final @NotNull FishContestData contest;

    public GameLeaderboardGUI(@NotNull Player player, @NotNull FishContestData contest) {
        super(GUIType.FIVE_BY_NINE, PluginConfig.GUI.TITLE.parse(player, contest.getID(), contest.getRankingRule()), 19, 25);
        this.contest = contest;
        this.player = player;

        setPreviousPageSlot(38);
        setNextPageSlot(42);

        loadPlayers();
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull FishContestData getContest() {
        return contest;
    }

    public void loadPlayers() {
        List<UserContestData> ranked = getContest().getRankedData();

        int playerRank = 1;
        String playerScore = null;

        for (UserContestData userContestData : ranked) {
            if (userContestData.getUUID().equals(player.getUniqueId())) {
                playerScore = userContestData.getScoreString(getContest().getRankingRule());
            } else {
                playerRank++;
            }
        }

        String none = Optional.ofNullable(PluginMessages.NONE.parse(player)).orElse(" ");
        ItemStack icon = PluginConfig.GUI.ICONS.CONTEST.getItem(
                player, 1,
                getContest().getID(), getContest().getRankingRule().getName(player),
                TimeDateUtils.toDHMSStyle(getContest().getDuration().toSeconds()),
                TimeDateUtils.getTimeString(getContest().getStartMillis()),
                TimeDateUtils.getTimeString(getContest().getEndMillis()),
                getContest().getData().size(),
                playerScore == null ? none : playerRank,
                playerScore == null ? none : playerScore
        );
        setItem(40, new GUIItem(icon));


        for (int i = 0; i < 3; i++) {
            int slot = i + 12;
            int index = i + 1;
            if (ranked.size() > i) {
                setItem(slot, generateItem(index, ranked.get(i)));
            } else {
                setItem(slot, new GUIItem(PluginConfig.GUI.ICONS.EMPTY.getItem(player, 1, index)));
            }
        }

        if (ranked.size() > 3) {
            for (int i = 3; i < ranked.size(); i++) {
                addItem(generateItem(i + 1, ranked.get(i)));
            }
        }

        if (getItemsContainer().size() < 7) {
            int index = getItemsContainer().size() + 4;
            for (int i = index; i <= 10; i++) {
                addItem(new GUIItem(PluginConfig.GUI.ICONS.EMPTY.getItem(player, 1, index)));
                index++;
            }
        }

    }

    public GUIItem generateItem(int index, UserContestData data) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (head.getItemMeta() instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(data.getUUID()));

            Object[] values = new Object[]{
                    data.getName(), index,
                    data.getScore(getContest().getRankingRule()), data.getItems().size(),
                    data.getTotalQuantityString(), data.getMaxQuantityString(),
            };

            skullMeta.setDisplayName(PluginConfig.GUI.ICONS.USER.NAME.parse(player, values));
            skullMeta.setLore(PluginConfig.GUI.ICONS.USER.LORE.parse(player, values));

            if (data.getUUID().equals(getPlayer().getUniqueId())) {
                skullMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            head.setItemMeta(skullMeta);
        }
        return new GUIItem(head);
    }

}
