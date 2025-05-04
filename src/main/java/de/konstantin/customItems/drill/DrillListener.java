package de.konstantin.customItems.drill;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DrillListener implements Listener {
    private final DrillManager drillManager;
    private static final List<Material> ALLOWED_BLOCKS = Arrays.asList(


            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE,
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE,

            Material.STONE, Material.DEEPSLATE,
            Material.ANDESITE, Material.POLISHED_ANDESITE,
            Material.DIORITE,
            Material.GRANITE,
            Material.CALCITE, Material.TUFF, Material.NETHERRACK, Material.BLACKSTONE
    );
    private static final List<Material> ORE_BLOCKS = Arrays.asList(
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE,
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE
    );
    private final ThreadLocal<Boolean> processingDrill = ThreadLocal.withInitial(() -> false);

    public DrillListener(DrillManager drillManager) {
        this.drillManager = drillManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (processingDrill.get() || event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!drillManager.isDrill(item)) {
            return;
        }

        Block block = event.getBlock();
        if (!ALLOWED_BLOCKS.contains(block.getType())) {
            return;
        }

        try {

            processingDrill.set(true);

            List<Material> brokenBlockMaterials = new ArrayList<>();
            brokenBlockMaterials.add(block.getType());

            List<Block> blocksToBreak = calculate3x3Area(block, player);
            blocksToBreak.remove(block);

            int blocksBroken = 1;

            for (Block targetBlock : blocksToBreak) {
                if (blocksBroken >= 9) {
                    break;
                }
                if (!targetBlock.getType().isAir() && targetBlock.getType().getHardness() > 0 && ALLOWED_BLOCKS.contains(targetBlock.getType())) {
                    BlockBreakEvent newEvent = new BlockBreakEvent(targetBlock, player);

                    if (ORE_BLOCKS.contains(targetBlock.getType())) {
                        Collection<ItemStack> drops = targetBlock.getDrops(item, player);
                        newEvent.setDropItems(true);
                    }

                    player.getServer().getPluginManager().callEvent(newEvent);
                    if (!newEvent.isCancelled()) {
                        // Break block and collect drops
                        targetBlock.breakNaturally(item);
                        brokenBlockMaterials.add(targetBlock.getType()); // Record the block type
                        blocksBroken++;
                    } else {
                    }
                }
            }

        } finally {
            processingDrill.set(false);
        }
    }

    private List<Block> calculate3x3Area(Block centerBlock, Player player) {
        List<Block> blocks = new ArrayList<>();
        Location center = centerBlock.getLocation();
        Vector direction = player.getLocation().getDirection();

        boolean xAxis = Math.abs(direction.getX()) > Math.abs(direction.getZ());
        boolean vertical = Math.abs(direction.getY()) > Math.abs(direction.getX()) &&
                Math.abs(direction.getY()) > Math.abs(direction.getZ());

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Block targetBlock;
                if (vertical) {
                    targetBlock = centerBlock.getWorld().getBlockAt(
                            center.getBlockX() + x,
                            center.getBlockY(),
                            center.getBlockZ() + y);
                } else if (xAxis) {
                    targetBlock = centerBlock.getWorld().getBlockAt(
                            center.getBlockX(),
                            center.getBlockY() + y,
                            center.getBlockZ() + x);
                } else {
                    targetBlock = centerBlock.getWorld().getBlockAt(
                            center.getBlockX() + x,
                            center.getBlockY() + y,
                            center.getBlockZ());
                }
                blocks.add(targetBlock);
            }
        }

        return blocks;
    }
}