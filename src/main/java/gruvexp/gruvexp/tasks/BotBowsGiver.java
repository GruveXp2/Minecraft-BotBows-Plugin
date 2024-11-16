package gruvexp.gruvexp.tasks;

import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class BotBowsGiver  extends BukkitRunnable {

    @Override
    public void run() {
        for (BotBowsPlayer p : BotBows.getPlayers()) {
            if (!BotBows.activeGame) {
                cancel();
                return;
            }
            if (!p.isDamaged()) {
                p.PLAYER.getInventory().setItem(0, BotBows.BOTBOW);
            }
        }
    }
}
