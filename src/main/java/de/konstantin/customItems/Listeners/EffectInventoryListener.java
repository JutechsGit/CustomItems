package de.konstantin.customItems.Listeners;


import de.konstantin.customItems.drill.DrillManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;

public class EffectInventoryListener implements Listener {
    private final DrillManager drillManager;

    public EffectInventoryListener(DrillManager drillManager) {
        this.drillManager = drillManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals("§bWähle einen Effekt")) return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // Handle Drill effect
        if (clicked.getType() == Material.IRON_PICKAXE &&
                clicked.getItemMeta() != null &&
                "§aDrill-Effekt".equals(clicked.getItemMeta().getDisplayName())) {

            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (!drillManager.isPickaxe(heldItem.getType())) {
                player.sendMessage("§cDu musst eine Spitzhacke in der Hand halten!");
                return;
            }

            ItemStack modified = drillManager.createDrill(heldItem);
            if (modified == null) {
                player.sendMessage("§cFehler beim Anwenden des Effekts.");
                return;
            }

            player.getInventory().setItemInMainHand(modified);
            player.sendMessage("§aDrill-Effekt angewendet!");
            player.closeInventory();
        }
    }
}
