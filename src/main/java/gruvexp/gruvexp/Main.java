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
    private static Main PLUGIN;
    public static World WORLD;
    public static Main getPlugin() {
        return PLUGIN;
    }

    @Override
    public void onEnable() {
        PLUGIN = this;
        Bukkit.getLogger().info("BotBows plugin enabled!");
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new HitListener(), this);
        getServer().getPluginManager().registerEvents(new movementListener(), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getServer().getPluginManager().registerEvents(new ShiftListener(), this);
        getServer().getPluginManager().registerEvents(new SwitchSpectator(), this);

        getCommand("menu").setExecutor(new MenuCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
        getCommand("stopgame").setExecutor(new StopGameCommand());
        WORLD = Bukkit.getWorld("BotBows (S2E1)");
        BotBowsManager.armorInit();
        MenuInit();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling BotBows plugin");
    }

    public static void MenuInit() {
        menus.put("game menu", new GameMenu());
        menus.put("select map", new SelectMapMenu());
        menus.put("select teams", new SelectTeamsMenu());
        menus.put("health", new HealthMenu());
        menus.put("win threshold", new WinThresholdMenu());
        menus.put("storm mode", new StormModeMenu());
    }
}
