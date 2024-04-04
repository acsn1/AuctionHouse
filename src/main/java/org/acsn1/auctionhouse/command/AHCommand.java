package org.acsn1.auctionhouse.command;

import org.acsn1.auctionhouse.AuctionHouse;
import org.acsn1.auctionhouse.ah.AuctionItem;
import org.acsn1.auctionhouse.gui.AHMenu;
import org.acsn1.auctionhouse.util.ChatUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AHCommand implements CommandExecutor, TabCompleter {

    public AHCommand() {
        AuctionHouse.getInstance().getCommand("ah").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;


        if(args.length == 0) {
            new AHMenu().show(player);
            player.sendMessage(ChatUtils.color("&7If you wish to sell type /ah sell <price> while holding an item in your main hand!"));
        }

        if(args.length == 2) {
            if(!(args[0]).equalsIgnoreCase("sell")) {
                player.sendMessage(ChatUtils.color("&cUsage: /ah sell <price>"));
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if(item==null || item.getType() == Material.AIR) {
                player.sendMessage(ChatUtils.color("&cYou can't sell air!"));
                return true;
            }

            try {
                double amount = Double.parseDouble(args[1]);

                if(amount <= 0) {
                    player.sendMessage(ChatUtils.color("&cPlease enter a correct number above 0."));
                    return true;
                }

                AuctionItem auctionItem = new AuctionItem(player.getUniqueId(), item, amount);
                AuctionHouse.getInstance().getDatabase().addItem(auctionItem);
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                player.sendMessage(ChatUtils.color("&eYou have added a new item in the /ah for $" + amount));



            } catch(NumberFormatException ex) {
                player.sendMessage(ChatUtils.color("&cPlease enter a correct number above 0."));
            }

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
