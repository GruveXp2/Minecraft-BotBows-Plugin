package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.Hit;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Arrow arrow)) {
            if (!(arrow.getShooter() instanceof Player attacker)) {return;} // den som skøyt
            if (!(e.getEntity() instanceof Player defender)) {return;} // den som blei hitta
            if (!BotBowsManager.isPlayerJoined(attacker) || !BotBowsManager.isPlayerJoined(defender)) {return;} // hvis de ikke er i gamet
            arrow.setKnockbackStrength(8);
            if (BotBowsManager.getTeam(attacker) == BotBowsManager.getTeam(defender) || attacker.isGlowing()) {
                arrow.remove(); // if the player already was hit and has a cooldown, or if the hit player is of the same team as the attacker, the arrow won't do damage
                e.setCancelled(true);
                return;
            }
            e.setDamage(0.01); // de skal ikke daue men bli satt i spectator til runda er ferig
            BotBowsManager.handleHit(defender, attacker);
            new Hit(defender).runTaskTimer(Main.getPlugin(), 0L, 40L); // 2s glowing
        } else {
            if (!(e.getEntity() instanceof Player defender)) {return;} // den som blei hitta
            if (BotBowsManager.isPlayerJoined(defender)) {
                e.setCancelled(true); // cant damage ingame players without bow
            }// entitien som utførte damag
        }
    }
}
