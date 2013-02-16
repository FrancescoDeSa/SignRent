package com.github.FrancescoDeSa;

import java.sql.SQLException;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SignRent extends JavaPlugin{
	
	@Override
    public void onEnable(){
		PluginManager pm = getServer().getPluginManager();
		if (!setupEconomy() ) {
            pm.disablePlugin(this);
            return;
        }
	
		settings = new Settings(this);
		if(settings.datasource.equals("database")){
			try {
				this.source = new SqliDataSource(this,settings);
			} catch (ClassNotFoundException | SQLException e) {
				this.getLogger().severe("Can't initialize database. Disabling plugin...");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
		session = source.load();
		pm.registerEvents(ascoltatore, this);
    }
 
    @Override
    public void onDisable(){
    	session.save();
    }
    
    
    private boolean setupEconomy(){
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

	public SignSession session;
	public DataSource source;
	public Settings settings;
	public final SignListener ascoltatore = new SignListener(this);
	public static Economy econ = null;
}
