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

public class SettingsMenuPage4 extends Menu {

    private static final ArrayList<String> PERCENT = new ArrayList<>(List.of("5%", "10%", "25%", "50%", "100%")); // liste over strings som viser hvor mange % sjanse for storm, tar inn int

    private static final ItemStack STORM_MODE_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Storm mode",
            ChatColor.DARK_RED + "" + ChatColor.BOLD + "Disabled", "If enabled, 10% of rounds will have storms.",
            "When there is a storm, you will get hit by", "lightning if you stand in dirext exposure", "to the sky for more than 5 seconds");
    private static final ItemStack STORM_MODE_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Storm mode",
            ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Enabled", "If enabled, 10% of rounds will have storms.",
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
                    Main.Id2Menu.get("botbows settings 3").open(clicker);
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

    void stormDisabled() { // Hvordan menu skal se ut når storm mode er disabled
        inventory.setItem(0, STORM_MODE_DISABLED);
        for (int i = 0; i < 5; i++) {
            inventory.setItem(i + 2, FILLER_GLASS);
        }
    }

    void stormEnabled() { // Hvordan menu skal se ut når storm mode er enabled
        inventory.setItem(0, STORM_MODE_ENABLED);
        for (int i = 0; i < 5; i++) {
            ItemStack is;
            if (i > BotBowsManager.stormFrequency) {
                is = makeItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE + "" + PERCENT.get(i));
            } else {
                is = makeItem(Material.CYAN_STAINED_GLASS_PANE, ChatColor.AQUA + "" + PERCENT.get(i));
            }
            inventory.setItem(i + 2, is);
        }

    }

    @Override
    public void callInternalFunction(int i) {}
}
