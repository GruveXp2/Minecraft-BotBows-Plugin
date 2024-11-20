package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.twtClassic.BotBows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return false;
        }
        BotBows.gameMenu.open(p);
        return true;
    }
}
