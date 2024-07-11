package gruvexp.gruvexp.twtClassic.botbowsTeams;

import gruvexp.gruvexp.Main;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;

public class TeamWacky extends BotBowsTeam {

    public TeamWacky() {
        Location[] spawnPos = {
                new Location(Main.WORLD, -215.5, 22.0, -277.5, 90, 10),
                new Location(Main.WORLD, -215.5, 22.0, -274.5, 90, 10),
                new Location(Main.WORLD, -213.5, 22.0, -279.5, 90, 10),
                new Location(Main.WORLD, -213.5, 22.0, -272.5, 90, 10),
                new Location(Main.WORLD, -212.5, 22.0, -276.0, 90, 10)
        };
        super("Wacky", ChatColor.GREEN, DyeColor.LIME, spawnPos);
    }

    public TeamWacky(BotBowsTeam otherTeam) {
        this();
        players = otherTeam.players;
        tpPlayersToSpawn();
    }
}
