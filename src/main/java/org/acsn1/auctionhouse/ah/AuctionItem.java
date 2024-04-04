package org.acsn1.auctionhouse.ah;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.acsn1.auctionhouse.AuctionHouse;
import org.acsn1.auctionhouse.util.ItemAdapter;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Getter @Setter
public class AuctionItem {

    private final String id;
    private final ItemStack item;
    private final UUID seller;
    private final double amount;
    private final long issued;
    private boolean confirmed;
    private boolean expired;

    public AuctionItem(UUID seller, ItemStack item, double amount) {
        this.id = UUID.randomUUID().toString().substring(0, 12);
        this.seller = seller;
        this.item = item;
        this.amount = amount;
        this.issued = System.currentTimeMillis();
    }

    public AuctionItem(String id, ItemStack item, UUID seller, double amount, long issued, boolean confirmed, boolean expired) {
        this.id = id;
        this.item = item;
        this.seller = seller;
        this.amount =amount;
        this.issued = issued;
        this.confirmed = confirmed;
        this.expired = expired;
    }

    public static AuctionItem load(String file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemStack.class, new ItemAdapter()).create();
        try(FileReader reader = new FileReader(file)) {
            return (AuctionItem) gson.fromJson(reader, AuctionItem.class);

        } catch(IOException ex) {
            AuctionHouse.getInstance().getLogger().severe("Failed to load " + file + " auction item!");
            return null;
        }
    }

    public void delete() {
        File file = new File(AuctionHouse.getInstance().getDataFolder() + "/data/", id + ".json");
        if(file.exists()) file.delete();
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(item.getClass(), new ItemAdapter()).create();
        String path = "plugins/AuctionHouse/data/";

        try(FileWriter writer = new FileWriter(path + id + ".json")){
            gson.toJson(this, writer);
        } catch(IOException ex) {
            AuctionHouse.getInstance().getLogger().severe("Failed to save " + id + " auction item!");
        }
    }


}
