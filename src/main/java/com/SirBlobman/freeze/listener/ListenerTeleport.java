package com.SirBlobman.freeze.listener;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.SirBlobman.api.configuration.ConfigurationManager;
import com.SirBlobman.freeze.FreezePlugin;
import com.SirBlobman.freeze.manager.FreezeManager;

public final class ListenerTeleport extends FreezeListener {
    public ListenerTeleport(FreezePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onMove(PlayerTeleportEvent e) {
        if(isDisabled()) return;
        FreezeManager freezeManager = getFreezeManager();

        Player player = e.getPlayer();
        if(!freezeManager.isFrozen(player)) return;

        Location fromLocation = e.getFrom();
        Location toLocation = e.getTo();
        if(toLocation == null || isSimilar(fromLocation, toLocation)) return;

        e.setCancelled(true);
        sendFrozenMessage(player);
    }

    private boolean isDisabled() {
        FreezePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        return !configuration.getBoolean("prevent-teleport", true);
    }

    private boolean isSimilar(Location location1, Location location2) {
        double x1 = location1.getX(), y1 = location1.getY(), z1 = location1.getZ();
        double x2 = location2.getX(), y2 = location2.getY(), z2 = location2.getZ();
        return (x1 == x2 && z1 == z2 && y2 <= y1);
    }
}