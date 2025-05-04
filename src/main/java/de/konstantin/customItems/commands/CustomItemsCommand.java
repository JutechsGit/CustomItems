package de.konstantin.customItems.commands;

import de.konstantin.customItems.drill.DrillManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemsCommand implements CommandExecutor {
    private final DrillManager drillManager;

    public CustomItemsCommand(DrillManager drillManager) {
        this.drillManager = drillManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden!");
            return true;
        }

        if (!player.hasPermission("customitems.use")) {
            player.sendMessage("§cKeine Berechtigung.");
            return true;
        }

        openEffectGUI(player);
        return true;
    }

    private void openEffectGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "§bWähle einen Effekt");

        // Drill effect item (iron pickaxe)
        ItemStack drillIcon = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = drillIcon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aDrill-Effekt");
            meta.setLore(java.util.List.of("§7Fügt deiner Spitzhacke", "§7einen Bohr-Effekt hinzu."));
            drillIcon.setItemMeta(meta);
        }

        gui.setItem(4, drillIcon); // Centered

        player.openInventory(gui);
    }
}
