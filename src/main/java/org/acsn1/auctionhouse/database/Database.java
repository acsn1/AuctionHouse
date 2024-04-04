package org.acsn1.auctionhouse.database;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.acsn1.auctionhouse.AuctionHouse;
import org.acsn1.auctionhouse.ah.AuctionItem;

import java.io.File;
import java.util.Set;

@Getter
public class Database {

    /*
     * Author: acsn1
     * Date: 04/03/2024
     * This plugin was coded in 1 hour.
     */

    private final AuctionHouse plugin = AuctionHouse.getInstance();
    private final Set<AuctionItem> items = Sets.newHashSet();

    public Database() {
        final File dir = new File(plugin.getDataFolder() + "/data/");
        if(!(dir.exists())) dir.mkdirs();

        File[] files = dir.listFiles();
        if(files==null) return;

        for(File file : files) {
            AuctionItem auctionItem = AuctionItem.load(file.getPath());
            if(auctionItem.isConfirmed() || auctionItem.isExpired()) continue;
            items.add(auctionItem);
        }

        plugin.getLogger().info("Loaded " + items.size() + " items in /ah");

    }

    public AuctionItem getAuctionItem(String id) {
        for(AuctionItem item : items) {
            if(item.getId().equalsIgnoreCase(id)){
                return item;
            }
        }
        return null;
    }


    public void addItem(AuctionItem item) {
        items.add(item);
        item.save();
    }

}
