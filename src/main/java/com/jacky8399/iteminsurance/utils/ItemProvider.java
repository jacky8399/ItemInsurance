package com.jacky8399.iteminsurance.utils;

import me.danjono.inventoryrollback.data.LogType;
import me.danjono.inventoryrollback.data.PlayerData;
import me.danjono.inventoryrollback.inventory.RestoreInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ItemProvider {
    public ItemStack @Nullable [] getLastInventory(Player player) {
        PlayerData data = new PlayerData(player, LogType.DEATH);
        if (data.getFile().exists()) {
            FileConfiguration yaml = data.getData();
            ConfigurationSection dataSection = yaml.getConfigurationSection("data");
            if (dataSection == null)
                return null;
            long latestBackupTimestamp = dataSection.getKeys(false).stream()
                    .mapToLong(Long::parseLong).max().orElse(-1);
            if (latestBackupTimestamp == -1) // no backups
                return null;
            RestoreInventory inv = new RestoreInventory(yaml, latestBackupTimestamp);
            ItemStack[] main = inv.retrieveMainInventory();
            ItemStack[] armor = inv.retrieveArmour();
            ItemStack[] result = Arrays.copyOf(main, main.length + armor.length);
            System.arraycopy(armor, 0, result, main.length, armor.length);
            return result;
        }
        return null;
    }
}
