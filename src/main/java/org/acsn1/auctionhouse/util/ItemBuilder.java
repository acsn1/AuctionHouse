package org.acsn1.auctionhouse.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {


    private ItemStack itemStack;
    private Material material;
    private String name;
    private int amount;
    private int modelData;
    private List<String> lore = new ArrayList<>();
    private boolean hideAttributes;
    private boolean hideEnchants;
    private Map<NamespacedKey, String> customData = new HashMap<>();

    public ItemBuilder(Material material) {
        this.material = material;
        this.itemStack = new ItemStack(material,1);
    }

    public ItemBuilder(ItemStack item, int amount) {
        this.itemStack = item;
        this.amount = amount;
    }

    public ItemBuilder(Material material, int amount) {
        this.material = material;
        this.itemStack = new ItemStack(material,amount);
    }

    public ItemBuilder addKey(NamespacedKey key, String value) {
        customData.putIfAbsent(key, value);
        return this;
    }

    public ItemBuilder removeKey(NamespacedKey key, String value) {
        customData.remove(key, value);
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder addLoreSpace() {
        this.lore.add("");
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        lore.add(ChatUtils.color(line));
        return this;
    }

    public ItemBuilder setLore(List<String> updatedLore) {
        List<String> a = new ArrayList<>();
        for(String b : updatedLore) {
            a.add(ChatUtils.color(b));
        }
        lore = a;
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level)
    {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addEnchantIf(Enchantment enchantment, int level, boolean require) {
        if(!require) return this;
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addLoreLineIf(String line, boolean require) {
        if(!require) return this;
        return addLoreLine(line);
    }

    public ItemBuilder setModelData(int data) {
        modelData=data;
        return this;
    }

    public ItemBuilder hideAttributes() {
        this.hideAttributes = true;
        return this;
    }

    public ItemBuilder hideEnchants() {
        this.hideEnchants = true;
        return this;
    }

    public ItemStack build() {
        ItemMeta meta = itemStack.getItemMeta();
        if(meta!=null) {
            if (name != null) {
                meta.setDisplayName(ChatUtils.color(name));
            }
            if(modelData!=0) {
                meta.setCustomModelData(modelData);
            }
            meta.setLore(lore);

            if (hideAttributes) meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (hideEnchants) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            for(Map.Entry<NamespacedKey, String> entry : customData.entrySet()) {
                meta.getPersistentDataContainer().set(entry.getKey(), PersistentDataType.STRING, entry.getValue());
            }

        }
        itemStack.setItemMeta(meta);
        return itemStack.clone();
    }

}
