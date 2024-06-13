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
        if (!(e.getDamager() instanceof Arrow)) {return;} // entitien som utførte damag
        Arrow arrow = (Arrow) e.getDamager();
        if (!(arrow.getShooter() instanceof Player)) {return;} // den som skøyt
        if (!(e.getEntity() instanceof Player)) {return;} // den som blei hitta
        arrow.setKnockbackStrength(8);
        Player attacker = (Player) arrow.getShooter();
        Player defender = (Player) e.getEntity();
        if (BotBowsManager.getTeamColor(attacker) == BotBowsManager.getTeamColor(defender) || attacker.isGlowing()) {
            arrow.remove(); // friendly fire off
            e.setCancelled(true);
            return;
        }
        e.setDamage(0.01); // de skal ikke daue men bli satt i spectator mode til runda er ferig
        BotBowsManager.handleHit(defender, attacker);
        new Glow(defender).runTaskTimer(Main.getPlugin(), 0L, 40L); // 2 SEKUNDER GLOWING
    }
}
