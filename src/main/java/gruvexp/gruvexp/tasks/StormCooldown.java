package gruvexp.gruvexp.tasks;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.twtClassic.Bar;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import gruvexp.gruvexp.twtClassic.Cooldowns;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StormCooldown extends BukkitRunnable {

    Player p;
    int time = 0;
    public StormCooldown(Player p) {
        this.p = p;
    }

    private boolean isPlayerExposed(Player p) {
        if (p.getLocation().getY() < 22) {return false;}
        if (p.getLocation().getY() >= 29) {return true;}

        int x = (int) Math.floor(p.getLocation().getX());
        int y = (int) Math.floor(p.getLocation().getY());
        int z = (int) Math.floor(p.getLocation().getZ());
        //p.sendMessage(ChatColor.GRAY + "======== [DEBUG] ========\np_coord = " + p.getLocation().getX() + ", " + p.getLocation().getY() + ", " + p.getLocation().getZ());
        for (int i = y + 2; i < 31; i++) {
            //p.sendMessage(ChatColor.GRAY + "" + x + ", " + y + ", " + z + " : " + world.getBlockAt(x, y, z).getType());
            if (Main.WORLD.getBlockAt(x, i, z).getType() != Material.AIR) {return false;}
        }
        return true;
    }

    @Override
    public void run() {
        if (isPlayerExposed(p)) {
            if (time < 240) {
                time += 4;
                Cooldowns.Player2StormCoolDown.put(p, time);
                if (time >= 240) {
                    Bar.setStormBarProgress(p, 1.0d);
                } else {
                    Bar.setStormBarProgress(p, time/240d);
                }
                if (time == 4) {
                    Bar.setStormBarVisibility(p, true);
                }
            } else {
                Main.WORLD.strikeLightningEffect(p.getLocation());
                time = 0;
                Bar.setStormBarProgress(p, 0);
                BotBowsManager.Player2HP.put(p, 0);
                p.damage(0.5);
                p.setGameMode(GameMode.SPECTATOR);
                BotBowsManager.messagePlayers(BotBowsManager.getTeamColor(p) + p.getPlayerListName() + ChatColor.AQUA + " was electrocuted to a crisp!");
                BotBowsManager.check4Victory(p);
            }
        } else {
            if (time > 0) {
                time -= 1;
                Cooldowns.Player2StormCoolDown.put(p, time);
                Bar.setStormBarProgress(p, time/240d);
                if (time == 0) {
                    Bar.setStormBarVisibility(p, false);
                }
            }
        }
    }
}
