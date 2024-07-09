package gruvexp.gruvexp.twtClassic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Bar {

    static HashMap<Player, BossBar> sneakBars = new HashMap<>(2);
    static HashMap<Player, BossBar> stormBars = new HashMap<>(2);

    public static void sneakBarInit() {
        for (Player p : BotBowsManager.getPlayers()) {
            BossBar bar = Bukkit.createBossBar(STR."\{ChatColor.YELLOW}Sneaking cooldown", BarColor.YELLOW, BarStyle.SEGMENTED_10);
            bar.addPlayer(p);
            bar.setProgress(0d);
            bar.setVisible(false);
            sneakBars.put(p, bar);
        }
    }

    public static void stormBarInit() {
        for (Player p : BotBowsManager.getPlayers()) {
            BossBar bar = Bukkit.createBossBar(STR."\{ChatColor.AQUA}Lightning timer", BarColor.BLUE, BarStyle.SEGMENTED_6);
            bar.addPlayer(p);
            bar.setProgress(0d);
            bar.setVisible(false);
            stormBars.put(p, bar);
        }
    }

    public static void setSneakBarColor(Player p, ChatColor chatColor, BarColor barColor) {
        sneakBars.get(p).setTitle(chatColor + sneakBars.get(p).getTitle().strip());
        sneakBars.get(p).setColor(barColor);
    }

    public static void setSneakBarVisibility(Player p, boolean bool) {
        sneakBars.get(p).setVisible(bool);
    }


    public static void setStormBarVisibility(Player p, boolean bool) {
        stormBars.get(p).setVisible(bool);
    }

    public static void setSneakBarProgress(Player p, double progress) {
        sneakBars.get(p).setProgress(progress);
    }


    public static void setStormBarProgress(Player p, double progress) {
        stormBars.get(p).setProgress(progress);
    }


}
