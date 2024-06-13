package gruvexp.gruvexp.twtClassic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Board {

    private static Objective objective;
    private static Team blue;
    private static Team red;

    public static void createBoard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        objective = board.registerNewObjective("botbows", Criteria.DUMMY,
                ChatColor.translateAlternateColorCodes('&', "&l&6BotBows &r&bClassic"));

        // setter inn scores
        /**for (int i = 0; i < BotBowsManager.team_red.size(); i++) { // røe spillere
            Player p = BotBowsManager.team_red.get(i);
            setScore( ChatColor.RED + "❤".repeat(BotBowsManager.Player2MaxHP.get(p)) + " " + p.getPlayerListName(), i);
        }**/
        setScore(ChatColor.DARK_RED + "TEAM SAUCE", BotBowsManager.teamRed.size());

        /**for (int i = 0; i < BotBowsManager.team_blue.size(); i++) { // blåe spillere //FJERN KANSKJE!!!!! <========== blir updata rett etterpå hver gang runda starter
            Player p = BotBowsManager.team_blue.get(i);
            setScore(ChatColor.RED + "❤".repeat(BotBowsManager.Player2MaxHP.get(p)) + " " + ChatColor.BLUE + p.getPlayerListName(), i + 1 + BotBowsManager.team_red.size());
        }**/
        setScore(ChatColor.DARK_BLUE + "TEAM BLAUD", BotBowsManager.getTotalPlayers() + 1);
        setScore(ChatColor.GRAY + "----------", BotBowsManager.getTotalPlayers() + 2);
        //setScore(ChatColor.RED + "Red:  " + ChatColor.RESET + BotBowsManager.getPoints("red"), BotBowsManager.getTotalPlayers() + 3);
        //setScore(ChatColor.BLUE + "Blue: " + ChatColor.RESET + BotBowsManager.getPoints("blue"), BotBowsManager.getTotalPlayers() + 4);
        setScore("", BotBowsManager.getTotalPlayers() + 5);

        for (Player p:Bukkit.getOnlinePlayers()) {
            p.setScoreboard(board);
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Team stuff!
        blue = board.registerNewTeam("Blue");
        red = board.registerNewTeam("Red");

        blue.setColor(ChatColor.BLUE);
        red.setColor(ChatColor.RED);

        for (Player p: BotBowsManager.teamBlue) {
            blue.addPlayer(p);
        }
        for (Player p: BotBowsManager.teamRed) {
            red.addPlayer(p);
        }
    }

    public static void updatePlayerScore(Player p) {
        Scoreboard sb = objective.getScoreboard();

        //fjerner scoren hvis den allerede er der, siden man skal bytte den ut med den oppdaterte versjonen
        for (Objective ignored : sb.getObjectives()) {
            for (String entries : sb.getEntries()) {
                if (entries.contains(p.getPlayerListName())) {
                    sb.resetScores(entries);
                }
            }
        }

        int hp = BotBowsManager.playerHP.get(p);
        int max_hp = BotBowsManager.playerMaxHP.get(p);
        int p_score;
        if (BotBowsManager.teamBlue.contains(p)) {
            p_score = BotBowsManager.teamBlue.indexOf(p) + BotBowsManager.teamRed.size() + 1;
        } else {
            p_score = BotBowsManager.teamRed.indexOf(p);
        }

        String s;
        if (max_hp > 5) {
            s = ChatColor.RED + "▏".repeat(hp) + ChatColor.GRAY + "▏".repeat(max_hp - hp) + BotBowsManager.getTeamColor(p) + " " + p.getPlayerListName();
        } else {
            s = ChatColor.RED + "❤".repeat(hp) + ChatColor.GRAY + "❤".repeat(max_hp - hp) + BotBowsManager.getTeamColor(p) + " " + p.getPlayerListName();
        }

        setScore(s, p_score);

    }

    public static void updateTeamScores() {
        Scoreboard sb = objective.getScoreboard();
        int winThreshold = BotBowsManager.winThreshold;

        for (Objective ignored : sb.getObjectives()) {
            for (String entries : sb.getEntries()) {
                if (entries.contains("Blue: ")) { // hvis "BLUE" har mellomrom etter seg så er det den linjen som viser poeng
                    sb.resetScores(entries);
                }
                if (entries.contains("Red:  ")) {
                    sb.resetScores(entries);
                }
            }
        }
        if (winThreshold == -1) {
            setScore(ChatColor.BLUE + "Blue: " + ChatColor.RESET + BotBowsManager.getPoints("blue"), 4 + BotBowsManager.getTotalPlayers()); // legger inn scoren til hvert team
            setScore(ChatColor.RED + "Red:  " + ChatColor.RESET + BotBowsManager.getPoints("red"), 3 + BotBowsManager.getTotalPlayers());
        } else if (winThreshold >= 35) {
            setScore(ChatColor.BLUE + "Blue: " + ChatColor.RESET + BotBowsManager.getPoints("blue") + " / " + ChatColor.GRAY + winThreshold, 4 + BotBowsManager.getTotalPlayers()); // legger inn scoren til hvert team
            setScore(ChatColor.RED + "Red:  " + ChatColor.RESET + BotBowsManager.getPoints("red") + " / " + ChatColor.GRAY + winThreshold, 3 + BotBowsManager.getTotalPlayers());
        } else { // få plass til mest mulig streker
            String c = "";
            if (winThreshold < 8) {
                c = "█";
            } else if (winThreshold < 9) {
                c = "▉";
            } else if (winThreshold < 10) {
                c = "▊";
            } else if (winThreshold < 12) {
                c = "▋";
            } else if (winThreshold < 15) {
                c = "▌";
            } else if (winThreshold < 17) {
                c = "▍";
            } else if (winThreshold < 23) {
                c = "▎";
            } else if (winThreshold < 34) {
                c = "▏";
            }
            int blue_points = BotBowsManager.getPoints("blue");
            int red_points = BotBowsManager.getPoints("red");
            if (blue_points > winThreshold) { // antall points cappes på win_threshold, så hvis noen hadde 3p og fikk 2p, men win thresholden er 4, så står det at de har 4p
                blue_points = winThreshold;
            } else if (red_points > winThreshold) {
                red_points = winThreshold;
            }

            setScore(ChatColor.BLUE + "Blue: " + ChatColor.GREEN + c.repeat(blue_points) + ChatColor.GRAY + c.repeat(winThreshold - blue_points), 4 + BotBowsManager.getTotalPlayers()); // legger inn scoren til hvert team
            setScore(ChatColor.RED + "Red:  " + ChatColor.GREEN + c.repeat(red_points) + ChatColor.GRAY + c.repeat(winThreshold - red_points), 3 + BotBowsManager.getTotalPlayers());
        }
    }

    private static void setScore(String text, int score) {
        Score l1 = objective.getScore(text);
        l1.setScore(score); //nederst
    }

    public static void resetTeams() {
        blue.unregister();
        red.unregister();
    }

}
