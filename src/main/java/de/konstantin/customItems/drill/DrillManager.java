package de.konstantin.customItems.drill;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DrillManager {
    private static final Component DRILL_LORE = Component.text("Bohrer I").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
    private final NamespacedKey drillKey;

    public DrillManager(Plugin plugin) {
        this.drillKey = new NamespacedKey(plugin, "drill_effect");
    }

    public ItemStack createDrill(ItemStack pickaxe) {
        if (pickaxe == null || !isPickaxe(pickaxe.getType())) {
            return null;
        }

        ItemStack drill = pickaxe.clone();
        ItemMeta meta = drill.getItemMeta();
        if (meta == null) {
            return null;
        }

        // Add lore
        List<Component> lore = meta.lore() != null ? new ArrayList<>(Objects.requireNonNull(meta.lore())) : new ArrayList<>();
        if (!lore.contains(DRILL_LORE)) {
            lore.add(DRILL_LORE);
        }
        meta.lore(lore);

        // Add PersistentData
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(drillKey, PersistentDataType.BYTE, (byte) 1);

        drill.setItemMeta(meta);
        return drill;
    }

    public boolean isDrill(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(drillKey, PersistentDataType.BYTE);
    }

    public boolean isPickaxe(Material material) {
        return switch (material) {
            case WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE -> true;
            default -> false;
        };
    }
}