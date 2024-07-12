package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import gruvexp.gruvexp.twtClassic.BotBowsMap;
import org.bukkit.Location;
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
        Player p = e.getPlayer();
        if (BotBowsManager.canMove) {
            Material material = p.getLocation().add(0, -0.05, 0).getBlock().getType(); // sjekker rett under, bare 0.05 itilfelle det er teppe
            if (material == Material.AIR) {
                material = p.getLocation().add(0, -0.9, 0).getBlock().getType(); // hvis man står på kanten av et teppe kan det være en effektblokk under
            }
            switch (material) { // add effekter basert på åssen blokk som er under
                case YELLOW_CONCRETE, YELLOW_CONCRETE_POWDER -> p.addPotionEffect(new PotionEffect(org.bukkit.potion.PotionEffectType.JUMP_BOOST, 1200, 6, true, false));
                case CYAN_CONCRETE, CYAN_CONCRETE_POWDER, CYAN_CARPET -> {
                    double Δy = e.getTo().getY() - e.getFrom().getY();
                    if (Δy <= 0.1) {break;} // fortsett bare viss man har hoppa (et visst antall upwards momentum)
                    double vX = p.getLocation().getDirection().getX();
                    double vZ = p.getLocation().getDirection().getZ();

                    p.setVelocity(new Vector(vX*2.5, 0.5, vZ*2.5));
                    p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 10, 2);
                }
                default -> p.removePotionEffect(PotionEffectType.JUMP_BOOST);
            }
            if (BotBowsManager.isPlayerJoined(p) && BotBowsManager.currentMap == BotBowsMap.GRAUT_VS_WACKY && BotBowsManager.activeGame && BotBowsManager.isInDungeon(p)) {
                BotBowsManager.handleDungeonMovement(p);
            }
        } else {
            if (!BotBowsManager.isPlayerJoined(e.getPlayer())) {return;}
            Location spawnPos = BotBowsManager.getPlayerSpawn(p);
            if (p.getLocation().getX() == spawnPos.getX() && p.getLocation().getZ() == spawnPos.getZ()) {return;}
            // hvis det er countdown (!canMove), playeren er joina og playeren har gått vekk fra spawn blir han telportert tebake
            p.teleport(spawnPos);
        }
    }
}
