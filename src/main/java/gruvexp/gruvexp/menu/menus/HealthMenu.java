package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.Main;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class HealthMenu extends Menu {

    boolean advancedHp = true;
    private static final ItemStack DYNAMIC_POINTS_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Dynamic points",
            ChatColor.DARK_RED + "Disabled", "If enabled, winning team gets 1", "point for each remaining hp.", "If disbabled, winning team only gets 1 point.");
    private static final ItemStack DYNAMIC_POINTS_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Dynamic points",
            ChatColor.DARK_GREEN + "Enabled", "If enabled, winning team gets 1", "point for each remaining hp.", "If disbabled, winning team only gets 1 point.");
    private static final ItemStack CUSTOM_HP_DISABLED = makeItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Custom player HP",
            ChatColor.DARK_RED + "" + ChatColor.BOLD + "Disabled", "By enabling this, each player", "can have a different amount of hp");
    private static final ItemStack CUSTOM_HP_ENABLED = makeItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Custom player HP",
            ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Enabled", "By enabling this, each player", "can have a different amount of hp");

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

        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        switch (e.getCurrentItem().getType()) { // stuff som skal gjøres når man trykker på et item
            case WHITE_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE:
                BotBowsManager.maxHP = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                updateMenu();
                break;
            case RED_STAINED_GLASS_PANE:
                if (name.equals("Dynamic points")) {
                    inventory.setItem(2, DYNAMIC_POINTS_ENABLED);
                    BotBowsManager.setDynamicScoring(true);
                } else {
                    inventory.setItem(6, CUSTOM_HP_ENABLED);
                    enableCustomHP();
                    advancedHp = true;
                }
                break;
            case LIME_STAINED_GLASS_PANE:
                if (name.equals("Dynamic points")) {
                    inventory.setItem(2, DYNAMIC_POINTS_DISABLED);
                    BotBowsManager.setDynamicScoring(false);
                } else {
                    inventory.setItem(6, CUSTOM_HP_DISABLED);
                    updateMenu();
                    advancedHp = false;
                }
                break;
            case PLAYER_HEAD:
                ItemStack head = e.getCurrentItem();
                Player p = Bukkit.getPlayer(UUID.fromString(head.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), "uuid"), PersistentDataType.STRING)));
                int slot = e.getSlot();
                int hp = head.getAmount();

                if (hp > 4) {
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
                    Main.menus.get("botbows settings 1").open(clicker);
                } else if (e.getSlot() == 23) {
                    Main.menus.get("botbows settings 3").open(clicker);
                }
        }
    }

    @Override
    public void setMenuItems() {
        disableCustomHP();
        updateMenu();

        inventory.setItem(2, DYNAMIC_POINTS_ENABLED);
        inventory.setItem(6, CUSTOM_HP_DISABLED);

        // page stuff

        inventory.setItem(21, LEFT);
        inventory.setItem(22, CLOSE);
        inventory.setItem(23, RIGHT);
    }

    public void updateMenu() { // The normal menu with a slider
        if (advancedHp) {
            for (int i = 9; i < 18; i++) {
                inventory.setItem(i, null);
            }
            for (int i = 0; i < BotBowsManager.teamBlue.size(); i++) {
                Player p = BotBowsManager.teamBlue.get(i);
                ItemStack item = makeHeadItem(p, ChatColor.BLUE);
                item.setAmount(BotBowsManager.playerMaxHP.get(p));
                inventory.setItem(i + 9, item);
            }
            for (int i = 0; i < BotBowsManager.teamRed.size(); i++) {
                Player p = BotBowsManager.teamRed.get(i);
                ItemStack item = makeHeadItem(p, ChatColor.RED);
                item.setAmount(BotBowsManager.playerMaxHP.get(p));
                inventory.setItem(17 - i, item);
            }
        } else {
            int maxHP = BotBowsManager.maxHP;

            for (int i = 0; i < 5; i++) {
                ItemStack is = makeItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE + "" + (i + 1));
                if (i < maxHP) {
                    is = makeItem(Material.PINK_STAINED_GLASS_PANE, ChatColor.RED + "" + (i + 1));
                }
                inventory.setItem(i + 11, is);
            }

            BotBowsManager.setPlayerMaxHP(maxHP);
            BotBowsManager.updatePlayerMaxHP(maxHP);
        }
    }
    
    public void enableCustomHP() {
        advancedHp = true;
        inventory.setItem(13, FILLER_GLASS);
        updateMenu();
    }
    public void disableCustomHP() {
        advancedHp = false;
        inventory.setItem(9, null);
        inventory.setItem(10, FILLER_GLASS);
        inventory.setItem(16, FILLER_GLASS);
        inventory.setItem(17, null);
        updateMenu();
    }

    public ItemStack makeHeadItem(Player p, ChatColor teamColor) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        itemMeta.setDisplayName(teamColor + p.getPlayerListName());
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "uuid"), PersistentDataType.STRING, p.getUniqueId().toString());
        itemMeta.setOwningPlayer(Bukkit.getPlayer(p.getName()));

        item.setItemMeta(itemMeta);

        return item;
    }
}
