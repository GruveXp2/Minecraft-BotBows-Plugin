package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.SettingsMenu;
import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.botbowsTeams.BotBowsTeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class SelectTeamsMenu extends SettingsMenu {
    @Override
    public String getMenuName() {
        return "Select teams";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        // if you click on a player then they change teams
        Player clicker = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {
            case PLAYER_HEAD -> {
                Player p = Bukkit.getPlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), "uuid"), PersistentDataType.STRING)));
                BotBowsTeam team = BotBows.getBotBowsPlayer(p).getTeam();
                team.getOppositeTeam().join(BotBows.getBotBowsPlayer(p));
                recalculateTeam();
                BotBows.healthMenu.updateMenu(); // pga teammembers endres må health settings oppdateres pga det er basert på farger
            }
            case BARRIER -> clicker.closeInventory();
            case LIGHT_BLUE_STAINED_GLASS_PANE -> {
                if (e.getSlot() == 21) {
                    BotBows.mapMenu.open(clicker);
                } else if (e.getSlot() == 23) {
                    BotBows.healthMenu.open(clicker);
                }
            }
        }
    }

    @Deprecated
    public void judithBytterLag() {
        Player p = Bukkit.getPlayer("Spionagent54");
        BotBowsTeam team = BotBows.getBotBowsPlayer(p).getTeam();
        team.moveToTeam(BotBows.getBotBowsPlayer(p), team.getOppositeTeam());
        recalculateTeam();
        ((HealthMenu) Main.menus.get("health")).updateMenu(); // pga teammembers endres må health settings oppdateres pga det er basert på farger
    }

    @Override
    public void setMenuItems() {
        setColoredGlassPanes();
        // page stuff
        inventory.setItem(21, LEFT);
        inventory.setItem(22, CLOSE);
        inventory.setItem(23, RIGHT);
    }

    public void setColoredGlassPanes() {
        ItemStack team1Pane = makeItem(BotBows.team1.getGlassPane(), STR."\{BotBows.team1.COLOR}Team \{BotBows.team1}");
        ItemStack team2Pane = makeItem(BotBows.team2.getGlassPane(), STR."\{BotBows.team2.COLOR}Team \{BotBows.team2}");
        inventory.setItem(0, team1Pane);
        inventory.setItem(1, team1Pane);
        inventory.setItem(7, team1Pane);
        inventory.setItem(8, team1Pane);
        inventory.setItem(9, team2Pane);
        inventory.setItem(10, team2Pane);
        inventory.setItem(16, team2Pane);
        inventory.setItem(17, team2Pane);
    }

    public void recalculateTeam() {
        inventory.remove(Material.PLAYER_HEAD); // Fjerner player heads sånn at det kan kalkuleres pånytt

        for (int i = 0; i < BotBows.team1.size(); i++) { // team 1
            ItemStack p = makeHeadItem(BotBows.team1.getPlayer(i).PLAYER, BotBows.team1.COLOR);
            inventory.setItem(2 + i, p);
        }
        for (int i = 0; i < BotBows.team2.size(); i++) { // team 2
            ItemStack p = makeHeadItem(BotBows.team2.getPlayer(i).PLAYER, BotBows.team2.COLOR);
            inventory.setItem(11 + i, p);
        }
    }
}
