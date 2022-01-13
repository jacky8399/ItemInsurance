package com.jacky8399.iteminsurance;

import com.jacky8399.iteminsurance.utils.ItemProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemInsurance extends JavaPlugin {

    private static ItemInsurance INSTANCE;
    public static ItemInsurance get() {
        return INSTANCE;
    }

    public ItemProvider provider;

    @Override
    public void onEnable() {
        INSTANCE = this;

        provider = new ItemProvider();

        getCommand("iteminsurance").setExecutor(new CommandItemInsurance());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
