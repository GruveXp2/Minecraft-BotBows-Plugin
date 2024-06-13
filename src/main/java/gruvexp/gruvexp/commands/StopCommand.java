package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        if (BotBowsManager.activeGame) {
            BotBowsManager.messagePlayers("The game was ended by " + p.getPlayerListName());
            BotBowsManager.endGame();
        } else {
            p.sendMessage(ChatColor.RED + "The game hast even started!");
        }
        return true;
    }
}
