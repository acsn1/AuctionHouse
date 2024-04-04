package org.acsn1.auctionhouse;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.acsn1.auctionhouse.command.AHCommand;
import org.acsn1.auctionhouse.database.Database;
import org.acsn1.auctionhouse.tasks.ExpireTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class AuctionHouse extends JavaPlugin {

    @Getter private static AuctionHouse instance;
    private Database database;
    private Economy economy;

    // Tasks
    private ExpireTask expireTask;

    @Override
    public void onEnable() {
        instance = this;
        database = new Database();

        if(!(setupEconomy())) {
            getLogger().severe("Couldn't find Vault dependency. Plugin is disabling now...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        expireTask = new ExpireTask();
    }

    @Override
    public void onDisable() {
        expireTask.terminate();
    }

    private void registerCommands() {
        new AHCommand();
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

}
