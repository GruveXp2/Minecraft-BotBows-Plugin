package gruvexp.gruvexp.commands;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender; // grabs which player did the command. endrer datatype til Player
        if (BotBowsManager.activeGame) {
            p.sendMessage(ChatColor.RED + "Cant change settings, the game is already ongoing!");
        } else if (!BotBowsManager.isPlayerJoined(p)) {
            p.sendMessage(ChatColor.RED + "You have to join to access the settings");
        } else {
            Main.Id2Menu.get("botbows settings 1").open(p);
        }
        return true;
    }
}
