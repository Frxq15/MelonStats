package org.melonmc.melonstats.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.melonmc.melonstats.MelonStats;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUITemplate {
    public static Map<UUID, GUITemplate> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();
    private final MelonStats plugin;
    private final int rows;
    private final String title;

    private final Inventory inventory;
    private Map<Integer, GUIAction> actions;
    private UUID uuid;

    public GUITemplate(MelonStats plugin, int rows, String title) {
        uuid = UUID.randomUUID();
        this.plugin = plugin;
        this.rows = rows;
        this.title = title;
        inventory = Bukkit.createInventory(null, 9 * rows, MelonStats.colourize(title));
        actions = new HashMap<>();
        inventoriesByUUID.put(getUUID(), this);
    }

    public interface GUIAction {
        void click(Player player);
    }

    public UUID getUUID() {
        return uuid;
    }

    public static Map<UUID, GUITemplate> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public void open(Player player) {
        player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), getUUID());
    }
    public void delete() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID u = openInventories.get(p.getUniqueId());
            if (u.equals(getUUID())) {
                p.closeInventory();
            }
        }
        inventoriesByUUID.remove(getUUID());
    }

    public Map<Integer, GUIAction> getActions() {
        return actions;
    }

    public void setItem(int slot, ItemStack stack, GUIAction action) {
        inventory.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    public void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    protected ItemStack createGlowingGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
}

