package gruvexp.gruvexp;

import gruvexp.gruvexp.commands.*;
import gruvexp.gruvexp.listeners.*;
import gruvexp.gruvexp.menu.Menu;
import gruvexp.gruvexp.menu.menus.*;
import gruvexp.gruvexp.twtClassic.BotBowsManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    public static HashMap<String, Menu> menus = new HashMap<>();
    private static Main instance;
    public static World WORLD;
    public static Main getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() { // ADD FUNNEY SCRACHT SOUNDEFFECTS OG PIOM LYD reSOURCEPACC
        instance = this;
        Bukkit.getLogger().info("BotBows plugin enabled!");
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new HitListener(), this);
        getServer().getPluginManager().registerEvents(new movementListener(), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getServer().getPluginManager().registerEvents(new ShiftListener(), this);
        getServer().getPluginManager().registerEvents(new SwitchSpectator(), this);

        getCommand("menu").setExecutor(new MenuCommand()); //når man skriver inn /menu så blir MenuCommand() runna
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
        getCommand("stopgame").setExecutor(new StopCommand());
        WORLD = Bukkit.getWorld("BotBows (S2E1)");
        BotBowsManager.armorInit();
        BotBowsManager.spawnInit();
        MenuInit();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling BotBows plugin");
    }

    public static void MenuInit() {
        menus.put("game menu", new GameMenu());
        menus.put("botbows settings 1", new SettingsMenuPage1());
        menus.put("botbows settings 2", new SettingsMenuPage2());
        menus.put("botbows settings 3", new SettingsMenuPage3());
        menus.put("botbows settings 4", new SettingsMenuPage4());
    }
}
