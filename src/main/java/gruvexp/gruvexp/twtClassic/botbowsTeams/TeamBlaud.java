package gruvexp.gruvexp.twtClassic.botbowsTeams;

import gruvexp.gruvexp.Main;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;

public class TeamBlaud extends BotBowsTeam {

    public TeamBlaud() {
        Location[] spawnPos = {
                new Location(Main.WORLD, -215.5, 22.0, -167.5, 90, 10),
                new Location(Main.WORLD, -215.5, 22.0, -164.5, 90, 10),
                new Location(Main.WORLD, -213.5, 22.0, -169.5, 90, 10),
                new Location(Main.WORLD, -213.5, 22.0, -162.5, 90, 10),
                new Location(Main.WORLD, -212.5, 22.0, -166.0, 90, 10)
        };
        super("Blaud", ChatColor.BLUE, DyeColor.BLUE, spawnPos);
    }

    public TeamBlaud(BotBowsTeam otherTeam) {
        this();
        players = otherTeam.players;
        tpPlayersToSpawn();
    }

}
