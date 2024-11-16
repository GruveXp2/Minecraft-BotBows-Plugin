package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class movementListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) { // hvis det er nedtelling og playeren er i gamet så kan dikke bevege seg
        Player p = e.getPlayer();
        if (BotBows.activeGame && BotBows.botBowsGame.canMove) {
            BotBows.botBowsGame.handleMovement(e);
        } else {
            if (!BotBows.activeGame) return;
            if (!BotBows.settings.isPlayerJoined(e.getPlayer())) {return;}
            BotBowsPlayer botBowsPlayer = BotBows.getBotBowsPlayer(p);
            Location spawnPos = botBowsPlayer.getTeam().getSpawnPos(p);
            if (p.getLocation().getX() == spawnPos.getX() && p.getLocation().getZ() == spawnPos.getZ()) {return;}
            // hvis det er countdown (!canMove), playeren er joina og playeren har gått vekk fra spawn blir han telportert tebake
            p.teleport(spawnPos);
        }
    }
}
