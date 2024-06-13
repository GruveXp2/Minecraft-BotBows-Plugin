package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class CountDownListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) { // hvis det er nedtelling og playeren er i gamet så kan de ikke bevege seg
        if (BotBowsManager.canMove) {return;}
        if (!BotBowsManager.isPlayerJoined(e.getPlayer())) {return;}
        Player p = e.getPlayer();
        if (p.getLocation().getX() == BotBowsManager.getPlayerSpawn(p).getX() && p.getLocation().getZ() == BotBowsManager.getPlayerSpawn(p).getZ()) {return;}
        // hvis det er countdown (!canMove), playeren er joina og playeren har gått vekk fra spawn blir han telportert tebake
        p.teleport(BotBowsManager.getPlayerSpawn(p));
    }
}
