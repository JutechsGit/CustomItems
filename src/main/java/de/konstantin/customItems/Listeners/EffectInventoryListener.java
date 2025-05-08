package de.konstantin.customItems.Listeners;


import de.konstantin.customItems.drill.DrillManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        if (!event.getView().title().equals(Component.text("Wähle einen Effekt").color(NamedTextColor.DARK_PURPLE))) return;

        event.setCancelled(true);

        if (event.getSlot() == 4) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (!drillManager.isPickaxe(heldItem.getType())) {
                player.sendMessage(Component.text("Du musst eine Spitzhacke in der Hand halten!").color(NamedTextColor.RED));
                return;
            }

            ItemStack modified = drillManager.createDrill(heldItem);
            if (modified == null) {
                player.sendMessage(Component.text("Fehler beim Anwenden des Effekts.").color(NamedTextColor.RED));
                return;
            }

            player.getInventory().setItemInMainHand(modified);
            player.sendMessage(Component.text("Drill-Effekt angewendet!").color(NamedTextColor.GREEN));
            player.closeInventory();
        }
    }
}
