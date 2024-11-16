package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.menus.SelectTeamsMenu;
import gruvexp.gruvexp.twtClassic.BotBows;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;
        if (BotBows.activeGame) {
            p.sendMessage(STR."\{ChatColor.RED}Cant leave, the game has already started!");
            return true;
        }
        BotBows.leaveGame(p);
        SelectTeamsMenu selectTeamsMenu = (SelectTeamsMenu) Main.menus.get("select teams");
        selectTeamsMenu.recalculateTeam(); // recalculate
        p.setGameMode(GameMode.SPECTATOR);
        return true;
    }
}
