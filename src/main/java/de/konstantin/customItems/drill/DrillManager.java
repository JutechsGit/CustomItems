package de.konstantin.customItems.drill;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DrillManager {
    private static final String DRILL_LORE = "§7Bohrer I";

    public ItemStack createDrill(ItemStack pickaxe) {
        if (pickaxe == null || !isPickaxe(pickaxe.getType())) {
            return null;
        }

        ItemStack drill = pickaxe.clone();
        ItemMeta meta = drill.getItemMeta();
        if (meta == null) {
            return null;
        }

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        if (!lore.contains(DRILL_LORE)) {
            lore.add(DRILL_LORE);
        }
        meta.setLore(lore);
        drill.setItemMeta(meta);
        return drill;
    }

    public boolean isDrill(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        return lore != null && lore.contains(DRILL_LORE);
    }

    public boolean isPickaxe(Material material) {
        return material == Material.WOODEN_PICKAXE ||
                material == Material.STONE_PICKAXE ||
                material == Material.IRON_PICKAXE ||
                material == Material.GOLDEN_PICKAXE ||
                material == Material.DIAMOND_PICKAXE ||
                material == Material.NETHERITE_PICKAXE;
    }
}