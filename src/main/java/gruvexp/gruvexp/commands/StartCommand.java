package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (BotBowsManager.activeGame) {
            p.sendMessage(ChatColor.RED + "The game has already started!");
        } else if (BotBowsManager.teamBlue.size() == 0 || BotBowsManager.teamRed.size() == 0) {
            p.sendMessage(ChatColor.RED + "Cant start game, both teams must have at least 1 player each");
        } else {
            BotBowsManager.startGame(p);
        }
        return true;
    }
}
