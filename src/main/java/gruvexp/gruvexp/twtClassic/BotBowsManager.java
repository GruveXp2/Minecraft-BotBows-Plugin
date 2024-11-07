package gruvexp.gruvexp.twtClassic;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.menus.HealthMenu;
import gruvexp.gruvexp.menu.menus.SelectTeamsMenu;
import gruvexp.gruvexp.tasks.BotBowsGiver;
import gruvexp.gruvexp.tasks.GvwDungeonProximityScanner;
import gruvexp.gruvexp.tasks.RoundCountdown;
import gruvexp.gruvexp.tasks.StartStorm;
import gruvexp.gruvexp.twtClassic.botbowsTeams.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

import static gruvexp.gruvexp.twtClassic.Bar.sneakBarInit;

public class BotBowsManager {

    private static final Main PLUGIN = Main.getPlugin();
    public static BotBowsTeam team1 = new TeamBlaud();
    public static BotBowsTeam team2 = new TeamSauce();
    public static Settings settings = new Settings();
    private static final Map<Player, BotBowsTeam> PLAYER_TEAM = new HashMap<>();
    public static final List<Player> PLAYERS = new ArrayList<>(); // liste med alle players som er i gamet
    private static final List<List<List<Integer>>> PLAYER_HEALTH_ARMOR = new ArrayList<>(); // Når man tar damag så kan man gette em liste med hvilke armor pieces som skal fjernes
    private static final List<Double> STORM_FREQUENCY = new ArrayList<>(List.of(0.05, 0.1, 0.25, 0.5, 1.0));
    public static final Map<Player, Integer> playerHP = new HashMap<>();
    public static final Map<Player, Boolean> isDamaged = new HashMap<>();
    private static boolean activeStorm = false;
    private static int round = 0; // hvilken runde man er på
    public static boolean activeGame = false; // hvis spillet har starta, så kan man ikke gjøre ting som /settings
    public static boolean canMove = true;
    // only for graut vs wacky
    private static final Map<Player, GvwDungeonProximityScanner> dungeonScanners = new HashMap<>();
    private static final Map<Player, DungeonGhoster> dungeonGhosters = new HashMap<>();


    public static ItemStack getBotBow() {
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

    public static void setMap(BotBowsMap map) {
        if (map == settings.currentMap) return;
        settings.currentMap = map;
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

    public static void armorInit() { // definerer hvilke armor som skal mistes når playeren har x antall liv. 0=boots, 1=leggings, 2=chestplate, 3=helmet
        List<List<Integer>> hp2 = new ArrayList<>();
        List<List<Integer>> hp3 = new ArrayList<>();
        List<List<Integer>> hp4 = new ArrayList<>();
        List<List<Integer>> hp5 = new ArrayList<>();
        hp2.add(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
        hp3.add(new ArrayList<>(Arrays.asList(0, 2)));
        hp3.add(new ArrayList<>(Arrays.asList(1, 3)));
        hp4.add(new ArrayList<>(List.of(2)));
        hp4.add(new ArrayList<>(Arrays.asList(0, 1)));
        hp4.add(new ArrayList<>(List.of(3)));
        hp5.add(new ArrayList<>(List.of(2)));
        hp5.add(new ArrayList<>(List.of(1)));
        hp5.add(new ArrayList<>(List.of(0)));
        hp5.add(new ArrayList<>(List.of(3)));
        hp5.add(new ArrayList<>()); // hvis man har fler liv enn 5 så blir denne calla, men da skal det ikke skje noe
        PLAYER_HEALTH_ARMOR.add(hp2);
        PLAYER_HEALTH_ARMOR.add(hp3);
        PLAYER_HEALTH_ARMOR.add(hp4);
        PLAYER_HEALTH_ARMOR.add(hp5);
    }

    public static void joinGame(Player p) {
        if (!PLAYERS.contains(p)) {
            if (activeGame) {
                p.sendMessage(STR."\{ChatColor.RED}A game is already ongoing, wait until it ends before you join");
                return;
            }
            PLAYERS.add(p);
            team1.join(p);
            if (PLAYERS.size() > 1) {
                settings.playerMaxHP.put(p, settings.maxHP);
                ((SelectTeamsMenu) Main.menus.get("select teams")).recalculateTeam();
                ((HealthMenu) Main.menus.get("health")).updateMenu();
            } else {
                settings.playerMaxHP.put(p, 3);
            }
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(settings.playerMaxHP.get(p) * 2);
            p.setHealth(settings.playerMaxHP.get(p) * 2);
            for (Player q : PLAYERS) {
                q.sendMessage(STR."\{p.getPlayerListName()} has joined BotBows Classic! (\{PLAYERS.size()})");
            }
            p.setGameMode(GameMode.ADVENTURE);
        }
        else {
            p.sendMessage(STR."\{ChatColor.RED}You already joined!");
        }
    }

    public static void registerPlayerTeam(Player p, BotBowsTeam team) {
        PLAYER_TEAM.put(p, team);
    }

    public static void unRegisterPlayerTeam(Player p) {
        PLAYER_TEAM.remove(p);
    }

    public static BotBowsTeam getOppositeTeam(BotBowsTeam team) {
        return team == team1 ? team2 : team1;
    }

    public static void startGame(CommandSender sender) {
        activeGame = true;
        messagePlayers(STR."\{ChatColor.GRAY}\{sender.getName()}: \{ChatColor.GREEN}The game has started!");
        sneakBarInit();
        Cooldowns.CoolDownInit(PLAYERS);
        Board.createBoard();
        startRound();
        if (settings.stormMode) {
            Bar.stormBarInit();
        }

        // legger til player liv osv
        for (Player q : PLAYERS) {
            isDamaged.put(q, false);
            Board.updatePlayerScore(q);
        }
        Board.updateTeamScores();
        new BotBowsGiver().runTaskTimer(PLUGIN, 100L, 10L);
        if (settings.currentMap == BotBowsMap.GRAUT_VS_WACKY) {
            initDungeon();
        }
    }

    public static void startRound() {
        if (activeStorm) {
            Cooldowns.resetStormRunnable();
            Main.WORLD.setThundering(false);
            Main.WORLD.setStorm(false);
            Main.WORLD.setClearWeatherDuration(10000);
            activeStorm = false;
        }

        round += 1;
        // alle har fullt med liv
        for (Player p : PLAYERS) {
            playerHP.put(p, settings.playerMaxHP.get(p));
            p.setHealth(playerHP.get(p) * 2);
            updateArmor(p);
            Board.updatePlayerScore(p);
            p.setGameMode(GameMode.ADVENTURE);
        }
        // teleporterer til spawn
        team1.tpPlayersToSpawn();
        team2.tpPlayersToSpawn();
        canMove = false;
        new RoundCountdown().runTaskTimer(PLUGIN, 0L, 20L); // mens de er på spawn, kan de ikke bevege seg og det er nedtelling til det begynner

        if (settings.stormMode && Math.random() < STORM_FREQUENCY.get(settings.stormFrequency)) {
            activeStorm = true;
            new StartStorm().runTaskTimer(PLUGIN, 100L, 100L); // starter storm og timers
        }
        if (settings.currentMap == BotBowsMap.GRAUT_VS_WACKY) { // starts the scanners for the dungeons after 7 seconds
            startScanners();
        }
    }

    public static void updateArmor(Player p) { // updates the armor pieces of the player
        int maxHP = BotBowsManager.settings.playerMaxHP.get(p); // Maks hp til playeren
        if (playerHP.get(p) == maxHP) { // hvis playeren har maxa liv så skal de få fullt ut med armor
            Color color = BotBowsManager.getTeam(p).DYECOLOR.getColor();
            p.getInventory().setArmorContents(new ItemStack[] {makeArmor(Material.LEATHER_BOOTS, color), makeArmor(Material.LEATHER_LEGGINGS, color), makeArmor(Material.LEATHER_CHESTPLATE, color), makeArmor(Material.LEATHER_HELMET, color)});
            return;
        }
        List<Integer> slots;
        if (BotBowsManager.settings.playerMaxHP.get(p) > 5) {
            float d = (float) maxHP / 5;
            int i = (int) Math.ceil((maxHP - playerHP.get(p)) / d);
            slots = PLAYER_HEALTH_ARMOR.get(3).get(i - 1);
        } else {
            slots = PLAYER_HEALTH_ARMOR.get(maxHP - 2).get(maxHP - playerHP.get(p) - 1);
        }

        for (Integer slot : slots) {
            switch (slot) {
                case 0 -> p.getInventory().setBoots(null);
                case 1 -> p.getInventory().setLeggings(null);
                case 2 -> p.getInventory().setChestplate(null);
                case 3 -> p.getInventory().setHelmet(null);
            }
        }
    }

    public static void setPlayerMaxHP(int hp) {
        for (Player p : PLAYERS) {
            settings.playerMaxHP.put(p, hp);
        }
    }

    public static ItemStack makeArmor(Material material, Color color) { // makes armor pieces
        ItemStack armor = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        assert meta != null;
        meta.setColor(color);
        armor.setItemMeta(meta);
        return armor;
    }

    public static void handleHit(Player p, Player q) { // When a player gets hit this function will handle it. p=def, q=atk.
        int playerHealth = playerHP.get(p) - 1;
        playerHP.put(p, playerHealth);
        messagePlayers(STR."\{getTeam(p).COLOR + p.getPlayerListName()} was sniped by \{getTeam(q).COLOR + q.getPlayerListName()}; \{getTeam(p).COLOR}\{playerHealth} hp left.");
        if (playerHealth == 0) {
            p.setGameMode(GameMode.SPECTATOR);
            p.setSpectatorTarget(q);
            p.sendMessage(STR."\{ChatColor.GRAY}Now spectating \{q.getPlayerListName()}");
            check4Victory(p);
        } else {
            updateArmor(p);
            p.setHealth(playerHealth * 2); // playerHealth er i hjerter, mens setHealth er i halvhjerter
        }
        Board.updatePlayerScore(p);
    }

    public static void check4Victory(Player dedPlayer) {
        BotBowsTeam dedPlayerTeam = getTeam(dedPlayer);
        checkTeam4Victory(getOppositeTeam(dedPlayerTeam), dedPlayerTeam);
    }

    public static void checkTeam4Victory(BotBowsTeam winningTeam, BotBowsTeam losingTeam) {
        for (Player p: losingTeam.getPlayers()) { // hvis teamet fortsatt lever
            if (playerHP.get(p) > 0) {
                return;
            }
        }

        messagePlayers(STR."\{winningTeam}\{ChatColor.WHITE} won the round!");
        int winScore = 0;
        if (settings.dynamicScoring) {
            for (Player p : winningTeam.getPlayers()) {
                winScore += playerHP.get(p);
            }
            int enemyTeamMaxHP = 0;
            messagePlayers(STR."\{winningTeam.COLOR}+\{winScore}p for remaining hp");
            for (Player p : losingTeam.getPlayers()) {
                enemyTeamMaxHP += settings.playerMaxHP.get(p);
            }
            winScore += enemyTeamMaxHP;
            messagePlayers(STR."\{winningTeam.COLOR}+\{enemyTeamMaxHP}p for enemy hp lost");
        } else {
            winScore = 1;
        }

        winningTeam.addPoints(winScore);
        messagePlayers(STR."""
        \{team1}: \{ChatColor.WHITE}\{team1.getPoints()}
        \{team2}: \{ChatColor.WHITE}\{team2.getPoints()}""");

        titlePlayers(STR."\{winningTeam} +\{winScore}", 40);
        Board.updateTeamScores();
        if (winningTeam.getPoints() >= BotBowsManager.settings.winThreshold && BotBowsManager.settings.winThreshold > 0) {
            postGame(winningTeam);
            return; // because the match is over, a new round won't start
        }
        startRound();
    }

    private static void initDungeon() {
        for (Player p : PLAYERS) {
            dungeonGhosters.put(p, new DungeonGhoster(p));
        }
    }

    private static void startScanners() {
        for (Player p : PLAYERS) {
            GvwDungeonProximityScanner scanner = new GvwDungeonProximityScanner(p);
            dungeonScanners.put(p, scanner);
            scanner.runTaskTimer(PLUGIN, 140L, 5L);
        }
        //debugMessage("starting scanners");
    }

    public static boolean isInDungeon(Player p) {
        return dungeonScanners.get(p).isInDungeon();
    }

    public static String getSection(Player p) {
        return dungeonGhosters.get(p).getSection();
    }

    public static void handleDungeonMovement(Player p) {
        dungeonGhosters.get(p).handleMovement();
    }

    public static void messagePlayers(String message) {
        for (Player p : PLAYERS) {
            p.sendMessage(message);
        }
    }

    public static void debugMessage(String message) {
        messagePlayers(STR."\{ChatColor.GRAY}[DEBUG]: \{message}");
    }

    public static void debugMessage(String message, boolean bool) {
        if (bool) debugMessage(message);
    }

    public static void titlePlayers(String title, int duration) {
        for (Player p : PLAYERS) {
            p.sendTitle(title, null, 2, duration, 5);
        }
    }

    public static BotBowsTeam getTeam(Player p) {
        return PLAYER_TEAM.get(p);
    }

    public static int getTotalPlayers() {
        return PLAYERS.size();
    }
    public static List<Player> getPlayers() {return PLAYERS;}
    public static void updatePlayerMaxHP(int maxHP) { // updates the amount of hearts the player has
        for (Player p : PLAYERS) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2 * maxHP);
            p.setHealth(BotBowsManager.settings.playerMaxHP.get(p) * 2);
        }
    }

    public static void leaveGame(Player p) {
        if (!PLAYERS.contains(p)) {
            p.sendMessage(STR."\{ChatColor.RED}You cant leave when you're not in a game");
            return;
        }
        if (Bar.sneakBars.containsKey(p)) {
            Bar.sneakBars.get(p).setVisible(false);
            Bar.sneakBars.get(p).removeAll();
            Bar.sneakBars.remove(p);
        }
        Cooldowns.sneakCooldowns.remove(p);
        if (Cooldowns.sneakRunnables.containsKey(p)) {
            Cooldowns.sneakRunnables.get(p).cancel();
            Cooldowns.sneakRunnables.remove(p);
        }
        if (dungeonScanners.containsKey(p)) {
            dungeonScanners.get(p).cancel();
            dungeonScanners.remove(p);
            dungeonGhosters.remove(p);
        }
        isDamaged.remove(p);
        settings.playerMaxHP.remove(p);
        playerHP.remove(p);
        PLAYERS.remove(p);
        getTeam(p).leave(p);
        ((SelectTeamsMenu) Main.menus.get("select teams")).recalculateTeam();
        ((HealthMenu) Main.menus.get("health")).updateMenu();
        p.sendMessage(STR."\{ChatColor.YELLOW}You left BotBows Classic");
        messagePlayers(STR."\{ChatColor.YELLOW}\{p.getPlayerListName()} has left the game (\{PLAYERS.size()})");
        p.setGameMode(GameMode.SPECTATOR);
        p.getInventory().remove(getBotBow());
    }
    public static boolean isPlayerJoined(Player p) {
        return PLAYERS.contains(p);
    }

    public static void postGame(BotBowsTeam winningTeam) {
        activeGame = false;
        if (winningTeam == null) {
            messagePlayers(STR."""
                    \{ChatColor.LIGHT_PURPLE}================
                    The game ended in a tie after \{round} round\{round == 1 ? "" : "s"}
                    ================""");
        } else {
            messagePlayers(STR."""
                    \{winningTeam.COLOR}================
                    TEAM \{winningTeam.toString().toUpperCase()} won the game after \{round} round\{round == 1 ? "" : "s"}! GG
                    ================""");
        }

        postGameTitle(winningTeam);

        Main.WORLD.setThundering(false);
        Main.WORLD.setStorm(false);
        Main.WORLD.setClearWeatherDuration(10000);

        ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
        assert sm != null;
        for (Player p : PLAYERS) {
            p.getInventory().setArmorContents(null);
            p.setScoreboard(sm.getNewScoreboard());
            // cancel sneak detector
            Bar.sneakBars.get(p).setVisible(false);
            if (Cooldowns.sneakRunnables.containsKey(p)) {
                Cooldowns.sneakRunnables.get(p).cancel();
            }
            p.setGlowing(false);
            p.setInvulnerable(false);
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            p.setGameMode(GameMode.SPECTATOR);
        }
        Board.resetTeams();
        Cooldowns.resetStormRunnable();
        isDamaged.clear();
        PLAYERS.clear();
        PLAYER_TEAM.clear();
        team1.reset();
        team2.reset();
        round = 0;
        canMove = true;
        Bar.sneakBars.clear();
        Cooldowns.sneakCooldowns.clear();
        Cooldowns.sneakRunnables.clear();
        if (settings.currentMap == BotBowsMap.GRAUT_VS_WACKY) {
            for (BukkitRunnable scanner : dungeonScanners.values()) {
                scanner.cancel();
            }
            dungeonScanners.clear();
            dungeonGhosters.clear();
        }
    }

    private static void postGameTitle(BotBowsTeam winningTeam) {
        if (winningTeam == null) {
            return;
        }
        BotBowsTeam losingTeam = getOppositeTeam(winningTeam);
        for (Player p :winningTeam.getPlayers()) {
            p.sendTitle(STR."\{winningTeam.COLOR}Victory", null, 10, 60, 20);
        }
        for (Player p : losingTeam.getPlayers()) {
            p.sendTitle(STR."\{losingTeam.COLOR}Defeat", null, 10, 60, 20);
        }
    }

    public static void endGame() { // the game has ended, check who won
        if (team1.getPoints() == team2.getPoints()) {
            postGame(null);
        } else if (team1.getPoints() > team2.getPoints()) {
            postGame(team1);
        } else {
            postGame(team2);
        }
    }
}
