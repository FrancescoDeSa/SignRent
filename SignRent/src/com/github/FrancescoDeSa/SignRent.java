package com.github.FrancescoDeSa;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SignRent extends JavaPlugin{
	
	public final SignListener ascoltatore = new SignListener();
	@Override
    public void onEnable(){
		getLogger().info("onEnable has been invoked!");
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(ascoltatore, this);
    }
 
    @Override
    public void onDisable() {
    	getLogger().info("onDisable has been invoked!");
    }
    
}
