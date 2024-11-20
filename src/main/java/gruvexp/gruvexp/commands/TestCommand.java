package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsMap;
import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    public static boolean rotation = true;
    public static boolean log = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;
        if (args.length == 1) {
            switch (args[0]) {
                case "t" ->
                        BotBows.debugMessage(STR."The team of \{p.getName()} is \{BotBows.getBotBowsPlayer(p).getTeam()}");
                case "w" -> {
                    BotBows.joinGame(Bukkit.getPlayer("GruveXp"));
                    BotBows.joinGame(Bukkit.getPlayer("Spionagent54"));
                    BotBows.settings.setMap(BotBowsMap.GRAUT_VS_WACKY);
                    BotBows.settings.setWinThreshold(-1);
                    BotBows.healthMenu.enableCustomHP();
                    BotBowsPlayer judith = BotBows.getBotBowsPlayer(Bukkit.getPlayer("Spionagent54"));

                    judith.setMaxHP(20);
                    Bukkit.dispatchCommand(Bukkit.getPlayer("GruveXp"), "botbows:start");  // tester om dungeonen funker
                }
                case "a" -> {
                    rotation = !rotation;
                    BotBows.debugMessage(STR."New location logic set to: \{rotation}");
                }
                case "b" -> {
                    log = !log;
                    BotBows.debugMessage(STR."Logging set to: \{log}");
                }
                case null, default -> BotBows.debugMessage(STR."Wrong arg");
            }
            return true;
        }
        //BotBowsManager.debugMessage(STR."\{p.getName()}is \{isInDungeon(p) ? "" : "not"} in a dungeon\{BotBowsManager.isInDungeon(p) ? STR.", section\{BotBowsManager.getSection(p)}" : ""}");
        return true;
    }
}
