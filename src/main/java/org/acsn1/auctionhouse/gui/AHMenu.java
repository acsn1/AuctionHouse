package org.acsn1.auctionhouse.gui;

import lombok.Getter;
import lombok.Setter;
import org.acsn1.auctionhouse.AuctionHouse;
import org.acsn1.auctionhouse.ah.AuctionItem;
import org.acsn1.auctionhouse.util.ChatUtils;
import org.acsn1.auctionhouse.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public final class AHMenu extends Menu implements Listener {

    private Inventory inventory;


    public AHMenu() {
        super("&lAuction House", 45);
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    public void show(Player player) {
        inventory = Bukkit.createInventory(null, getSize(), getTitle());

        List<AuctionItem> items = getPlugin().getDatabase().getItems().stream().toList();
        for(AuctionItem item : items) {
            if(item.isConfirmed() || item.isExpired()) continue;
            if(inventory.getItem(getSize()-1)!=null) break;
            ItemStack itemStack = item.getItem();
            if(itemStack==null) continue;

            ItemMeta meta = itemStack.getItemMeta();
            assert meta!=null;
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(getPlugin(), "ah"), PersistentDataType.STRING, item.getId());
            itemStack.setItemMeta(meta);

            inventory.addItem(itemStack);
        }


        player.openInventory(inventory);


    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if(!event.getInventory().equals(inventory)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;
        if(!event.getView().getTopInventory().equals(inventory)) return;
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if(item==null) return;
        ItemMeta meta = item.getItemMeta();
        assert meta!=null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        String id = "";
        for(NamespacedKey keys : container.getKeys()) {
            if(!keys.getKey().equalsIgnoreCase("ah")) continue;

            id = container.get(keys, PersistentDataType.STRING);

        }

        if(id.equalsIgnoreCase("")) return;
        AuctionItem auctionItem = getPlugin().getDatabase().getAuctionItem(id);
        if(auctionItem==null) return;
        // Double check for safety reasons, the item shouldn't be in the UI in the first place but a second check just to be safe
        if(auctionItem.isConfirmed() || auctionItem.isExpired()) return;


        // If the seller clicks their item, just give it back
        if(auctionItem.getSeller().equals(player.getUniqueId())) {

            if(player.getInventory().firstEmpty() == -1) {
                player.sendMessage(ChatUtils.color("&cYour inventory is full, please give it some space before proceeding!"));
                return;
            }

            getPlugin().getDatabase().getItems().remove(auctionItem);
            auctionItem.delete();

            event.setCurrentItem(null);
            player.closeInventory();
            addToInventory(player, auctionItem.getItem());

            player.sendMessage(ChatUtils.color("&eYour item has been removed from the /ah list!"));
            return;
        }


        // Seller != Clicker

        double balance = getPlugin().getEconomy().getBalance(player);
        if(balance < auctionItem.getAmount()) {
            player.sendMessage(ChatUtils.color("&cYou can't purchase this as it costs $" + auctionItem.getAmount() + " while you only have $"+balance));
            return;
        }

        if(player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatUtils.color("&cYour inventory is full, please give it some space before proceeding!"));
            return;
        }

        event.setCurrentItem(null);
        player.closeInventory();
        auctionItem.setConfirmed(true);
        getPlugin().getEconomy().withdrawPlayer(player, auctionItem.getAmount());
        addToInventory(player, auctionItem.getItem());

        updateSeller(auctionItem.getSeller(), auctionItem.getAmount());
        getPlugin().getDatabase().getItems().remove(auctionItem);

    }

    private void addToInventory(@NotNull Player player, @NotNull ItemStack item) {

        ItemStack itemStack = item.clone();
        ItemMeta meta = itemStack.getItemMeta();
        assert meta!=null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        for(NamespacedKey keys : container.getKeys()) {
            if(!keys.getKey().equalsIgnoreCase("ah")) continue;
            container.remove(keys);
        }

        itemStack.setItemMeta(meta);
        player.getInventory().addItem(itemStack);
    }

    private void updateSeller(UUID seller, double amount) {
        new BukkitRunnable() {
            public void run() {
                getPlugin().getEconomy().depositPlayer(PlayerUtils.getName(seller), amount);
                cancel();
            }
        }.runTaskLater(getPlugin(), 40L);
    }

}
