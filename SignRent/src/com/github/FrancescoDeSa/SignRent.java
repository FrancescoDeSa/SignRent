package com.github.FrancescoDeSa;

//import org.bukkit.command.Command;
import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SignRent extends JavaPlugin{
	
	@Override
    public void onEnable(){
		
		if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(ascoltatore, this);
		
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		SignData = new DataStore(new File(pluginFolder+File.separator+"SignRent.dat"),this);
		SignData.Carica();
    }
 
    @Override
    public void onDisable() {
    	getLogger().info("onDisable has been invoked!");
    	SignData.Salva();
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	public DataStore SignData;
	public final SignListener ascoltatore = new SignListener(this);
	public static Economy econ = null;
	public Logger log = getLogger();
}
