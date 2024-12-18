package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.SettingsMenu;
import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.BotBowsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class HealthMenu extends SettingsMenu {

    private boolean customHP;
    private static final ItemStack DYNAMIC_POINTS_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Dynamic points",
            ChatColor.DARK_RED + "Disabled", "If enabled, winning team gets 1", "point for each remaining hp.", "If disbabled, winning team only gets 1 point.");
    private static final ItemStack DYNAMIC_POINTS_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Dynamic points",
            ChatColor.DARK_GREEN + "Enabled", "If enabled, winning team gets 1", "point for each remaining hp.", "If disbabled, winning team only gets 1 point.");
    private static final ItemStack CUSTOM_HP_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Custom player HP",
            ChatColor.DARK_RED + "" + ChatColor.BOLD + "Disabled", "By enabling this, each player", "can have a different amount of hp");
    private static final ItemStack CUSTOM_HP_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Custom player HP",            ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Enabled", "By enabling this, each player", "can have a different amount of hp");

    @Override    public String getMenuName() {
        return "Select player health";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        // if you click on a player then they change teams
        Player clicker = (Player) e.getWhoClicked();

        switch (e.getCurrentItem().getType()) { // stuff som skal gjøres når man trykker på et item
            case WHITE_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE:
                settings.setMaxHP(Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())));
                updateMenu();
                break;
            case RED_STAINED_GLASS_PANE:
                if (e.getCurrentItem().equals(DYNAMIC_POINTS_DISABLED)) {
                    enableDynamicPoints();
                } else if (e.getCurrentItem().equals(CUSTOM_HP_DISABLED)) { // clicked on custom hp setting
                    enableCustomHP();
                }
                break;
            case LIME_STAINED_GLASS_PANE:
                if (e.getCurrentItem().equals(DYNAMIC_POINTS_ENABLED)) {
                    disableDynamicPoints();
                } else if (e.getCurrentItem().equals(CUSTOM_HP_ENABLED)) { // clicked on custom hp setting
                    disableCustomHP();
                }
                break;
            case PLAYER_HEAD:
                ItemStack head = e.getCurrentItem();
                Player p = Bukkit.getPlayer(UUID.fromString(head.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), "uuid"), PersistentDataType.STRING)));
                BotBowsPlayer bp = BotBows.getBotBowsPlayer(p);
                int slot = e.getSlot();
                int maxHP = head.getAmount();

                if (maxHP > 9) {
                    maxHP += 5;
                    if (maxHP > 20) {
                        maxHP = 1;
                    }
                } else {
                    maxHP += 1;
                }

                head.setAmount(maxHP);
                inventory.setItem(slot, head);
                bp.setMaxHP(maxHP);
                break;
            case BARRIER:
                clicker.closeInventory();
                break;
            case LIGHT_BLUE_STAINED_GLASS_PANE:
                if (e.getSlot() == 21) {
                    BotBows.teamsMenu.open(clicker);
                } else if (e.getSlot() == 23) {
                    BotBows.winThresholdMenu.open(clicker);
                }
        }
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems(); // initer settings
        disableCustomHP();
        enableDynamicPoints();

        // page stuff
        inventory.setItem(21, LEFT);
        inventory.setItem(22, CLOSE);
        inventory.setItem(23, RIGHT);
    }

    public void updateMenu() {
        if (customHP) { // each player can have their own health
            for (int i = 9; i < 18; i++) {
                inventory.setItem(i, null);
            }
            for (int i = 0; i < settings.team1.size(); i++) {
                BotBowsPlayer p = settings.team1.getPlayer(i);
                ItemStack item = makeHeadItem(p.PLAYER, settings.team1.COLOR);
                item.setAmount(p.getMaxHP());
                inventory.setItem(i + 9, item);
            }
            for (int i = 0; i < settings.team2.size(); i++) {
                BotBowsPlayer p = settings.team2.getPlayer(i);
                ItemStack item = makeHeadItem(p.PLAYER, settings.team2.COLOR);
                item.setAmount(p.getMaxHP());
                inventory.setItem(17 - i, item);
            }
        } else { // The normal menu with a slider
            int maxHP = settings.getMaxHP();

            for (int i = 0; i < 5; i++) {
                ItemStack is = makeItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE + String.valueOf(i + 1));
                if (i < maxHP) {
                    is = makeItem(Material.PINK_STAINED_GLASS_PANE, ChatColor.RED + String.valueOf(i + 1));
                }
                inventory.setItem(i + 11, is);
            }
            settings.setMaxHP(maxHP);
        }
    }
    
    public void enableCustomHP() {
        customHP = true;
        inventory.setItem(6, CUSTOM_HP_ENABLED);
        inventory.setItem(13, FILLER_GLASS);
        updateMenu();
    }

    public void disableCustomHP() {
        customHP = false;
        inventory.setItem(6, CUSTOM_HP_DISABLED);
        inventory.setItem(9, null);
        inventory.setItem(10, FILLER_GLASS);
        inventory.setItem(16, FILLER_GLASS);
        inventory.setItem(17, null);
        updateMenu();
    }

    public void enableDynamicPoints() {
        inventory.setItem(2, DYNAMIC_POINTS_ENABLED);
        settings.setDynamicScoring(true);
    }

    public void disableDynamicPoints() {
        inventory.setItem(2, DYNAMIC_POINTS_DISABLED);
        settings.setDynamicScoring(false);
    }
}
