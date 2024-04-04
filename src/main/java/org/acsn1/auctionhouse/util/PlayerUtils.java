package org.acsn1.auctionhouse.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtils {

    public static String getName(UUID uuid) {
        Player t = Bukkit.getPlayer(uuid);
        if(t!=null) return t.getName();
        OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
        return offline.getName();
    }

}
