package gruvexp.gruvexp.twtClassic;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.StormCooldown;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cooldowns { // Inneholder shifting og storm logic

    public static Map<Player, BukkitTask> sneakRunnables = new HashMap<>();
    public static Map<Player, BukkitTask> stormRunnables = new HashMap<>();
    public static Map<Player, Integer> sneakCooldowns = new HashMap<>();
    public static Map<Player, Integer> stormCooldowns = new HashMap<>();

    public static void CoolDownInit(List<Player> players) {
        for (Player p : players) {
            sneakCooldowns.put(p, 0);
        }
        if (BotBowsManager.stormMode) {
            for (Player p : players) {
                stormCooldowns.put(p, 0);
            }
        }
    }

    public static void stormRunnableInit() {
        for (Player p : BotBowsManager.getPlayers()) {
            stormRunnables.put(p, new StormCooldown(p).runTaskTimer(Main.getPlugin(), 0L, 2L));
        }
    }

    public static void resetStormRunnable() {
        for (Player p : BotBowsManager.getPlayers()) {
            stormCooldowns.put(p, 0);
            if (stormRunnables.containsKey(p)) {
                stormRunnables.get(p).cancel();
            }
            Bar.setStormBarProgress(p, 0d);
            Bar.setStormBarVisibility(p, false);
        }
        stormRunnables.clear();
    }
}
