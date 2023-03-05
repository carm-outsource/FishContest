package cc.carm.outsource.plugin.fishcontest.listener;

import cc.carm.lib.easylistener.EasyListener;
import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.conf.PluginConfig;
import cc.carm.outsource.plugin.fishcontest.conf.fish.ConfiguredFishes;
import cc.carm.outsource.plugin.fishcontest.conf.fish.FishConfig;
import cc.carm.outsource.plugin.fishcontest.data.FishContestData;
import cc.carm.outsource.plugin.fishcontest.data.FishItemData;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class FishListener extends EasyListener {

    public FishListener(Plugin plugin) {
        super(plugin);
    }


    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        FishContestData contest = Main.getContestManager().getRunningContest();
        if (contest == null) return;
        if (!(event.getCaught() instanceof Item item)) return;

        Player player = event.getPlayer();
        Biome biome = event.getHook().getLocation().getBlock().getBiome();
        ConfiguredFishes fishes = PluginConfig.ITEMS.getNotNull();

        FishConfig fish = fishes.randomFish(biome);
        if (fish == null) {
            Main.severe("在尝试为玩家 " + player.getName() + " 随机鱼时, 未找到对应的鱼类配置, 请检查配置文件");
            return;
        }

        FishItemData data = fish.createData(biome);
        contest.modifyUser(player, u -> u.addFish(data)); // 添加到本次用户的数据中

        ItemStack fishItem = fish.getItem(player, 1, data);
        item.setItemStack(fishItem);

    }

}
