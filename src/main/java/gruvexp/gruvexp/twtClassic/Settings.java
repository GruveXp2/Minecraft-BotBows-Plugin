package gruvexp.gruvexp.twtClassic;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static gruvexp.gruvexp.twtClassic.BotBowsManager.PLAYERS;

public class Settings {
    public BotBowsMap currentMap = BotBowsMap.BLAUD_VS_SAUCE; // default map
    public final Map<Player, Integer> playerMaxHP = new HashMap<>();
    public int maxHP = 3;
    public boolean stormMode = false;
    public int stormFrequency = 2; // 5%, 10%, 25%, 50%, 100%
    public boolean dynamicScoring = true; // If true, når alle på et lag dør så gis et poeng for hvert liv som er igjen
    public int winThreshold = 5; // hvor mange poeng man skal spille til. Hvis den er satt til -1, så fortsetter det for alltid

    public void setDynamicScoring(boolean dynamic_scoring) {
        dynamicScoring = dynamic_scoring;
    }
    public void setPlayerMaxHP(int hp) {
        for (Player p : PLAYERS) {
            playerMaxHP.put(p, hp);
        }
    }
}
