package gruvexp.gruvexp.twtClassic;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class Cooldowns { // Inneholder shifting og storm logic

    public static Map<Player, BukkitTask> sneakRunnables = new HashMap<>();
    public static Map<Player, Integer> sneakCooldowns = new HashMap<>();

    public static void stormRunnableInit() {
        for (Player p : BotBowsManager.getPlayers()) {
            stormRunnables.put(p, new StormCooldown(p).runTaskTimer(Main.getPlugin(), 0L, 2L));
        }
    }

}
