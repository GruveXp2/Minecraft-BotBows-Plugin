package gruvexp.gruvexp.twtClassic.botbowsGames;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.tasks.GvwDungeonProximityScanner;
import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import gruvexp.gruvexp.twtClassic.DungeonGhoster;
import gruvexp.gruvexp.twtClassic.Settings;
import gruvexp.gruvexp.twtClassic.botbowsTeams.BotBowsTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class GrautWackyGame extends BotBowsGame {

    private static final Map<BotBowsPlayer, GvwDungeonProximityScanner> dungeonScanners = new HashMap<>();
    private static final Map<BotBowsPlayer, DungeonGhoster> dungeonGhosters = new HashMap<>();

    public GrautWackyGame(Settings settings) {
        super(settings);
    }

    @Override
    public void leaveGame(BotBowsPlayer p) {
        super.leaveGame(p);
        if (dungeonScanners.containsKey(p)) {
            dungeonScanners.get(p).cancel();
            dungeonScanners.remove(p);
            dungeonGhosters.remove(p);
        }
    }

    @Override
    public void startGame(Player gameStarter) {
        super.startGame(gameStarter);
        initDungeon();
    }

    @Override
    public void startRound() {
        super.startRound();
        startScanners();
    }

    @Override
    public void handleMovement(PlayerMoveEvent e) {
        super.handleMovement(e);
        Player p = e.getPlayer();
        BotBowsPlayer bp = BotBows.getBotBowsPlayer(p);
        if (BotBows.settings.isPlayerJoined(p) && isInDungeon(bp)) {
            handleDungeonMovement(bp);
        }
    }

    @Override
    public void postGame(BotBowsTeam winningTeam) {
        super.postGame(winningTeam);
        for (BukkitRunnable scanner : dungeonScanners.values()) {
            scanner.cancel();
        }
        dungeonScanners.clear();
        dungeonGhosters.clear();
    }

    private void initDungeon() {
        for (BotBowsPlayer p : PLAYERS) {
            dungeonGhosters.put(p, new DungeonGhoster(p));
        }
    }

    private void startScanners() {
        for (BotBowsPlayer p : PLAYERS) {
            GvwDungeonProximityScanner scanner = new GvwDungeonProximityScanner(p.PLAYER);
            dungeonScanners.put(p, scanner);
            scanner.runTaskTimer(Main.getPlugin(), 140L, 5L);
        }
        //debugMessage("starting scanners");
    }

    public boolean isInDungeon(BotBowsPlayer p) {
        return dungeonScanners.get(p).isInDungeon();
    }

    public String getSection(BotBowsPlayer p) {
        return dungeonGhosters.get(p).getSection();
    }

    public void handleDungeonMovement(BotBowsPlayer p) {
        dungeonGhosters.get(p).handleMovement();
    }
}
