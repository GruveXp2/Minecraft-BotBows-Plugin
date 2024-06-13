package gruvexp.gruvexp.tasks;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BotBowsGiver  extends BukkitRunnable {

    ItemStack BOTBOW = BotBowsManager.getBotBow();

    @Override
    public void run() {
        for (Player p : BotBowsManager.getPlayers()) {
            if (!BotBowsManager.activeGame) {
                cancel();
                return;
            }
            if (!BotBowsManager.isDamaged.get(p)) {
                p.getInventory().setItem(0, BOTBOW);
            }
        }
    }
}
