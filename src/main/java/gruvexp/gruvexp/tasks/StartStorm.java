package gruvexp.gruvexp.tasks;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import gruvexp.gruvexp.twtClassic.Cooldowns;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class StartStorm extends BukkitRunnable {

    boolean b = true;
    @Override
    public void run() {
        if (b) {
            BotBowsManager.messagePlayers(ChatColor.DARK_RED + "STORM INCOMING!" + ChatColor.RED + " Seek shelter immediately!");
            BotBowsManager.titlePlayers(ChatColor.RED + "STORM INCOMING", 80);
            b = false;
        } else {
            Cooldowns.stormRunnableInit();
            Main.WORLD.setThundering(true);
            Main.WORLD.setStorm(true);
            Main.WORLD.setThunderDuration(6000);
            cancel();
        }
    }
}
