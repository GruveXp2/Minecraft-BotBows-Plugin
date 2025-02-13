package gruvexp.gruvexp.menu.menus;

import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.twtClassic.BotBows;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GameMenu extends Menu {
    @Override
    public String getMenuName() {
        return "Game Menu";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem().getType() == Material.BOW) {
            BotBows.joinGame(p);
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack botbows = makeItem(Material.BOW, "BotBows Classic", "The classic game of BotBows");
        inventory.setItem(1, botbows);
        setFillerGlass();
    }
}
