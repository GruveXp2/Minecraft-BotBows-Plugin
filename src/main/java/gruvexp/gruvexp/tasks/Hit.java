package gruvexp.gruvexp.tasks;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Hit extends BukkitRunnable {

    Player p;
    Inventory inv;
    public Hit(Player p) {
        this.p = p;
        this.inv = p.getInventory();
    }

    int time = 0;
    @Override
    public void run() {
        if (time == 0) {
            p.setGlowing(true);
            p.setInvulnerable(true);
            if (p.getGameMode() == GameMode.SURVIVAL) {
                p.setGameMode(GameMode.ADVENTURE); // midlertidig
            }
            BotBowsManager.isDamaged.put(p, true);
            for (int i = 0; i < 9; i++) { // flytter items fra hotbar 1 hakk opp
                inv.setItem(i + 27, inv.getItem(i));
                inv.setItem(i, new ItemStack(Material.BARRIER));
            }
            time ++;
        } else if (time == 1) {
            p.setGlowing(false);
            p.setInvulnerable(false);
            if (p.getGameMode() == GameMode.ADVENTURE) {
                p.setGameMode(GameMode.SURVIVAL); // midlertidig
            }
            BotBowsManager.isDamaged.put(p, false);
            for (int i = 0; i < 9; i++) { // flytter items tilbake
                inv.setItem(i, inv.getItem(i + 27));
                p.getInventory().setItem(i + 27, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            }
            cancel();
        }
    }
}
