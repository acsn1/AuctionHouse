package org.acsn1.auctionhouse.gui;

import lombok.Getter;
import org.acsn1.auctionhouse.AuctionHouse;
import org.acsn1.auctionhouse.util.ChatUtils;

@Getter
public sealed class Menu permits AHMenu {

    private final AuctionHouse plugin = AuctionHouse.getInstance();

    private final String title;
    private final int size;

    public Menu(String title, int size) {
        this.title = ChatUtils.color(title);
        this.size = size;
    }


}
