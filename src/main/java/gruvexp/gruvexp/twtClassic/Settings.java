package gruvexp.gruvexp.twtClassic;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.menus.HealthMenu;
import gruvexp.gruvexp.menu.menus.SelectTeamsMenu;
import gruvexp.gruvexp.twtClassic.botbowsTeams.*;
import gruvexp.gruvexp.twtClassic.hazard.StormHazard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Settings {
    public BotBowsMap currentMap = BotBowsMap.BLAUD_VS_SAUCE; // default map

    public StormHazard stormHazard = new StormHazard();

    public BotBowsTeam team1 = new TeamBlaud();
    public BotBowsTeam team2 = new TeamSauce();
    public final Set<BotBowsPlayer> PLAYERS = new HashSet<>(); // liste med alle players som er i gamet

    public boolean stormMode = false;
    public int stormFrequency = 2; // 5%, 10%, 25%, 50%, 100%
    public boolean dynamicScoring = true; // If true, når alle på et lag dør så gis et poeng for hvert liv som er igjen
    public int winThreshold = 5; // hvor mange poeng man skal spille til. Hvis den er satt til -1, så fortsetter det for alltid

    public Settings() {
        team1.setOppositeTeam(team2);
        team2.setOppositeTeam(team1);
    }

    public void setDynamicScoring(boolean dynamicScoring) {
        this.dynamicScoring = dynamicScoring;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
        for (BotBowsPlayer p : players) { // oppdaterer livene til alle playersene
            p.setMaxHP(maxHP);
        }
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMap(BotBowsMap map) {
        if (map == currentMap) return;
        currentMap = map;
        switch (map) {
            case BLAUD_VS_SAUCE -> setNewTeams(new TeamBlaud(team1), new TeamSauce(team2));
            case GRAUT_VS_WACKY -> setNewTeams(new TeamGraut(team1), new TeamWacky(team2));
        }
    }

    private void setNewTeams(BotBowsTeam newTeam1, BotBowsTeam newTeam2) {
        team1 = newTeam1;
        team2 = newTeam2;
        BotBows.teamsMenu.setColoredGlassPanes(); // update the glass pane items that show the team colors and name
        BotBows.teamsMenu.recalculateTeam(); // update the player heads so they have the correct color
        BotBows.healthMenu.updateMenu(); // update so the name colors match the new team color
    }

    public void joinGame(Player p) {
        BotBowsPlayer bp = BotBows.getBotBowsPlayer(p);
        if (bp == null) {
            bp = new BotBowsPlayer(p, this);
            BotBows.registerBotBowsPlayer(bp);
        } else if (players.contains(bp)) {
            p.sendMessage(STR."\{ChatColor.RED}You already joined!");
            return;
        }
        players.add(bp);
        if (team1.size() <= team2.size()) { // players fordeles jevnt i lagene
            team1.join(bp);
        } else {
            team2.join(bp);
        }
        BotBows.teamsMenu.recalculateTeam();
        BotBows.healthMenu.updateMenu();
        for (Player q : Bukkit.getOnlinePlayers()) {
            q.sendMessage(STR."\{p.getPlayerListName()} has joined BotBows Classic! (\{players.size()})");
        }
    }

    public void leaveGame(BotBowsPlayer p) {
        if (!players.contains(p)) {
            p.PLAYER.sendMessage(STR."\{ChatColor.RED}You cant leave when you're not in a game");
            return;
        }
        p.leaveGame();
        players.remove(p);
        BotBows.teamsMenu.recalculateTeam();
        BotBows.healthMenu.updateMenu();
    }

    public boolean isPlayerJoined(Player p) {
        return PLAYERS.contains(p);
    }
}
