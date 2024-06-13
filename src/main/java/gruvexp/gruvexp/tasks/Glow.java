package gruvexp.gruvexp.tasks;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Glow extends BukkitRunnable {

    Player p;
    public Glow(Player p) {
        this.p = p;
    }

    int time = 0;
    @Override
    public void run() {
        if (time == 0) {
            p.setGlowing(true);
            p.setInvulnerable(true);
            BotBowsManager.Player2IsDamaged.put(p, true);
            for (int i = 0; i < 9; i++) {
                p.getInventory().setItem(i, new ItemStack(Material.BARRIER));
            }
            time ++;
        } else if (time == 1) {
            p.setGlowing(false);
            p.setInvulnerable(false);
            BotBowsManager.Player2IsDamaged.put(p, false);
            for (int i = 0; i < 9; i++) {
                p.getInventory().setItem(i, null);
            }
            cancel();
        }
    }
}
