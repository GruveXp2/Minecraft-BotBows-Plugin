package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (BotBowsManager.activeGame) {
            sender.sendMessage(STR."\{ChatColor.RED}The game has already started!");
        } else if (BotBowsManager.team1.isEmpty() || BotBowsManager.team2.isEmpty()) {
            sender.sendMessage(STR."\{ChatColor.RED}Cant start game, both teams must have at least 1 player each");
        } else {
            BotBowsManager.startGame(sender);
        }
        return true;
    }
}
