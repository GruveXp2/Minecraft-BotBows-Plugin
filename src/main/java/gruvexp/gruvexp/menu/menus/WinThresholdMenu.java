package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WinThresholdMenu extends Menu {
    @Override
    public String getMenuName() {
        return "Select win threshold";
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {
            case RED_STAINED_GLASS_PANE:
                if (BotBowsManager.winThreshold > 10) {
                    BotBowsManager.winThreshold -= 10;
                } else {
                    BotBowsManager.winThreshold = -1;
                }
                updateMenu();
                break;
            case PINK_STAINED_GLASS_PANE:
                if (BotBowsManager.winThreshold > 1) {
                    BotBowsManager.winThreshold -= 1;
                } else {
                    BotBowsManager.winThreshold = -1;
                }
                updateMenu();
                break;
            case LIME_STAINED_GLASS_PANE:
                if (BotBowsManager.winThreshold == -1) {
                    BotBowsManager.winThreshold = 1;
                } else {
                    BotBowsManager.winThreshold += 1;
                }
                updateMenu();
                break;
            case GREEN_STAINED_GLASS_PANE:
                BotBowsManager.winThreshold += 10;
                updateMenu();
                break;
            case BARRIER:
                clicker.closeInventory();
                break;
            case LIGHT_BLUE_STAINED_GLASS_PANE:
                if (e.getSlot() == 12) {
                    Main.menus.get("botbows settings 2").open(clicker);
                } else {
                    Main.menus.get("botbows settings 4").open(clicker);
                }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack sub_10 = makeItem(Material.RED_STAINED_GLASS_PANE, "-10");
        ItemStack sub_1= makeItem(Material.PINK_STAINED_GLASS_PANE, "-1");
        ItemStack add_1 = makeItem(Material.LIME_STAINED_GLASS_PANE, "+1");
        ItemStack add_10 = makeItem(Material.GREEN_STAINED_GLASS_PANE, "+10");
        ItemStack is = makeItem(Material.BLUE_TERRACOTTA, ChatColor.BLUE + "Win score threshold");
        is.setAmount(BotBowsManager.winThreshold);

        inventory.setItem(2, sub_10);
        inventory.setItem(3, sub_1);
        inventory.setItem(4, is);
        inventory.setItem(5, add_1);
        inventory.setItem(6, add_10);

        // page stuff
        inventory.setItem(12, LEFT);
        inventory.setItem(13, CLOSE);
        inventory.setItem(14, RIGHT);

        inventory.setItem(0, FILLER_GLASS);
        inventory.setItem(1, FILLER_GLASS);
        inventory.setItem(7, FILLER_GLASS);
        inventory.setItem(8, FILLER_GLASS);
    }

    private void updateMenu() {
        ItemStack is;
        if (BotBowsManager.winThreshold > 0) {
            is = makeItem(Material.BLUE_TERRACOTTA, ChatColor.BLUE + "Win score threshold");
            is.setAmount(BotBowsManager.winThreshold);
        } else {
            is = makeItem(Material.YELLOW_TERRACOTTA, ChatColor.YELLOW + "Infinite rounds");
        }
        inventory.setItem(4, is);
    }
}
