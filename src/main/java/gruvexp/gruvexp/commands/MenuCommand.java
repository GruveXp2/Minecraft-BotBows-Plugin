package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender; // grabs which player did the command. endrer datatype til Player

        Main.Id2Menu.get("game menu").open(p);

        return true;
    }
}
