package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.menu.SettingsMenu;
import gruvexp.gruvexp.twtClassic.BotBows;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WinThresholdMenu extends SettingsMenu {
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
                settings.changeWinThreshold(-10);
                break;
            case PINK_STAINED_GLASS_PANE:
                settings.changeWinThreshold(-1);
                break;
            case LIME_STAINED_GLASS_PANE:
                settings.changeWinThreshold(1);
                updateMenu();
                break;
            case GREEN_STAINED_GLASS_PANE:
                settings.changeWinThreshold(10);
                break;
            case BARRIER:
                clicker.closeInventory();
                break;
            case LIGHT_BLUE_STAINED_GLASS_PANE:
                if (e.getSlot() == 12) {
                    BotBows.teamsMenu.open(clicker);
                } else {
                    BotBows.hazardMenu.open(clicker);
                }
        }
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();
        ItemStack sub10 = makeItem(Material.RED_STAINED_GLASS_PANE, "-10");
        ItemStack sub1= makeItem(Material.PINK_STAINED_GLASS_PANE, "-1");
        ItemStack add1 = makeItem(Material.LIME_STAINED_GLASS_PANE, "+1");
        ItemStack add10 = makeItem(Material.GREEN_STAINED_GLASS_PANE, "+10");
        ItemStack is = makeItem(Material.BLUE_TERRACOTTA, ChatColor.BLUE + "Win score threshold");
        is.setAmount(settings.getWinThreshold());

        inventory.setItem(2, sub10);
        inventory.setItem(3, sub1);
        inventory.setItem(4, is);
        inventory.setItem(5, add1);
        inventory.setItem(6, add10);

        // page stuff
        inventory.setItem(12, LEFT);
        inventory.setItem(13, CLOSE);
        inventory.setItem(14, RIGHT);

        inventory.setItem(0, FILLER_GLASS);
        inventory.setItem(1, FILLER_GLASS);
        inventory.setItem(7, FILLER_GLASS);
        inventory.setItem(8, FILLER_GLASS);
    }
    public void updateMenu() {
        ItemStack is;
        if (settings.getWinThreshold() > 0) {
            is = makeItem(Material.BLUE_TERRACOTTA, ChatColor.BLUE + "Win score threshold");
            is.setAmount(settings.getWinThreshold());
        } else {
            is = makeItem(Material.YELLOW_TERRACOTTA, ChatColor.YELLOW + "Infinite rounds", "Run /stopgame to stop the game");
        }
        inventory.setItem(4, is);
    }
}
