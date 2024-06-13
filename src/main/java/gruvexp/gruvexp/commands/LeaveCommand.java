package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;
        if (BotBowsManager.activeGame) {
            p.sendMessage(ChatColor.RED + "Cant leave, the game has already started!");
            return true;
        }
        BotBowsManager.leaveGame(p);
        Main.menus.get("botbows settings 1").callInternalFunction(0); // recalculate

        return true;
    }
}
