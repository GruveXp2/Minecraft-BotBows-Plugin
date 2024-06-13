package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class SettingsMenuPage1 extends Menu {
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
                if (BotBowsManager.teamBlue.contains(p)) {
                    BotBowsManager.teamBlue.remove(p);
                    BotBowsManager.teamRed.add(p);
                } else {
                    BotBowsManager.teamRed.remove(p);
                    BotBowsManager.teamBlue.add(p);
                }
                recalculateTeam();
                Main.Id2Menu.get("botbows settings 2").callInternalFunction(0);
            }
            case BARRIER -> clicker.closeInventory();
            case LIGHT_BLUE_STAINED_GLASS_PANE -> Main.Id2Menu.get("botbows settings 2").open(clicker);
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack blue = makeItem(Material.BLUE_STAINED_GLASS_PANE, ChatColor.BLUE + "Team Blue");
        ItemStack red = makeItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Team Red");
        inventory.setItem(0, blue);
        inventory.setItem(1, blue);
        inventory.setItem(7, blue);
        inventory.setItem(8, blue);
        inventory.setItem(9, red);
        inventory.setItem(10, red);
        inventory.setItem(16, red);
        inventory.setItem(17, red);

        // page stuff
        inventory.setItem(22, CLOSE);
        inventory.setItem(23, RIGHT);
    }

    @Override
    public void callInternalFunction(int i) {
        if (i == 0) {
            recalculateTeam();
        }
    }

    public void recalculateTeam() {
        inventory.remove(Material.PLAYER_HEAD); // Fjerner player heads sånn at det kan kalkuleres pånytt

        for (int i = 0; i < BotBowsManager.teamBlue.size(); i++) { // team blue
            ItemStack p = makeHeadItem(BotBowsManager.teamBlue.get(i), ChatColor.BLUE);
            inventory.setItem(2 + i, p);
        }
        for (int i = 0; i < BotBowsManager.teamRed.size(); i++) { // team red
            ItemStack p = makeHeadItem(BotBowsManager.teamRed.get(i), ChatColor.RED);
            inventory.setItem(11 + i, p);
        }
    }
    public ItemStack makeHeadItem(Player p, ChatColor teamcolor) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        itemMeta.setDisplayName(teamcolor + p.getPlayerListName());
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "uuid"), PersistentDataType.STRING, p.getUniqueId().toString());
        itemMeta.setOwningPlayer(Bukkit.getPlayer(p.getName()));

        item.setItemMeta(itemMeta);

        return item;
    }
}
