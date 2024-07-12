package gruvexp.gruvexp.tasks;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class RoundCountdown extends BukkitRunnable { // LANGUAGE LEVEL = 14

    int time = 0;

    @Override
    public void run() {
        switch (time) {
            case 0, 1, 2, 3, 4 ->
                    BotBowsManager.messagePlayers(STR."\{ChatColor.BOLD}\{ChatColor.GREEN}BotBows Classic \{ChatColor.RESET}is starting in \{ChatColor.GOLD}\{5 - time}");
            case 5 -> {
                BotBowsManager.messagePlayers(STR."\{ChatColor.BOLD}\{ChatColor.GREEN}BotBows Classic \{ChatColor.RESET}has started!");
                BotBowsManager.canMove = true;
                cancel(); // stopper loopen
            }
        }
        time ++;
    }
}
