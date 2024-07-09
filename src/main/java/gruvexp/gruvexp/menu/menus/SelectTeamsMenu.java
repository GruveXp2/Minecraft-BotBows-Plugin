package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import gruvexp.gruvexp.twtClassic.botbowsTeams.BotBowsTeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class SelectTeamsMenu extends Menu {
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
                BotBowsTeam team = BotBowsManager.getTeam(p);
                team.moveToTeam(p, BotBowsManager.getOppositeTeam(team));
                recalculateTeam();
                ((HealthMenu) Main.menus.get("health")).updateMenu(); // pga teammembers endres må health settings oppdateres pga det er basert på farger
            }
            case BARRIER -> clicker.closeInventory();
            case LIGHT_BLUE_STAINED_GLASS_PANE -> {
                if (e.getSlot() == 21) {
                    Main.menus.get("select map").open(clicker);
                } else if (e.getSlot() == 23) {
                    Main.menus.get("health").open(clicker);
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        setColoredGlassPanes();
        // page stuff
        inventory.setItem(22, CLOSE);
        inventory.setItem(23, RIGHT);
    }

    public void setColoredGlassPanes() {
        ItemStack team1Pane = makeItem(BotBowsManager.team1.getglassPane(), STR."\{BotBowsManager.team1.COLOR}Team \{BotBowsManager.team1}");
        ItemStack team2Pane = makeItem(BotBowsManager.team2.getglassPane(), STR."\{BotBowsManager.team2.COLOR}Team \{BotBowsManager.team2}");
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

        for (int i = 0; i < BotBowsManager.team1.size(); i++) { // team 1
            ItemStack p = makeHeadItem(BotBowsManager.team1.getPlayer(i), BotBowsManager.team1.COLOR);
            inventory.setItem(2 + i, p);
        }
        for (int i = 0; i < BotBowsManager.team2.size(); i++) { // team 2
            ItemStack p = makeHeadItem(BotBowsManager.team2.getPlayer(i), BotBowsManager.team2.COLOR);
            inventory.setItem(11 + i, p);
        }
    }
}
