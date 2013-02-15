package com.github.FrancescoDeSa;

//import org.bukkit.command.Command;
import java.io.File;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SignRent extends JavaPlugin{
	
	public final SignListener ascoltatore = new SignListener(this);
	@Override
    public void onEnable(){
		getLogger().info("onEnable has been invoked!");
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
    
	public DataStore SignData;
}
