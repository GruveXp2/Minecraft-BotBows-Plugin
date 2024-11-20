package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.twtClassic.BotBows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        BotBows.leaveGame(p);
    }

}
