package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class movementListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) { // hvis det er nedtelling og playeren er i gamet så kan dikke bevege seg
        if (BotBowsManager.canMove) {
            Player p = e.getPlayer();
            Material material = p.getLocation().add(0, -0.05, 0).getBlock().getType();
            if (material == Material.AIR) {
                material = p.getLocation().add(0, -0.1, 0).getBlock().getType();
            }
            switch (material) { // add effekter basert på åssen blokk som er under
                case YELLOW_CONCRETE, YELLOW_CONCRETE_POWDER -> p.addPotionEffect(new PotionEffect(org.bukkit.potion.PotionEffectType.JUMP_BOOST, 1200, 6, true, false));
                case CYAN_CONCRETE, CYAN_CONCRETE_POWDER, CYAN_CARPET -> {
                    double Δy = e.getTo().getY() - e.getFrom().getY();
                    if (Δy <= 0.1) {break;}
                    double vX = p.getLocation().getDirection().getX();
                    double vZ = p.getLocation().getDirection().getZ();

                    p.setVelocity(new Vector(vX*2.5, 0.5, vZ*2.5));
                    p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 10, 2);
                }
                default -> p.removePotionEffect(PotionEffectType.JUMP_BOOST);
            }
        }
        if (!BotBowsManager.isPlayerJoined(e.getPlayer())) {return;}
        Player p = e.getPlayer();
        if (p.getLocation().getX() == BotBowsManager.getPlayerSpawn(p).getX() && p.getLocation().getZ() == BotBowsManager.getPlayerSpawn(p).getZ()) {return;}
        // hvis det er countdown (!canMove), playeren er joina og playeren har gått vekk fra spawn blir han telportert tebake
        p.teleport(BotBowsManager.getPlayerSpawn(p));
    }
}
