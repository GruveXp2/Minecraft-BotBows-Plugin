package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.twtClassic.BotBows;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (BotBows.activeGame) {
            sender.sendMessage(STR."\{ChatColor.RED}The game has already started!");
        } else if (BotBows.team1.isEmpty() || BotBows.team2.isEmpty()) {
            sender.sendMessage(STR."\{ChatColor.RED}Cant start game, both teams must have at least 1 player each");
        } else {
            BotBows.startGame((Player) sender);
        }
        return true;
    }
}
