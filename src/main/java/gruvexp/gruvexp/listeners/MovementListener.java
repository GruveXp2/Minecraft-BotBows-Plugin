package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) { // hvis det er nedtelling og playeren er i gamet så kan dikke bevege seg
        if (!BotBows.activeGame) return;
        Player p = e.getPlayer();
        if (!BotBows.settings.isPlayerJoined(e.getPlayer())) {return;}
        if (BotBows.botBowsGame.canMove) {
            BotBows.botBowsGame.handleMovement(e);
        } else {
            BotBowsPlayer bp = BotBows.getBotBowsPlayer(p);
            Location spawnPos = bp.getTeam().getSpawnPos(bp);
            if (p.getLocation().getX() == spawnPos.getX() && p.getLocation().getZ() == spawnPos.getZ()) {return;}
            // hvis det er countdown (!canMove), playeren er joina og playeren har gått vekk fra spawn blir han telportert tebake
            p.teleport(spawnPos);
        }
    }
}
