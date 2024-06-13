package gruvexp.gruvexp.listeners;

import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwitchSpectator implements Listener {

    private static void spectateNext(Player p, boolean own_team) {
        ArrayList<Player> team = own_team? BotBowsManager.getTeam(p) : BotBowsManager.getOpponentTeam(p);
        String team_str = own_team? "own team" : "opponent team";
        List<Player> alive_players = team.stream() // lager liste med alle de levende playersene
                .filter(q -> q.getGameMode() == GameMode.ADVENTURE)
                .collect(Collectors.toList());

        if (alive_players.size() == 0) {
            p.sendMessage(ChatColor.GRAY + "Cant spectate, " + team_str + " has no alive players");
            return;
        }

        if (p.getSpectatorTarget() == null) {
            p.setSpectatorTarget(alive_players.get(0));
            //p.sendMessage(ChatColor.GRAY + "Currently spectating no players, spectating " + alive_players.get(0).getPlayerListName() + "(first player in " + team_str + ")");
            return;
        }

        if (team.contains(p.getSpectatorTarget())) {
            int i = alive_players.indexOf((Player) p.getSpectatorTarget());
            if (i == alive_players.size() - 1) {
                i = -1;
            }
            p.setSpectatorTarget(alive_players.get(i + 1)); // spectater den neste playeren
            //p.sendMessage(ChatColor.GRAY + "Already spectating someone from " + team_str + " (" + alive_players.get(alive_players.indexOf((Player) p.getSpectatorTarget())).getPlayerListName() + "), spectating " + alive_players.get(i + 1).getPlayerListName() + "the next player if possible");
        } else {
            p.setSpectatorTarget(alive_players.get(0)); // spectater den første player i enemy team
            //p.sendMessage(ChatColor.GRAY + "Switching to " + team_str + " and spectating " + alive_players.get(0).getPlayerListName() + " (the first player)");
        }
    }

    @EventHandler()
    public void onMouseClick(PlayerInteractEvent e) {
        if (!BotBowsManager.activeGame) return;
        Player p = e.getPlayer();
        if (!BotBowsManager.isPlayerJoined(p)) return;
        if (p.getGameMode() != GameMode.SPECTATOR) return;
        Action a = e.getAction();

        //p.sendMessage(ChatColor.GRAY + "You clicked a button: ");

        if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
            //p.sendMessage(ChatColor.GRAY + "left clicked air or block");
            spectateNext(p, true);
        } else if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            //p.sendMessage(ChatColor.GRAY + "right clicked air or block");
            spectateNext(p, false);
        }
    }

}
