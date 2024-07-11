package gruvexp.gruvexp.twtClassic.botbowsTeams;

import gruvexp.gruvexp.Main;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;

public class TeamGraut extends BotBowsTeam {

    public TeamGraut() {
        Location[] spawnPos = {
                new Location(Main.WORLD, -268.5, 22.0, -274.5, -90, 10),
                new Location(Main.WORLD, -268.5, 22.0, -277.5, -90, 10),
                new Location(Main.WORLD, -270.5, 22.0, -272.5, -90, 10),
                new Location(Main.WORLD, -270.5, 22.0, -279.5, -90, 10),
                new Location(Main.WORLD, -271.5, 22.0, -276.0, -90, 10)
        };
        super("Graut", ChatColor.LIGHT_PURPLE, DyeColor.PURPLE, spawnPos);
    }

    public TeamGraut(BotBowsTeam otherTeam) {
        this();
        players = otherTeam.players;
        tpPlayersToSpawn();
    }

}
