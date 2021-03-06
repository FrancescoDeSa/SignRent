package com.github.FrancescoDeSa;

import java.io.File;
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
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		settings = new Settings(this);
		if(settings.datasource.equals("database")){
			try {
				this.source = new SqliDataSource(this,settings);
			} catch (ClassNotFoundException | SQLException e) {
				this.getLogger().warning(e.getMessage());
				this.getLogger().severe("Can't initialize database. Disabling plugin...");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
		SignScheduler.setPlugin(this);
		session = source.load();
		ascoltatore = new SignListener(this);
		pm.registerEvents(ascoltatore, this);
    }
 
    @Override
    public void onDisable(){
    	if(session.save()){
    		System.out.println("Sessione salvata con successo!!!");
    	}
    	else System.out.println("ERRORE: IMPOSSIBILE SALVARE LA SESSIONE");
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
	public SignListener ascoltatore;
	public static Economy econ = null;
}
