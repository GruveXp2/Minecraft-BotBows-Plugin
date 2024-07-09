package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StormModeMenu extends Menu {

    private static final ArrayList<String> PERCENT = new ArrayList<>(List.of("5%", "10%", "25%", "50%", "100%")); // liste over strings som viser hvor mange % sjanse for storm, tar inn int

    private static final ItemStack STORM_MODE_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, STR."\{ChatColor.RED}Storm mode",
            STR."\{ChatColor.DARK_RED}\{ChatColor.BOLD}Disabled", "If enabled, 10% of rounds will have storms.",
            "When there is a storm, you will get hit by", "lightning if you stand in dirext exposure", "to the sky for more than 5 seconds");
    private static final ItemStack STORM_MODE_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, STR."\{ChatColor.GREEN}Storm mode",
            STR."\{ChatColor.DARK_GREEN}\{ChatColor.BOLD}Enabled", "If enabled, 10% of rounds will have storms.",
            "When there is a storm, you will get hit by", "lightning if you stand in dirext exposure", "to the sky for more than 5 seconds");

    @Override
    public String getMenuName() {
        return "Storm mode";
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {
            case WHITE_STAINED_GLASS_PANE, CYAN_STAINED_GLASS_PANE:
                String s = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                if (BotBowsManager.stormFrequency != PERCENT.indexOf(s)) {
                    BotBowsManager.stormFrequency = PERCENT.indexOf(s);
                    stormEnabled();
                }
                break;
            case RED_STAINED_GLASS_PANE:
                stormEnabled();
                BotBowsManager.stormMode = true;
                break;
            case LIME_STAINED_GLASS_PANE:
                stormDisabled();
                BotBowsManager.stormMode = false;
                break;
            case BARRIER:
                clicker.closeInventory();
                break;
            case LIGHT_BLUE_STAINED_GLASS_PANE:
                if (e.getSlot() == 12) {
                    Main.menus.get("win threshold").open(clicker);
                }
        }
    }

    @Override
    public void setMenuItems() {

        stormDisabled();

        // page stuff
        inventory.setItem(12, LEFT);
        inventory.setItem(13, CLOSE);
    }

    void stormDisabled() { // Hvordan menu skal se ut når storm mode er disabla
        inventory.setItem(0, STORM_MODE_DISABLED);
        for (int i = 0; i < 5; i++) {
            inventory.setItem(i + 2, FILLER_GLASS);
        }
    }

    void stormEnabled() { // Hvordan menu skal se ut når storm mode er enabla
        inventory.setItem(0, STORM_MODE_ENABLED);
        for (int i = 0; i < 5; i++) {
            ItemStack is;
            if (i > BotBowsManager.stormFrequency) {
                is = makeItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE + PERCENT.get(i));
            } else {
                is = makeItem(Material.CYAN_STAINED_GLASS_PANE, ChatColor.AQUA + PERCENT.get(i));
            }
            inventory.setItem(i + 2, is);
        }

    }
}
