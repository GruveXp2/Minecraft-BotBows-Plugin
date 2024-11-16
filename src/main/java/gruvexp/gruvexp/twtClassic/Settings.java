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
        for (BotBowsPlayer p : PLAYERS) { // oppdaterer livene til alle playersene
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
            case BLAUD_VS_SAUCE -> {
                team1 = new TeamBlaud(team1);
                team2 = new TeamSauce(team2);
            }
            case GRAUT_VS_WACKY -> {
                team1 = new TeamGraut(team1);
                team2 = new TeamWacky(team2);
            }
        }
        ((SelectTeamsMenu) Main.menus.get("select teams")).setColoredGlassPanes(); // update the glass pane items that show the team colors and name
        ((SelectTeamsMenu) Main.menus.get("select teams")).recalculateTeam(); // update the player heads so they have the correct color
        ((HealthMenu) Main.menus.get("health")).updateMenu(); // update so the name colors match the new team color
    }

    public void joinGame(Player p) {
        if (!PLAYERS.contains(p)) {
            BotBowsPlayer botBowsP = BotBows.getBotBowsPlayer(p);
            if (botBowsP == null) {
                botBowsP = new BotBowsPlayer(p, this);
                BotBows.registerBotBowsPlayer(botBowsP);
            }
            PLAYERS.add(botBowsP);
            team1.join(botBowsP);
            if (PLAYERS.size() > 1) {
                BotBows.teamsMenu.recalculateTeam();
                BotBows.healthMenu.updateMenu();
            }
            for (Player q : Bukkit.getOnlinePlayers()) {
                q.sendMessage(STR."\{p.getPlayerListName()} has joined BotBows Classic! (\{PLAYERS.size()})");
            }
        }
        else {
            p.sendMessage(STR."\{ChatColor.RED}You already joined!");
        }
    }

    public void leaveGame(BotBowsPlayer p) {
        if (!PLAYERS.contains(p)) {
            p.PLAYER.sendMessage(STR."\{ChatColor.RED}You cant leave when you're not in a game");
            return;
        }
        ((SelectTeamsMenu) Main.menus.get("select teams")).recalculateTeam();
        ((HealthMenu) Main.menus.get("health")).updateMenu();
        p.leaveGame();
        BotBows.messagePlayers(STR."\{ChatColor.YELLOW}\{p.PLAYER.getPlayerListName()} has left the game (\{PLAYERS.size()})");
        PLAYERS.remove(p);
    }

    public boolean isPlayerJoined(Player p) {
        return PLAYERS.contains(p);
    }
}
