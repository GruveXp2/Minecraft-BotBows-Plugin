package gruvexp.gruvexp.twtClassic.botbowsTeams;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BotBowsTeam {
    public final String NAME;
    public final ChatColor COLOR;
    public final DyeColor DYECOLOR;
    public final Location[] SPAWNPOS;
    public final Location TRIBUNE_POS;
    List<Player> players = new ArrayList<>(4);
    private int points;

    public BotBowsTeam(String name, ChatColor color, DyeColor dyeColor, Location[] spawnPos, Location tribunePos) {
        NAME = name;
        COLOR = color;
        DYECOLOR = dyeColor;
        SPAWNPOS = spawnPos;
        TRIBUNE_POS = tribunePos;
    }

    public void tpPlayersToSpawn() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).teleport(SPAWNPOS[i]);
        }
    }

    public void postTeamSwap() { // when the map is changed and the teams are swapped out
        for (Player p : players) {
            BotBowsManager.registerPlayerTeam(p, this);
            p.teleport(TRIBUNE_POS);
        }
    }

    public void join(Player p) {
        players.add(p);
        p.teleport(TRIBUNE_POS);
        BotBowsManager.registerPlayerTeam(p, this);
    }

    public void moveToTeam(Player p, BotBowsTeam newTeam) {
        players.remove(p);
        newTeam.join(p);
    }

    public void leave(Player p) {
        players.remove(p);
        BotBowsManager.unRegisterPlayerTeam(p);
    }

    public void reset() {
        players.clear();
        points = 0;
    }

    public int size() {return players.size();}

    public boolean hasPlayer(Player p) {
        return players.contains(p);
    }

    public Player getPlayer(int id) {return players.get(id);}

    public int getPlayerID(Player p) {return players.indexOf(p);}

    public List<Player> getPlayers() {return players;}

    public boolean isEmpty() {return players.isEmpty();}

    public Location getSpawnPos(Player p) {
        return SPAWNPOS[players.indexOf(p)];
    }

    public int getPoints() {return points;}
    public void addPoints(int score) {
        points += score;
        if (points > BotBowsManager.winThreshold) {
            points = BotBowsManager.winThreshold;
        }
    }

    public Material getGlassPane() {
        return Material.getMaterial(STR."\{DYECOLOR.name()}_STAINED_GLASS_PANE");
    }

    @Override
    public String toString() {
        return COLOR + NAME;
    }
}
