package de.konstantin.customItems.commands;

import de.konstantin.customItems.drill.DrillManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemsCommand implements CommandExecutor {
    private final DrillManager drillManager;

    public CustomItemsCommand(DrillManager drillManager) {
        this.drillManager = drillManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Nur Spieler können diesen Befehl verwenden!").color(NamedTextColor.RED));
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
        Inventory gui = Bukkit.createInventory(null, 9, Component.text("Wähle einen Effekt").color(NamedTextColor.DARK_PURPLE));

        // Drill effect item (iron pickaxe)
        ItemStack drillIcon = new ItemStack(Material.IRON_PICKAXE);
        final Component DRILL_EFFECT_NAME = Component.text("Bohrer").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true);
        ItemMeta meta = drillIcon.getItemMeta();
        if (meta != null) {
            meta.displayName(DRILL_EFFECT_NAME);
            meta.lore(java.util.List.of(
                    Component.text("§7Fügt deiner Spitzhacke").color(NamedTextColor.GRAY),
                    Component.text("§7einen Bohr-Effekt hinzu.").color(NamedTextColor.GRAY)
            ));
            drillIcon.setItemMeta(meta);
        }

        gui.setItem(4, drillIcon); // Centered

        player.openInventory(gui);
    }
}
