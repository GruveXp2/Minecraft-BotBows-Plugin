package gruvexp.gruvexp.twtClassic;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.BotBowsGiver;
import gruvexp.gruvexp.tasks.RoundCountdown;
import gruvexp.gruvexp.tasks.StartStorm;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

import static gruvexp.gruvexp.twtClassic.Bar.sneakBarInit;

public class BotBowsManager { // FANCY STUFF MAN KAN KLIKKE PÅ TEXT I CHATTEN OG COMMAND RUNNES!!!

    private static final Main PLUGIN = Main.getPlugin();
    private static final ArrayList<Player> PLAYERS = new ArrayList<>(); // liste med alle players som er i gamet
    public static final ArrayList<Location> BLUE_SPAWN = new ArrayList<>(4);
    public static final ArrayList<Location> RED_SPAWN = new ArrayList<>(4);
    private static final ArrayList<ArrayList<ArrayList<Integer>>> PLAYER_HEALTH_ARMOR = new ArrayList<>(); // Når man tar damag så kan man gette em liste med hvilke armor pieces som skal fjernes
    private static final ArrayList<Double> STORM_FREQUENCY_DOUBLE = new ArrayList<>(List.of(0.05, 0.1, 0.25, 0.5, 1.0));
    public static HashMap<Player, Integer> Player2HP = new HashMap<>();
    public static HashMap<Player, Integer> Player2MaxHP = new HashMap<>();
    public static HashMap<Player, Boolean> Player2IsDamaged = new HashMap<>();
    public static ArrayList<Player> teamBlue = new ArrayList<>();
    public static ArrayList<Player> teamRed = new ArrayList<>();
    private static int[] teamPoints = new int[] {0, 0}; // blue, red
    public static int maxHP = 3;
    public static  boolean stormMode = false;
    public static int stormFrequency = 2; // 5%, 10%, 25%, 50%, 100%
    private static boolean activeStorm = false;
    private static boolean dynamicScoring = true; // If true, når alle på et lag dør så gis et poeng for hvert liv som er igjen
    public static int winThreshold = 5; // hvor mange poeng man skal spille til. Hvis den er satt til -1, så fortsetter det for alltid
    private static int round = 0; // hvilken runde man er på
    public static boolean activeGame = false; // hvis spillet har starta, så kan man ikke gjøre ting som /settings
    public static boolean canMove = true;


    public static ItemStack getBotBow() {
        ItemStack BOTBOW = new ItemStack(Material.CROSSBOW);
        CrossbowMeta meta = (CrossbowMeta) BOTBOW.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "BotBow");
        meta.setLore(Arrays.asList("The strongest bow", "ever known to man"));
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 10, true);
        meta.addChargedProjectile(new ItemStack(Material.ARROW));
        BOTBOW.setItemMeta(meta);
        BOTBOW.setDurability((short) 464);
        return BOTBOW;
    }

    public static void setDynamicScoring(boolean dynamic_scoring) {
        BotBowsManager.dynamicScoring = dynamic_scoring;
    }

    public static void armorInit() { // definerer hvilke armor som skal mistes når playeren har x antall liv. 0=boots, 1=leggings, 2=chestplate, 3=helmet
        ArrayList<ArrayList<Integer>> hp2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> hp3 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> hp4 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> hp5 = new ArrayList<>();
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

    public static void spawnInit() {

        BLUE_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -215.5, 22.0, -167.5, 90, 10));
        BLUE_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -215.5, 22.0, -164.5, 90, 10));
        BLUE_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -213.5, 22.0, -169.5, 90, 10));
        BLUE_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -213.5, 22.0, -162.5, 90, 10));
        BLUE_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -212.5, 22.0, -166.0, -90, 10));

        RED_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -268.5, 22.0, -164.5, -90, 10));
        RED_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -268.5, 22.0, -167.5, -90, 10));
        RED_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -270.5, 22.0, -162.5, -90, 10));
        RED_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -270.5, 22.0, -169.5, -90, 10));
        RED_SPAWN.add(new Location(Bukkit.getWorld("BotBows (S2E1)"), -271.5, 22.0, -166.0, -90, 10));

    }

    public static void joinGame(Player p) {
        if (!PLAYERS.contains(p)) {
            if (activeGame) {
                p.sendMessage(ChatColor.RED + "A game is already ongoing, wait until it ends before you join");
                return;
            }
            PLAYERS.add(p);
            teamBlue.add(p);
            if (PLAYERS.size() > 1) {
                Player2MaxHP.put(p, maxHP);
                Main.Id2Menu.get("botbows settings 1").callInternalFunction(0);
                Main.Id2Menu.get("botbows settings 2").callInternalFunction(0);
            } else {
                Player2MaxHP.put(p, 3);
            }
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Player2MaxHP.get(p) * 2);
            p.setHealth(Player2MaxHP.get(p) * 2);
            for (Player q : PLAYERS) {
                q.sendMessage(p.getPlayerListName() +" has joined BotBows Classic! ("+ PLAYERS.size() +")");
            }
        }
        else {
            p.sendMessage( ChatColor.RED + "You already joined!");
        }
    }

    public static void startGame(Player p) {
        activeGame = true;
        messagePlayers(ChatColor.GRAY + p.getName() + ": " + ChatColor.GREEN + "The game has started!");
        sneakBarInit();
        Cooldowns.CoolDownInit(PLAYERS);
        Board.createBoard();
        startRound();
        if (stormMode) {
            Bar.stormBarInit();
        }

        // legger til player liv osv
        for (Player q : PLAYERS) {
            Player2IsDamaged.put(q, false);
            Board.updatePlayerScore(q);
        }
        Board.updateTeamScores();
        new BotBowsGiver().runTaskTimer(Main.getPlugin(), 100L, 10L);
    }

    public static void startRound() {
        if (activeStorm) {
            Cooldowns.resetStormRunnable();
            Main.WORLD.setThundering(false);
            Main.WORLD.setStorm(false);
            Main.WORLD.setClearWeatherDuration(10000);
        }
        activeStorm = false;

        round += 1;
        // alle har fullt med liv
        for (Player p : PLAYERS) {
            Player2HP.put(p, Player2MaxHP.get(p));
            p.setHealth(Player2HP.get(p) * 2);
            updateArmor(p);
            Board.updatePlayerScore(p);
            p.setGameMode(GameMode.ADVENTURE);
        }
        // teleporterer til spawn
        for (int i = 0; i < teamBlue.size(); i++) {
            teamBlue.get(i).teleport(BLUE_SPAWN.get(i));
        }
        for (int i = 0; i < teamRed.size(); i++) {
            teamRed.get(i).teleport(RED_SPAWN.get(i));
        }
        canMove = false;
        new RoundCountdown().runTaskTimer(PLUGIN, 0L, 20L); // mens de er på spawn, kan de ikke bevege seg og det er nedtelling til det begynner

        if (stormMode && Math.random() < STORM_FREQUENCY_DOUBLE.get(stormFrequency)) {
            activeStorm = true;
            new StartStorm().runTaskTimer(PLUGIN, 100L, 100L); // starter storm og timers
        }
    }

    public static void updateArmor(Player p) { // updates the armor pieces of the player
        int maxHP = Player2MaxHP.get(p); // Maks hp til playeren
        if (Player2HP.get(p) == maxHP) { // hvis playeren har maxa liv så skal de få fullt ut med armor
            Color color;
            if (teamBlue.contains(p)) {
                color = Color.BLUE;
            } else {
                color = Color.RED;
            }
            p.getInventory().setArmorContents(new ItemStack[] {makeArmor(Material.LEATHER_BOOTS, color), makeArmor(Material.LEATHER_LEGGINGS, color), makeArmor(Material.LEATHER_CHESTPLATE, color), makeArmor(Material.LEATHER_HELMET, color)});
            return;
        }
        ArrayList<Integer> slots;
        if (Player2MaxHP.get(p) > 5) {
            float d = (float) maxHP / 5;
            int i = (int) Math.ceil((maxHP - Player2HP.get(p)) / d);
            slots = PLAYER_HEALTH_ARMOR.get(3).get(i - 1);
        } else {
            slots = PLAYER_HEALTH_ARMOR.get(maxHP - 2).get(maxHP - Player2HP.get(p) - 1);
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
            Player2MaxHP.put(p, hp);
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
        int playerHealth = Player2HP.get(p) - 1;
        Player2HP.put(p, playerHealth);
        messagePlayers(getTeamColor(p) + p.getPlayerListName() + " was sniped by " + getTeamColor(q) + q.getPlayerListName() + "; " + getTeamColor(p) + playerHealth + " hp left.");
        if (playerHealth == 0) {
            p.setGameMode(GameMode.SPECTATOR);
            p.setSpectatorTarget(q);
            p.sendMessage(ChatColor.GRAY + "Now spectating " + q.getPlayerListName());
            check4Victory(p);
        } else {
            updateArmor(p);
            p.setHealth(playerHealth * 2); // playerHealth er i hjerter, mens setHealth er i halvhjerter
        }
        Board.updatePlayerScore(p);
    }

    public static void check4Victory(Player dedPlayer) {
        if (teamBlue.contains(dedPlayer)) {
            checkTeam4Victory(teamRed, teamBlue, "Red");
        } else {
            checkTeam4Victory(teamBlue, teamRed, "Blue");
        }
    }

    public static void checkTeam4Victory(ArrayList<Player> team1, ArrayList<Player> team2, String team1ColorName) {

        for (Player p: team2) { // hvis teamet fortsatt lever
            if (Player2HP.get(p) > 0) {
                return;
            }
        }

        ChatColor team1Color = ChatColor.BLUE;
        String team1Name = "BLAUD";
        int team1ID = 0;

        if (Objects.equals(team1ColorName, "Red")) {
            team1Color = ChatColor.RED;
            team1Name = "SAUCE";
            team1ID = 1;
        }

        // TEAM1 WIN
        messagePlayers(team1Color + team1ColorName + ChatColor.WHITE +  " won the round!");
        int winScore = 0;
        if (dynamicScoring) {
            for (Player p : team1) {
                winScore += Player2HP.get(p);
            }
            int enemyTeamMaxHP = 0;
            messagePlayers(team1Color + "+" + winScore + "p for remaining hp");
            for (Player p : team2) {
                enemyTeamMaxHP += Player2MaxHP.get(p);
            }
            winScore += enemyTeamMaxHP;
            messagePlayers(team1Color + "+" + enemyTeamMaxHP + "p for enemy hp lost");
        } else {
            winScore = 1;
        }

        teamPoints[team1ID] += winScore; // team1 gets points
        messagePlayers(ChatColor.BLUE + "Blue: " + ChatColor.WHITE + teamPoints[0] + "\n" + ChatColor.RED + "Red: " + ChatColor.WHITE + teamPoints[1]);
        titlePlayers(team1Color + team1ColorName + " +" + winScore, 40);
        Board.updateTeamScores();
        if (teamPoints[team1ID] >= BotBowsManager.winThreshold && BotBowsManager.winThreshold > 0) {
            postGame("TEAM " + team1Name, team1Color);
            return; // siden team1 vinner, returner vi før vi kommer til linjen som starter ny runde
        }
        startRound();
    }

    public static void messagePlayers(String message) {
        for (Player p : PLAYERS) {
            p.sendMessage(message);
        }
    }

    public static void titlePlayers(String title, int duration) {
        for (Player p : PLAYERS) {
            p.sendTitle(title, null, 2, duration, 5);
        }
    }

    public static ChatColor getTeamColor(Player p) {
        return teamBlue.contains(p) ? ChatColor.BLUE : ChatColor.RED;
    }

    public static ArrayList<Player> getTeam(Player p) {
        return teamBlue.contains(p) ? teamBlue : teamRed;
    }

    public static ArrayList<Player> getOpponentTeam(Player p) {
        return teamBlue.contains(p) ? teamRed : teamBlue;
    }

    public static Location getPlayerSpawn(Player p) {
        if (teamBlue.contains(p)) {
            int i = teamBlue.indexOf(p);
            return BLUE_SPAWN.get(i);
        } else {
            int i = teamRed.indexOf(p);
            return RED_SPAWN.get(i);
        }
    }

    public static int getTotalPlayers() {
        return PLAYERS.size();
    }
    public static ArrayList<Player> getPlayers() {return PLAYERS;}
    public static void updatePlayerMaxHP(int maxHP) { // updates the amount of hearts the player has
        for (Player p : PLAYERS) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2 * maxHP);
            p.setHealth(Player2MaxHP.get(p) * 2);
        }
    }

    public static int getPoints(String team) {
        return switch (team) {
            case "blue" -> teamPoints[0];
            case "red" -> teamPoints[1];
            default -> -1;
        };
    }

    public static void leaveGame(Player p) {
        if (!PLAYERS.contains(p)) {
            p.sendMessage(ChatColor.RED + "You cant leave when you're not in a game");
            return;
        }
        if (Bar.Player2ShiftBar.containsKey(p)) {
            Bar.Player2ShiftBar.get(p).setVisible(false);
            Bar.Player2ShiftBar.get(p).removeAll();
            Bar.Player2ShiftBar.remove(p);
        }
        Cooldowns.Player2SneakCoolDown.remove(p);
        if (Cooldowns.Player2SneakRunnable.containsKey(p)) {
            Cooldowns.Player2SneakRunnable.get(p).cancel();
            Cooldowns.Player2SneakRunnable.remove(p);
        }
        Player2IsDamaged.remove(p);
        Player2MaxHP.remove(p);
        Player2HP.remove(p);
        PLAYERS.remove(p);
        if (teamBlue.contains(p)) {
            teamBlue.remove(p);
        } else {
            teamRed.remove(p);
        }
        Main.Id2Menu.get("botbows settings 1").callInternalFunction(0);
        Main.Id2Menu.get("botbows settings 2").callInternalFunction(0);
        p.sendMessage(ChatColor.YELLOW + "You left BotBows Classic");
        messagePlayers(ChatColor.YELLOW + p.getPlayerListName() + " has left the game (" + PLAYERS.size() + ")");
    }
    public static boolean isPlayerJoined(Player p) {
        return PLAYERS.contains(p);
    }

    public static void postGame(String winner, ChatColor color) {
        activeGame = false;
        messagePlayers(color + "================\n" + winner + " won the game after " + round + " rounds! GG\n================");

        postGameTitle(winner);

        Main.WORLD.setThundering(false);
        Main.WORLD.setStorm(false);
        Main.WORLD.setClearWeatherDuration(10000);

        for (Player p : PLAYERS) {
            p.getInventory().setArmorContents(null);

            ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
            assert sm != null;
            p.setScoreboard(sm.getNewScoreboard());
            Bar.Player2ShiftBar.get(p).setVisible(false);
            if (Cooldowns.Player2SneakRunnable.containsKey(p)) {
                Cooldowns.Player2SneakRunnable.get(p).cancel();
            }
            p.setGlowing(false);
            p.setInvulnerable(false);
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            p.setGameMode(GameMode.SPECTATOR);
        }
        Board.resetTeams();
        Cooldowns.resetStormRunnable();
        Player2IsDamaged.clear();
        PLAYERS.clear();
        teamBlue.clear();
        teamRed.clear();
        teamPoints = new int[]{0, 0};
        round = 0;
        canMove = true;
        Bar.Player2ShiftBar.clear();
        Cooldowns.Player2SneakCoolDown.clear();
        Cooldowns.Player2SneakRunnable.clear();
        Main.MenuInit();
    }

    private static void postGameTitle(String winner) {
        if (Objects.equals(winner, "Nobody")) {
            return;
        }
        ArrayList<Player> winningTeam = teamBlue;
        ArrayList<Player> losingTeam = teamRed;
        ChatColor winningColor = ChatColor.BLUE;
        if (Objects.equals(winner, "TEAM SAUCE")) {
            winningTeam = teamRed;
            losingTeam = teamBlue;
            winningColor = ChatColor.RED;
        }

        for (Player p : winningTeam) {
            p.sendTitle(winningColor + "Victory", null, 10, 60, 20);
        }
        for (Player p : losingTeam) {
            p.sendTitle(winningColor + "Defeat", null, 10, 60, 20);
        }
    }

    public static void endGame() { // finn ut hvilke lag som leder
        if (teamPoints[0] == teamPoints[1]) {
            postGame("Nobody", ChatColor.LIGHT_PURPLE);
        } else if (teamPoints[0] > teamPoints[1]) {
            postGame("TEAM BLAUD", ChatColor.BLUE);
        } else {
            postGame("TEAM SAUCE", ChatColor.RED);
        }

    }

}
