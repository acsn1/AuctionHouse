package org.acsn1.auctionhouse.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ItemUtils {

    public static String parseItemStack(ItemStack itemStack)
    {
        try
        {
            final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(itemStack);
            return Base64Coder.encodeLines(arrayOutputStream.toByteArray());
        }
        catch (final Exception exception)
        {
            throw new RuntimeException("Could not turn ItemStack to Base64", exception);
        }
    }

    public static ItemStack Of(String base64)
    {
        try
        {
            final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            final BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(arrayInputStream);
            return (ItemStack) objectInputStream.readObject();
        }
        catch (final Exception exception)
        {
            throw new RuntimeException("Error translating base64 to itemstack", exception);
        }
    }

}
