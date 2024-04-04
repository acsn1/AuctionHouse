package org.acsn1.auctionhouse.tasks;

import org.acsn1.auctionhouse.AuctionHouse;
import org.acsn1.auctionhouse.ah.AuctionItem;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class ExpireTask {

    private final AuctionHouse plugin = AuctionHouse.getInstance();

    private int taskId;

    public ExpireTask() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {

            for(AuctionItem items : plugin.getDatabase().getItems()) {
                if(items.isConfirmed() || items.isExpired()) continue;
                long difference = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - items.getIssued());
                if(difference < 7) continue;
                items.setExpired(true);
                items.delete();

            }

        }, 0L, 20L);
    }

    public void terminate() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

}
