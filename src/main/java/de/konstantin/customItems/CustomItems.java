package de.konstantin.customItems;

import de.konstantin.customItems.commands.CustomItemsCommand;
import de.konstantin.customItems.drill.DrillListener;
import de.konstantin.customItems.drill.DrillManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomItems extends JavaPlugin {
    private DrillManager drillManager;

    @Override
    public void onEnable() {
        this.drillManager = new DrillManager();
        getServer().getPluginManager().registerEvents(new DrillListener(drillManager), this);
        getCommand("customitems").setExecutor(new CustomItemsCommand(drillManager));
    }

    @Override
    public void onDisable() {
    }

    public DrillManager getDrillManager() {
        return drillManager;
    }
}