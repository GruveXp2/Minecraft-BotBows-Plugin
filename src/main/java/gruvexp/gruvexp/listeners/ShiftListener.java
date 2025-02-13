package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.SneakCoolDown;
import gruvexp.gruvexp.twtClassic.Bar;
import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.Cooldowns;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ShiftListener implements Listener {

    @EventHandler
    public void onShiftToggle(PlayerToggleSneakEvent e) {

        if (!BotBows.activeGame) {return;}
        Player p = e.getPlayer();
        if (!BotBows.settings.isPlayerJoined(p)) {return;}
        if (p.getGameMode() != GameMode.ADVENTURE) {return;}
        if (p.isSneaking()) {
            Bar.setSneakBarColor(p, ChatColor.RED, BarColor.RED);
            return;
        }

        if (Cooldowns.sneakCooldowns.get(p) <= 0) {
            Cooldowns.sneakRunnables.put(p, new SneakCoolDown(p).runTaskTimer(Main.getPlugin(), 0L, 1L));
            Bar.setSneakBarVisibility(p, true);
        } else {
            e.setCancelled(true);
            p.setSneaking(false);
        }
    }

}
