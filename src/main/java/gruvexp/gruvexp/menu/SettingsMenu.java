package gruvexp.gruvexp.menu;

import gruvexp.gruvexp.twtClassic.BotBows;
import gruvexp.gruvexp.twtClassic.Settings;

public abstract class SettingsMenu extends Menu {

    protected Settings settings;

    @Override
    public void setMenuItems() {
        this.settings = BotBows.settings;
    }
}
