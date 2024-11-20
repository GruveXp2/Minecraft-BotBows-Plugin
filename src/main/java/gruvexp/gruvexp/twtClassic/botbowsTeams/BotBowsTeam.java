package gruvexp.gruvexp.twtClassic.botbowsTeams;

import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public abstract class BotBowsTeam {
    public final String NAME;
    public final ChatColor COLOR;
    public final DyeColor DYECOLOR;
    public final Location[] SPAWNPOS;
    public final Location TRIBUNE_POS;
    private BotBowsTeam oppositeTeam;
    List<BotBowsPlayer> players = new ArrayList<>(4);
    private int points;

    public BotBowsTeam(String name, ChatColor color, DyeColor dyeColor, Location[] spawnPos, Location tribunePos) {
        NAME = name;
        COLOR = color;
        DYECOLOR = dyeColor;
        SPAWNPOS = spawnPos;
        TRIBUNE_POS = tribunePos;
    }

    public BotBowsTeam getOppositeTeam() {return oppositeTeam;}

    public void setOppositeTeam(BotBowsTeam oppositeTeam) {
        if (this.oppositeTeam != null) {
            throw new IllegalStateException("This team already has an assigned opposite team");
        }
        this.oppositeTeam = oppositeTeam;
    }

    public void tpPlayersToSpawn() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).PLAYER.teleport(SPAWNPOS[i]);
        }
    }

    public void postTeamSwap() { // when the map is changed and the teams are swapped out
        for (BotBowsPlayer p : players) {
            p.PLAYER.teleport(TRIBUNE_POS);
        }
    }

    public void join(BotBowsPlayer p) {
        players.add(p);
        p.PLAYER.teleport(TRIBUNE_POS);
        p.joinTeam(this);
    }

    public void moveToTeam(BotBowsPlayer p, BotBowsTeam newTeam) {
        players.remove(p);
        newTeam.join(p);
    }

    public void leave(BotBowsPlayer p) {
        players.remove(p);
        p.leaveTeam();
    }

    public void reset() {
        points = 0;
    }

    public int size() {return players.size();}

    public boolean hasPlayer(BotBowsPlayer p) {
        return players.contains(p);
    }

    public BotBowsPlayer getPlayer(int id) {return players.get(id);}

    public int getPlayerID(BotBowsPlayer p) {return players.indexOf(p);}

    public List<BotBowsPlayer> getPlayers() {return players;}

    public boolean isEmpty() {return players.isEmpty();}

    public Location getSpawnPos(BotBowsPlayer p) {
        return SPAWNPOS[players.indexOf(p)];
    }

    public int getPoints() {return points;}
    public void addPoints(int score) {
        points += score;
    }

    public Material getGlassPane() {
        return Material.getMaterial(STR."\{DYECOLOR.name()}_STAINED_GLASS_PANE");
    }

    @Override
    public String toString() {
        return COLOR + NAME;
    }
}
