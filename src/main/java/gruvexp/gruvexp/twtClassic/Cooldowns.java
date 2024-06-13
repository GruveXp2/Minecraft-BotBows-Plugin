package gruvexp.gruvexp.twtClassic;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.StormCooldown;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Cooldowns { // Inneholder shifting og storm logic

    public static HashMap<Player, BukkitTask> Player2SneakRunnable = new HashMap<>();
    public static HashMap<Player, BukkitTask> Player2StormRunnable = new HashMap<>();
    public static HashMap<Player, Integer> Player2SneakCoolDown = new HashMap<>();
    public static HashMap<Player, Integer> Player2StormCoolDown = new HashMap<>();

    public static void CoolDownInit(ArrayList<Player> players) {
        for (Player p : players) {
            Player2SneakCoolDown.put(p, 0);
        }
        if (BotBowsManager.stormMode) {
            for (Player p : players) {
                Player2StormCoolDown.put(p, 0);
            }
        }
    }

    public static void stormRunnableInit() {
        for (Player p : BotBowsManager.getPlayers()) {
            Player2StormRunnable.put(p, new StormCooldown(p).runTaskTimer(Main.getPlugin(), 0L, 2L));
        }
    }

    public static void resetStormRunnable() {
        for (Player p : BotBowsManager.getPlayers()) {
            Player2StormCoolDown.put(p, 0);
            if (Player2StormRunnable.containsKey(p)) {
                Player2StormRunnable.get(p).cancel();
            }
            Bar.setStormBarProgress(p, 0d);
            Bar.setStormBarVisibility(p, false);
        }
        Player2StormRunnable.clear();
    }
}
