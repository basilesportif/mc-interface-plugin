package org.kinode;

import org.kinode.managers.PluginMgr;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class PluginInstance extends JavaPlugin implements Listener {

    private static PluginInstance instance;
    private PluginMgr manager;
    private Location prevLocation;

    @Override
    public void onEnable() {
        getLogger().info("Movement logging enabled. TIMTIME.");
        Bukkit.getPluginManager().registerEvents(this, this);
        instance = this;
        this.manager = new PluginMgr();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // here
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location toLocation = event.getTo();
        // don't print pitch & yaw
        if (prevLocation != null && toLocation.getX() == prevLocation.getX() && toLocation.getY() == prevLocation.getY()
                && toLocation.getZ() == prevLocation.getZ()) {
            return;
        }
        prevLocation = toLocation;
        getLogger().info(event.getPlayer().getName() + " moved to X: " + toLocation.getX() + " Y: " + toLocation.getY()
                + " Z: " + toLocation.getZ());
        if (Math.abs(toLocation.getX()) > 100 || Math.abs(toLocation.getY()) > 100
                || Math.abs(toLocation.getZ()) > 100) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Movement outside allowed bounds is not permitted.");
        }
    }

    /**
     * @return The Minecraft Plugin Instance
     */
    public static PluginInstance getInstance() {
        return instance;
    }

    /**
     * @return The Main Manager (PluginMgr by default)
     */
    public PluginMgr getMgr() {
        return manager;
    }

    /**
     * Returns true or false depending on the value of the debug config option in
     * config.yml if it exists.
     * By default, it returns false.
     * 
     * @return true or false
     */
    public boolean isDebugMode() {
        boolean isMgrReady = manager != null && manager.isInitialized();
        boolean isConfigMgrReady = isMgrReady && manager.getConfigMgr() != null
                && manager.getConfigMgr().isInitialized();

        if (isConfigMgrReady && manager.getConfigMgr().get("debug") != null
                && manager.getConfigMgr().get("debug") instanceof Boolean)
            return (boolean) manager.getConfigMgr().get("debug");
        return false;
    }
}
