package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.menu.SettingsMenu;
import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.hazard.EarthquakeHazard;
import gruvexp.gruvexp.twtClassic.hazard.HazardChance;
import gruvexp.gruvexp.twtClassic.hazard.StormHazard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HazardMenu extends SettingsMenu {

    private static final Map<String, HazardChance> PERCENT_MAP = new HashMap<>();
    private static final List<String> PERCENT = List.of("DISABLED", "5%", "10%", "25%", "50%", "ALWAYS");

    static {
        PERCENT_MAP.put("DISABLED", HazardChance.DISABLED);
        PERCENT_MAP.put("5%", HazardChance.FIVE);
        PERCENT_MAP.put("10%", HazardChance.TEN);
        PERCENT_MAP.put("25%", HazardChance.TWENTY_FIVE);
        PERCENT_MAP.put("50%", HazardChance.FIFTY);
        PERCENT_MAP.put("ALWAYS", HazardChance.ALWAYS);
    }
    private StormHazard stormHazard;
    private EarthquakeHazard earthquakeHazard = BotBows.settings.earthquakeHazard;

    private ItemStack getStormItem() {
        ItemStack item;
        String[] loreDesc = new String[] {"When there is a storm, you will get hit by", "lightning if you stand in dirext exposure", "to the sky for more than 5 seconds"};
        if (stormHazard.getHazardChance() == HazardChance.DISABLED) {
            item = makeItem(Material.RED_STAINED_GLASS_PANE, STR."\{ChatColor.RED}Storms", STR."\{ChatColor.DARK_RED}\{ChatColor.BOLD}Disabled",
                    "If enabled, x% of rounds will have storms.", loreDesc[0], loreDesc[1], loreDesc[2]);
        } else {
            item = makeItem(Material.LIME_STAINED_GLASS_PANE, STR."\{ChatColor.GREEN}Storms", STR."\{ChatColor.DARK_GREEN}\{ChatColor.BOLD}Enabled",
                    STR."\{stormHazard.getHazardChance().getPercent()}% of rounds will have storms.", loreDesc[0], loreDesc[1], loreDesc[2]);
        }
        return item;
    }

    private ItemStack getEarthquakeItem() {
        ItemStack item;
        String[] loreDesc = new String[] {"When there is an earthwuake, you will get hit by", "stones if you go underground", "for more than 5 seconds"};
        if (earthquakeHazard.getHazardChance() == HazardChance.DISABLED) {
            item = makeItem(Material.RED_STAINED_GLASS_PANE, STR."\{ChatColor.RED}Earthquakes", STR."\{ChatColor.DARK_RED}\{ChatColor.BOLD}Disabled",
                    "If enabled, x% of rounds will have earthquakes.", loreDesc[0], loreDesc[1], loreDesc[2]);
        } else {
            item = makeItem(Material.LIME_STAINED_GLASS_PANE, STR."\{ChatColor.GREEN}Earthquakes", STR."\{ChatColor.DARK_GREEN}\{ChatColor.BOLD}Enabled",
                    STR."\{earthquakeHazard.getHazardChance().getPercent()}% of rounds will have earthquakes.", loreDesc[0], loreDesc[1], loreDesc[2]);
        }
        return item;
    }

    @Override
    public String getMenuName() {
        return "Hazards";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {
            case WHITE_STAINED_GLASS_PANE, CYAN_STAINED_GLASS_PANE, BROWN_STAINED_GLASS_PANE -> {
                String s = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                if (e.getSlot() < 9) {
                    if (stormHazard.getHazardChance() != PERCENT_MAP.get(s)) {
                        stormHazard.setHazardChance(PERCENT_MAP.get(s));
                        updateStormBar();
                    }
                } else if (e.getSlot() < 18) {
                    if (earthquakeHazard.getHazardChance() != PERCENT_MAP.get(s)) {
                        earthquakeHazard.setHazardChance(PERCENT_MAP.get(s));
                        updateEarthquakeBar();
                    }
                }
            }
            case BARRIER -> clicker.closeInventory();
            case LIGHT_BLUE_STAINED_GLASS_PANE -> {
                if (e.getSlot() == 21) {
                    BotBows.winThresholdMenu.open(clicker);
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();
        stormHazard = settings.stormHazard;
        earthquakeHazard = settings.earthquakeHazard;
        updateStormBar();
        updateEarthquakeBar();

        // page stuff
        inventory.setItem(22, LEFT);
        inventory.setItem(23, CLOSE);
    }

    void updateStormBar() { // Hvordan menu skal se ut når storm mode er enabla
        inventory.setItem(0, getStormItem());
        for (int i = 0; i < PERCENT.size(); i++) {
            ItemStack item;
            if (PERCENT_MAP.get(PERCENT.get(i)).getPercent() > stormHazard.getHazardChance().getPercent()) {
                item = makeItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE + PERCENT.get(i));
            } else {
                item = makeItem(Material.CYAN_STAINED_GLASS_PANE, ChatColor.AQUA + PERCENT.get(i));
            }
            inventory.setItem(i + 2, item);
        }
    }

    void updateEarthquakeBar() { // Hvordan menu skal se ut når storm mode er enabla
        inventory.setItem(9, getEarthquakeItem());
        for (int i = 0; i < PERCENT.size(); i++) {
            ItemStack item;
            if (PERCENT_MAP.get(PERCENT.get(i)).getPercent() > earthquakeHazard.getHazardChance().getPercent()) {
                item = makeItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE + PERCENT.get(i));
            } else {
                item = makeItem(Material.BROWN_STAINED_GLASS_PANE, ChatColor.GOLD + PERCENT.get(i));
            }
            inventory.setItem(i + 11, item);
        }
    }
}
