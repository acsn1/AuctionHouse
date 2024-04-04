package org.acsn1.auctionhouse.util;

import org.bukkit.ChatColor;

public class ChatUtils {

    // No hex support, speed running this
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
