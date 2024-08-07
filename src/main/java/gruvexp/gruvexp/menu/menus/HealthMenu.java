package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class HealthMenu extends Menu {

    private boolean customHP;
    private static final ItemStack DYNAMIC_POINTS_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, STR."\{ChatColor.RED}Dynamic points",
            STR."\{ChatColor.DARK_RED}Disabled", "If enabled, winning team gets 1", "point for each remaining hp.", "If disbabled, winning team only gets 1 point.");
    private static final ItemStack DYNAMIC_POINTS_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, STR."\{ChatColor.GREEN}Dynamic points",
            STR."\{ChatColor.DARK_GREEN}Enabled", "If enabled, winning team gets 1", "point for each remaining hp.", "If disbabled, winning team only gets 1 point.");
    private static final ItemStack CUSTOM_HP_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, STR."\{ChatColor.RED}Custom player HP",
            STR."\{ChatColor.DARK_RED}\{ChatColor.BOLD}Disabled", "By enabling this, each player", "can have a different amount of hp");
    private static final ItemStack CUSTOM_HP_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, STR."\{ChatColor.GREEN}Custom player HP",
            STR."\{ChatColor.DARK_GREEN}\{ChatColor.BOLD}Enabled", "By enabling this, each player", "can have a different amount of hp");

    @Override
    public String getMenuName() {
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
                BotBowsManager.maxHP = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
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
                int slot = e.getSlot();
                int hp = head.getAmount();

                if (hp > 9) {
                    hp += 5;
                    if (hp > 20) {
                        hp = 1;
                    }
                } else {
                    hp += 1;
                }

                head.setAmount(hp);
                inventory.setItem(slot, head);
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp * 2);
                p.setHealth(hp * 2);
                BotBowsManager.playerMaxHP.put(p, hp);
                break;
            case BARRIER:
                clicker.closeInventory();
                break;
            case LIGHT_BLUE_STAINED_GLASS_PANE:
                if (e.getSlot() == 21) {
                    Main.menus.get("select teams").open(clicker);
                } else if (e.getSlot() == 23) {
                    Main.menus.get("win threshold").open(clicker);
                }
        }
    }

    @Override
    public void setMenuItems() {
        disableCustomHP();
        enableDynamicPoints();
        updateMenu();

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
            for (int i = 0; i < BotBowsManager.team1.size(); i++) {
                Player p = BotBowsManager.team1.getPlayer(i);
                ItemStack item = makeHeadItem(p, BotBowsManager.team1.COLOR);
                item.setAmount(BotBowsManager.playerMaxHP.get(p));
                inventory.setItem(i + 9, item);
            }
            for (int i = 0; i < BotBowsManager.team2.size(); i++) {
                Player p = BotBowsManager.team2.getPlayer(i);
                ItemStack item = makeHeadItem(p, BotBowsManager.team2.COLOR);
                item.setAmount(BotBowsManager.playerMaxHP.get(p));
                inventory.setItem(17 - i, item);
            }
        } else { // The normal menu with a slider
            int maxHP = BotBowsManager.maxHP;

            for (int i = 0; i < 5; i++) {
                ItemStack is = makeItem(Material.WHITE_STAINED_GLASS_PANE, STR."\{ChatColor.WHITE}\{i + 1}");
                if (i < maxHP) {
                    is = makeItem(Material.PINK_STAINED_GLASS_PANE, STR."\{ChatColor.RED}\{i + 1}");
                }
                inventory.setItem(i + 11, is);
            }

            BotBowsManager.setPlayerMaxHP(maxHP);
            BotBowsManager.updatePlayerMaxHP(maxHP);
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
        BotBowsManager.setDynamicScoring(true);
    }

    public void disableDynamicPoints() {
        inventory.setItem(2, DYNAMIC_POINTS_DISABLED);
        BotBowsManager.setDynamicScoring(false);
    }
}
