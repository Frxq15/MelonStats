package org.melonmc.melonstats.gui.menus;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.gui.GUITemplate;

import java.util.ArrayList;
import java.util.List;

public class leaderboardsMenu extends GUITemplate {
    private final MelonStats plugin;
    private final FileConfiguration gui;

    public leaderboardsMenu(MelonStats plugin) {
        super(plugin, MelonStats.getInstance().getConfig().getInt("GUI.LEADERBOARD.ROWS"),
                MelonStats.getInstance().getConfig().getString("GUI.LEADERBOARD.TITLE"));
        this.plugin = plugin;
        this.gui = plugin.getConfig();
        ;
        initialize();
    }

    public void initialize() {
        initializeItems();
    }

    public void initializeItems() {
        int slots = MelonStats.getInstance().getConfig().getInt("GUI.LEADERBOARD.ROWS") * 9;
        if (!MelonStats.getInstance().getConfig().getString("GUI.LEADERBOARD.FILLER_ITEM").equalsIgnoreCase("none")) {
            for (int i = 0; i < slots; i++) {
                Integer value = new Integer(MelonStats.getInstance().getConfig().getInt("GUI.LEADERBOARD.FILLER_ITEM_DATA"));
                setItem(i, new ItemStack(Material.valueOf(MelonStats.getInstance().getConfig().getString("GUI.LEADERBOARD.FILLER_ITEM")), 1,
                        value.shortValue()));
            }
        }
        gui.getConfigurationSection("GUI.LEADERBOARD.ITEMS").getKeys(false).forEach(item -> {
            setItem(getItemSlot(item), createItem(item));
        });
    }

    public boolean hasGlow(String item, boolean miscitem) {
        if (miscitem) {
            return gui.getBoolean("GUI.LEADERBOARD.ITEMS." + item + ".GLOW");
        }
        return gui.getBoolean("GUI.LEADERBOARD.ITEMS." + item + ".GLOW");
    }

    public Integer getItemSlot(String item) {
        return gui.getInt("GUI.LEADERBOARD.ITEMS." + item + ".SLOT");
    }

    public ItemStack createItem(String item) {
        //normal item creation
        List<String> lore = new ArrayList<String>();
        String material = gui.getString("GUI.LEADERBOARD.ITEMS." + item + ".MATERIAL");
        Integer amount = gui.getInt("GUI.LEADERBOARD.ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = gui.getString("GUI.LEADERBOARD.ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(MelonStats.colourize(name));
        if (hasGlow(item, true)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(MelonStats.colourize(returnLore(String.valueOf(item))));
        i.setItemMeta(meta);
        lore.clear();
        return i;
    }

    public List<String> returnLore(String item) {
        List<String> lore = new ArrayList<>();
        switch (item) {
            case "TOP_KILLS":
                for (String line : gui.getStringList("GUI.LEADERBOARD.ITEMS." + item + ".LORE")) {
                    for (int i2 = 1; i2 < 15; i2++) {
                        line = line.replace("%topkills_name_" + i2 + "%", plugin.getLeaderboard().getNameByKillsPosition(i2));
                        line = line.replace("%topkills_kills_" + i2 + "%", plugin.getLeaderboard().getKillsByPosition(i2) + "");
                    }
                    lore.add(line);
                }
                return lore;
            case "TOP_DEATHS":
                for (String line : gui.getStringList("GUI.LEADERBOARD.ITEMS." + item + ".LORE")) {
                    for (int i2 = 1; i2 < 15; i2++) {
                        line = line.replace("%topdeaths_name_" + i2 + "%", plugin.getLeaderboard().getNameByDeathsPosition(i2));
                        line = line.replace("%topdeaths_deaths_" + i2 + "%", plugin.getLeaderboard().getDeathsByPosition(i2) + "");
                    }
                    lore.add(line);
                }
                return lore;
            case "TOP_HIGHEST_STREAK":
                for (String line : gui.getStringList("GUI.LEADERBOARD.ITEMS." + item + ".LORE")) {
                    for (int i2 = 1; i2 < 15; i2++) {
                        line = line.replace("%higheststreak_name_" + i2 + "%", plugin.getLeaderboard().getNameByHighestStreakPosition(i2));
                        line = line.replace("%higheststreak_streak_" + i2 + "%", plugin.getLeaderboard().getHighestStreakByPosition(i2) + "");
                    }
                    lore.add(line);
                }
                return lore;
        }
        return lore;
    }
}
