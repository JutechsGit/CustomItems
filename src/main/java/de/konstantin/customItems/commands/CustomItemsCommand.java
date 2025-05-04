package de.konstantin.customItems.commands;

import de.konstantin.customItems.drill.DrillManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomItemsCommand implements CommandExecutor {
    private final DrillManager drillManager;
    private static final String PERMISSION = "customitems.use";

    public CustomItemsCommand(DrillManager drillManager) {
        this.drillManager = drillManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        // Permission check
        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            return true;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase("effect")) {
            player.sendMessage("§cVerwendung: /customitems effect <effect>");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !drillManager.isPickaxe(item.getType())) {
            player.sendMessage("§cDu musst eine Spitzhacke in der Hand halten!");
            return true;
        }

        String effect = args[1].toLowerCase();
        if (effect.equals("drill")) {
            ItemStack drill = drillManager.createDrill(item);
            if (drill != null) {
                player.getInventory().setItemInMainHand(drill);
                player.sendMessage("§aDrill-Effekt wurde zur Spitzhacke hinzugefügt!");
            } else {
                player.sendMessage("§cFehler beim Hinzufügen des Drill-Effekts!");
            }
        } else {
            player.sendMessage("§cUnbekannter Effekt: " + effect);
        }

        return true;
    }

    private boolean isPickaxe(ItemStack item) {
        return drillManager.createDrill(item) != null;
    }
}