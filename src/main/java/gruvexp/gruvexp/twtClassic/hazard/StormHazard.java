package gruvexp.gruvexp.twtClassic.hazard;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.PlayerStormTimer;
import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import gruvexp.gruvexp.twtClassic.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashMap;

public class StormHazard extends Hazard{

    static HashMap<BotBowsPlayer, BossBar> bars = new HashMap<>(2);

    public StormHazard(Settings settings) {
        super(settings);
    }

    public void init() { // calles når spillet begynner
        if (getHazardChance() == HazardChance.DISABLED) return;
        for (BotBowsPlayer p : settings.getPlayers()) {
            BossBar bar = Bukkit.createBossBar(STR."\{ChatColor.AQUA}Lightning timer", BarColor.BLUE, BarStyle.SEGMENTED_6);
            bar.addPlayer(p.PLAYER);
            bar.setProgress(0d);
            bar.setVisible(false);
            bars.put(p, bar);
        }
    }

    @Override
    protected void trigger() {
        BotBows.messagePlayers(STR."\{ChatColor.DARK_RED}STORM INCOMING!\{ChatColor.RED} Seek shelter immediately!");
        BotBows.titlePlayers(STR."\{ChatColor.RED}STORM INCOMING", 80);
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            for (BotBowsPlayer p : settings.getPlayers()) {
                hazardTimers.put(p.PLAYER, new PlayerStormTimer(p.PLAYER, bars.get(p)).runTaskTimer(Main.getPlugin(), 0L, 2L));
            }
            Main.WORLD.setThundering(true);
            Main.WORLD.setStorm(true);
            Main.WORLD.setThunderDuration(12000); //10min
        }, 100L); // 5 sekunder
    }

    @Override
    public void end() {
        super.end();
        for (BossBar bossBar : bars.values()) { // resett storm baren og skjul den
            bossBar.setVisible(false);
            bossBar.setProgress(0d);
        }
        hazardTimers.clear();
        // resett været
        Main.WORLD.setThundering(false);
        Main.WORLD.setStorm(false);
        Main.WORLD.setClearWeatherDuration(12000);
    }
}
