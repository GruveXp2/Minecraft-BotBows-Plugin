package gruvexp.gruvexp.twtClassic;

import gruvexp.gruvexp.twtClassic.botbowsGames.BotBowsGame;
import gruvexp.gruvexp.twtClassic.botbowsGames.GrautWackyGame;
import gruvexp.gruvexp.twtClassic.botbowsTeams.BotBowsTeam;
import gruvexp.gruvexp.twtClassic.botbowsTeams.TeamBlaud;
import gruvexp.gruvexp.twtClassic.botbowsTeams.TeamSauce;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class BotBows {

    public static final ItemStack BOTBOW = getBotBow();
    public static Settings settings;
    public static BotBowsGame botBowsGame;
    private static final HashMap<Player, BotBowsPlayer> PLAYERS = new HashMap<>(); // liste med alle players som er i gamet
    public static boolean activeGame = false; // hvis spillet har starta, så kan man ikke gjøre ting som /settings

    public static GameMenu gameMenu;
    public static MapMenu mapMenu;
    public static HealthMenu healthMenu;
    public static TeamsMenu teamsMenu;
    public static WinThresholdMenu winThresholdMenu;
    public static HazardMenu hazardMenu;

    public static void init() { // a
        settings = new Settings();
        BotBows.debugMessage("settings is not null,i fosmeone says its null their lyign");
        gameMenu = new GameMenu();
        mapMenu = new MapMenu();
        BotBows.debugMessage("healthmenu init");
        healthMenu = new HealthMenu();
        BotBows.debugMessage("teamsmenu init");
        teamsMenu = new TeamsMenu();
        BotBows.debugMessage("yee ig");
        winThresholdMenu = new WinThresholdMenu();
        hazardMenu = new HazardMenu();
    }

    public static void joinGame(Player p) {
        if (activeGame) {
            p.sendMessage(STR."\{ChatColor.RED}A game is already ongoing, wait until it ends before you join");
            return;
        }
        settings.joinGame(p);
    }

    public static void leaveGame(Player p) {
        BotBowsPlayer bp = getBotBowsPlayer(p);
        if (!settings.isPlayerJoined(p)) {
            p.sendMessage("Nothing happened, you werent in the game in the first place");
            return;
        }
        if (activeGame) {
            botBowsGame.leaveGame(bp);
        } else {
            settings.leaveGame(bp);
        }
    }

    public static void startGame(Player gameStarter) {
        if (activeGame) {
            gameStarter.sendMessage(STR."\{ChatColor.RED}The game has already started!");
            return;
        } else if (settings.team1.isEmpty() || settings.team2.isEmpty()) {
            gameStarter.sendMessage(STR."\{ChatColor.RED}Cant start game, both teams must have at least 1 player each");
            return;
        }
        if (settings.currentMap == BotBowsMap.GRAUT_VS_WACKY) {
            botBowsGame = new GrautWackyGame(settings);
        } else {
            botBowsGame = new BotBowsGame(settings);
        }
        botBowsGame.startGame(gameStarter);
    }

    private static ItemStack getBotBow() {
        ItemStack BOTBOW = new ItemStack(Material.CROSSBOW);
        CrossbowMeta meta = (CrossbowMeta) BOTBOW.getItemMeta();
        meta.setDisplayName(STR."\{ChatColor.BOLD}\{ChatColor.GREEN}BotBow");
        meta.setLore(Arrays.asList("The strongest bow", "ever known to man"));
        meta.addEnchant(Enchantment.POWER, 10, true);
        meta.addEnchant(Enchantment.PUNCH, 10, true);
        meta.addChargedProjectile(new ItemStack(Material.ARROW));
        Damageable damageable = (Damageable) meta;
        damageable.setDamage((short) 464);
        BOTBOW.setItemMeta(damageable);
        return BOTBOW;
    }

    public static BotBowsPlayer getBotBowsPlayer(Player p) {
        return PLAYERS.get(p);
    }

    public static void registerBotBowsPlayer(BotBowsPlayer p) {
        if (PLAYERS.containsKey(p.PLAYER)) return;
        PLAYERS.put(p.PLAYER, p);
    }

    public static void check4Victory(BotBowsPlayer dedPlayer) {
        botBowsGame.check4Victory(dedPlayer);
    }

    public static void messagePlayers(String message) {
        for (BotBowsPlayer p : settings.getPlayers()) {
            p.PLAYER.sendMessage(message);
        }
    }

    public static void debugMessage(String message) {
        messagePlayers(STR."\{ChatColor.GRAY}[DEBUG]: \{message}");
    }

    public static void debugMessage(String message, boolean bool) {
        if (bool) debugMessage(message);
    }

    public static void titlePlayers(String title, int duration) {
        for (BotBowsPlayer p : settings.getPlayers()) {
            p.PLAYER.sendTitle(title, null, 2, duration, 5);
        }
    }

    public static int getTotalPlayers() {
        return settings.getPlayers().size();
    }
    public static Collection<BotBowsPlayer> getPlayers() {
        return PLAYERS.values();
    }
}