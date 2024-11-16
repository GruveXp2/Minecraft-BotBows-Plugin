package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.menus.HealthMenu;
import gruvexp.gruvexp.menu.menus.SelectTeamsMenu;
import gruvexp.gruvexp.menu.menus.WinThresholdMenu;
import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsMap;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TestCommand implements CommandExecutor {

    public static boolean rotation = true;
    public static boolean log = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;
        if (args.length == 1) {
            if (Objects.equals(args[0], "t")) {
                BotBows.debugMessage(STR."The team of \{p.getName()} is \{BotBows.getBotBowsPlayer(p).getTeam()}");
            } else if (Objects.equals(args[0], "w")) { // tester om dungeonen funker
                BotBows.joinGame(Bukkit.getPlayer("GruveXp"));
                BotBows.joinGame(Bukkit.getPlayer("Spionagent54"));
                BotBows.settings.setMap(BotBowsMap.GRAUT_VS_WACKY);
                BotBows.settings.winThreshold = -1;
                ((WinThresholdMenu) Main.menus.get("win threshold")).updateMenu();
                ((SelectTeamsMenu) Main.menus.get("select teams")).judithBytterLag();
                ((HealthMenu) Main.menus.get("health")).enableCustomHP();
                Player judith = Bukkit.getPlayer("Spionagent54");

                judith.setMaxHP(20);
                Bukkit.dispatchCommand(Bukkit.getPlayer("GruveXp"), "botbows:start");
            } else if (Objects.equals(args[0], "a")) {
                rotation = !rotation;
                BotBows.debugMessage(STR."New location logic set to: \{rotation}");
            } else if (Objects.equals(args[0], "b")) {
                log = !log;
                BotBows.debugMessage(STR."Logging set to: \{log}");
            } else {
                BotBows.debugMessage(STR."Wrong arg");
            }
            return true;
        }
        //BotBowsManager.debugMessage(STR."\{p.getName()}is \{isInDungeon(p) ? "" : "not"} in a dungeon\{BotBowsManager.isInDungeon(p) ? STR.", section\{BotBowsManager.getSection(p)}" : ""}");
        return true;
    }
}
