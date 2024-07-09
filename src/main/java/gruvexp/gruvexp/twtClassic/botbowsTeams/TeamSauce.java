package gruvexp.gruvexp.twtClassic.botbowsTeams;

import gruvexp.gruvexp.Main;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;

public class TeamSauce extends BotBowsTeam {

    public TeamSauce() {
        Location[] spawnPos = {
                new Location(Main.WORLD, -268.5, 22.0, -164.5, -90, 10),
                new Location(Main.WORLD, -268.5, 22.0, -167.5, -90, 10),
                new Location(Main.WORLD, -270.5, 22.0, -162.5, -90, 10),
                new Location(Main.WORLD, -270.5, 22.0, -169.5, -90, 10),
                new Location(Main.WORLD, -271.5, 22.0, -166.0, -90, 10)
        };
        super("Sauce", ChatColor.RED, DyeColor.RED, spawnPos);
    }

    public TeamSauce(BotBowsTeam otherTeam) {
        this();
        players = otherTeam.players;
        tpPlayersToSpawn();
    }
}
