package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import gruvexp.gruvexp.twtClassic.BotBowsMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SelectMapMenu extends Menu {

    public static final ItemStack ROYAL_ARENA = makeItem(Material.SLIME_BALL, STR."\{ChatColor.GRAY}Royal Arena",
            STR."\{ChatColor.BLUE}Blaud\{ChatColor.WHITE} vs \{ChatColor.RED}Sauce",
            "A flat arena with modern royal style",
            "Has a huge cave room underground");
    public static final ItemStack ICY_RAVINE = makeItem(Material.SPRUCE_SAPLING, STR."\{ChatColor.AQUA}Icy Ravine",
            STR."\{ChatColor.LIGHT_PURPLE}Graut\{ChatColor.WHITE} vs \{ChatColor.GREEN}Wacky",
            "A flat arena in a spruce forest with ice spikes and igloos",
            "Has a huge ravine in the middle and many caves underground");

    @Override
    public String getMenuName() {
        return "Select map";
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {
            case SLIME_BALL -> BotBowsManager.setMap(BotBowsMap.BLAUD_VS_SAUCE);
            case SPRUCE_SAPLING -> BotBowsManager.setMap(BotBowsMap.GRAUT_VS_WACKY);
            case BARRIER -> clicker.closeInventory();
            case LIGHT_BLUE_STAINED_GLASS_PANE -> Main.menus.get("select teams").open(clicker);
        }
    }

    @Override
    public void setMenuItems() {

        inventory.setItem(3, ROYAL_ARENA);
        inventory.setItem(5, ICY_RAVINE);
        // page stuff
        inventory.setItem(13, CLOSE);
        inventory.setItem(14, RIGHT);
    }
}
