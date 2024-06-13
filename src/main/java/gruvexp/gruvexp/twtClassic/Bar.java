package gruvexp.gruvexp.twtClassic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Bar {

    static HashMap<Player, BossBar> Player2ShiftBar = new HashMap<>(2);
    static HashMap<Player, BossBar> Player2StormBar = new HashMap<>(2);

    public static void sneakBarInit() {
        for (Player p : BotBowsManager.getPlayers()) {
            BossBar bar = Bukkit.createBossBar(ChatColor.YELLOW + "Sneaking cooldown", BarColor.YELLOW, BarStyle.SEGMENTED_10);
            bar.addPlayer(p);
            bar.setProgress(0d);
            bar.setVisible(false);
            Player2ShiftBar.put(p, bar);
        }
    }

    public static void stormBarInit() {
        for (Player p : BotBowsManager.getPlayers()) {
            BossBar bar = Bukkit.createBossBar(ChatColor.AQUA + "Lightning timer", BarColor.BLUE, BarStyle.SEGMENTED_6);
            bar.addPlayer(p);
            bar.setProgress(0d);
            bar.setVisible(false);
            Player2StormBar.put(p, bar);
        }
    }

    public static void setSneakBarColor(Player p, ChatColor chatColor, BarColor barColor) {
        Player2ShiftBar.get(p).setTitle(chatColor + Player2ShiftBar.get(p).getTitle().strip());
        Player2ShiftBar.get(p).setColor(barColor);
    }

    public static void setSneakBarVisibility(Player p, boolean bool) {
        Player2ShiftBar.get(p).setVisible(bool);
    }


    public static void setStormBarVisibility(Player p, boolean bool) {
        Player2StormBar.get(p).setVisible(bool);
    }

    public static void setSneakBarProgress(Player p, double progress) {
        Player2ShiftBar.get(p).setProgress(progress);
    }


    public static void setStormBarProgress(Player p, double progress) {
        Player2StormBar.get(p).setProgress(progress);
    }


}
