package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.Glow;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!BotBowsManager.activeGame) {return;}
        if (!(e.getDamager() instanceof Arrow arrow)) {return;} // entitien som utførte damag
        if (!(arrow.getShooter() instanceof Player attacker)) {return;} // den som skøyt
        if (!(e.getEntity() instanceof Player defender)) {return;} // den som blei hitta
        arrow.setKnockbackStrength(8);
        if (BotBowsManager.getTeam(attacker) == BotBowsManager.getTeam(defender) || attacker.isGlowing()) {
            arrow.remove(); // if the player already was hit and has a cooldown, or if the hit player is of the same team as the attacker, the arrow won't do damage
            e.setCancelled(true);
            return;
        }
        e.setDamage(0.01); // de skal ikke daue men bli satt i spectator til runda er ferig
        BotBowsManager.handleHit(defender, attacker);
        new Glow(defender).runTaskTimer(Main.getPlugin(), 0L, 40L); // 2s glowing
    }
}
