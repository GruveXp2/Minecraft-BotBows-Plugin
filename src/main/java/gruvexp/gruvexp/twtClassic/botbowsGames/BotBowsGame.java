package gruvexp.gruvexp.twtClassic.botbowsGames;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.commands.TestCommand;
import gruvexp.gruvexp.tasks.BotBowsGiver;
import gruvexp.gruvexp.tasks.RoundCountdown;
import gruvexp.gruvexp.twtClassic.*;
import gruvexp.gruvexp.twtClassic.botbowsTeams.BotBowsTeam;
import gruvexp.gruvexp.twtClassic.hazard.EarthquakeHazard;
import gruvexp.gruvexp.twtClassic.hazard.StormHazard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import java.util.Set;

import static gruvexp.gruvexp.twtClassic.Bar.sneakBarInit;

public class BotBowsGame {

    public final Settings settings;
    protected final BotBowsTeam team1;
    protected final BotBowsTeam team2;
    protected final Set<BotBowsPlayer> players;
    protected final StormHazard stormHazard;
    protected final EarthquakeHazard earthquakeHazard;
    public boolean canMove = true;
    protected int round = 0; // hvilken runde man er på

    public BotBowsGame(Settings settings) {
        this.settings = settings;
        this.team1 = settings.team1;
        this.team2 = settings.team2;
        this.players = settings.getPlayers();
        this.stormHazard = settings.stormHazard;
        this.earthquakeHazard = settings.earthquakeHazard;
    }

    public void leaveGame(BotBowsPlayer p) {
        // stuff
        settings.leaveGame(p);
        Board.removePlayerScore(p);
        if (Bar.sneakBars.containsKey(p.PLAYER)) {
            Bar.sneakBars.get(p.PLAYER).setVisible(false);
            Bar.sneakBars.get(p.PLAYER).removeAll();
            Bar.sneakBars.remove(p.PLAYER);
        }
        Cooldowns.sneakCooldowns.remove(p.PLAYER);
        if (Cooldowns.sneakRunnables.containsKey(p.PLAYER)) {
            Cooldowns.sneakRunnables.get(p.PLAYER).cancel();
            Cooldowns.sneakRunnables.remove(p.PLAYER);
        }
    }

    public void startGame(Player gameStarter) {
        BotBows.activeGame = true;
        BotBows.messagePlayers(STR."\{ChatColor.GRAY}\{gameStarter.getName()}: \{ChatColor.GREEN}The game has started!");
        sneakBarInit();
        Cooldowns.CoolDownInit(players);
        Board.createBoard();
        startRound();
        stormHazard.init();
        earthquakeHazard.init();

        // legger til player liv osv
        for (BotBowsPlayer q : players) {
            q.revive();
            Board.updatePlayerScore(q);
        }
        Board.updateTeamScores();
        new BotBowsGiver().runTaskTimer(Main.getPlugin(), 100L, 10L);
    }

    public void startRound() {
        round ++;
        // alle har fullt med liv
        for (BotBowsPlayer p : players) {
            p.revive();
        }
        stormHazard.end();
        earthquakeHazard.end();
        // teleporterer til spawn
        team1.tpPlayersToSpawn();
        team2.tpPlayersToSpawn();
        canMove = false;
        new RoundCountdown(this).runTaskTimer(Main.getPlugin(), 0L, 20L); // mens de er på spawn, kan de ikke bevege seg og det er nedtelling til det begynner
    }

    public void triggerHazards() {
        stormHazard.triggerOnChance();
        earthquakeHazard.triggerOnChance();
    }

    public void handleMovement(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        boolean b = TestCommand.log;
        Block block = p.getLocation().add(0, -0.05, 0).getBlock(); // sjekker rett under, bare 0.05 itilfelle det er teppe

        BotBows.debugMessage(STR."1: Material: \{block.getType().name()}", b);
        if (block.getType() == Material.AIR) {
            block = p.getLocation().add(0, -0.9, 0).getBlock(); // hvis man står på kanten av et teppe kan det være en effektblokk under
            BotBows.debugMessage(STR."2: Material: \{block.getType().name()}", b);
        }
        if (block.getType() == Material.AIR) {
            block = p.getLocation().add(0, 0, 0).getBlock();
            BotBows.debugMessage(STR."3: Material: \{block.getType().name()}", b);
        } else {
            BotBows.debugMessage(STR."Material: \{block.getType().name()}", b);
        }
        Material material = block.getType();
        if (block.getType() == Material.LIGHT) {
            BotBows.debugMessage(STR."Light level: \{((Light) block.getBlockData()).getLevel()}", b);
            if (((Light) block.getBlockData()).getLevel() == 0) { // sida det ikke går an å sjekke når players står uttafor kanten, så workarounder jeg det ved å sette light bloccs ved sida cyan yeetpads
                material = Material.CYAN_CARPET;
                BotBows.debugMessage("yee it works", b);
            }
        }
        switch (material) { // add effekter basert på åssen blokk som er under
            case YELLOW_CONCRETE, YELLOW_CONCRETE_POWDER, YELLOW_CARPET -> {
                p.addPotionEffect(new PotionEffect(org.bukkit.potion.PotionEffectType.JUMP_BOOST, 1200, 6, true, false));
            }
            case CYAN_CONCRETE, CYAN_CONCRETE_POWDER, CYAN_CARPET -> {
                double Δy = e.getTo().getY() - e.getFrom().getY();
                if (Δy <= 0.1) {break;} // fortsett bare viss man har hoppa (et visst antall upwards momentum)
                double vX = p.getLocation().getDirection().getX();
                double vZ = p.getLocation().getDirection().getZ();

                p.setVelocity(new Vector(vX*2.5, 0.5, vZ*2.5));
                p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 10, 2);
            }
            default -> p.removePotionEffect(PotionEffectType.JUMP_BOOST);
        }
    }

    public void check4Victory(BotBowsPlayer dedPlayer) {
        BotBowsTeam dedPlayerTeam = dedPlayer.getTeam();
        checkTeam4Victory(dedPlayerTeam.getOppositeTeam(), dedPlayerTeam);
    }

    public void checkTeam4Victory(BotBowsTeam winningTeam, BotBowsTeam losingTeam) {
        for (BotBowsPlayer p: losingTeam.getPlayers()) { // hvis teamet fortsatt lever
            if (p.getHP() > 0) {
                return;
            }
        }

        BotBows.messagePlayers(STR."\{winningTeam}\{ChatColor.WHITE} won the round!");
        int winScore = 0;
        if (settings.dynamicScoring) {
            for (BotBowsPlayer p : winningTeam.getPlayers()) {
                winScore += p.getHP();
            }
            int enemyTeamMaxHP = 0;
            BotBows.messagePlayers(STR."\{winningTeam.COLOR}+\{winScore}p for remaining hp");
            for (BotBowsPlayer p : losingTeam.getPlayers()) {
                enemyTeamMaxHP += p.getMaxHP();
            }
            winScore += enemyTeamMaxHP;
            BotBows.messagePlayers(STR."\{winningTeam.COLOR}+\{enemyTeamMaxHP}p for enemy hp lost");
        } else {
            winScore = 1;
        }

        winningTeam.addPoints(winScore);
        BotBows.messagePlayers(STR."""
        \{team1}: \{ChatColor.WHITE}\{team1.getPoints()}
        \{team2}: \{ChatColor.WHITE}\{team2.getPoints()}""");

        BotBows.titlePlayers(STR."\{winningTeam} +\{winScore}", 40);
        Board.updateTeamScores();

        if (winningTeam.getPoints() >= settings.getWinThreshold() && settings.getWinThreshold() > 0) {
            postGame(winningTeam);
            return; // because the match is over, a new round won't start
        }
        startRound();
    }

    public void postGame(BotBowsTeam winningTeam) {
        BotBows.activeGame = false;
        canMove = true;
        if (winningTeam == null) {
            BotBows.messagePlayers(STR."""
                    \{ChatColor.LIGHT_PURPLE}================
                    The game ended in a tie after \{round} round\{round == 1 ? "" : "s"}
                    ================""");
        } else {
            BotBows.messagePlayers(STR."""
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
        for (BotBowsPlayer p : players) {
            p.reset();
        }
        Board.resetTeams();
        team1.reset();
        team2.reset();
        Bar.sneakBars.clear();
        Cooldowns.sneakCooldowns.clear();
        Cooldowns.sneakRunnables.clear();
        stormHazard.end();
        earthquakeHazard.end();
    }

    private void postGameTitle(BotBowsTeam winningTeam) {
        if (winningTeam == null) {
            return;
        }
        BotBowsTeam losingTeam = winningTeam.getOppositeTeam();
        for (BotBowsPlayer p :winningTeam.getPlayers()) {
            p.PLAYER.sendTitle(STR."\{winningTeam.COLOR}Victory", null, 10, 60, 20);
        }
        for (BotBowsPlayer p : losingTeam.getPlayers()) {
            p.PLAYER.sendTitle(STR."\{losingTeam.COLOR}Defeat", null, 10, 60, 20);
        }
    }

    public void endGame() { // the game has ended, check who won
        if (team1.getPoints() == team2.getPoints()) {
            postGame(null);
        } else if (team1.getPoints() > team2.getPoints()) {
            postGame(team1);
        } else {
            postGame(team2);
        }
    }
}