package com.github.FrancescoDeSa;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings{

	//generic
	public String datasource;
	//database
    public String getDatabase, getTableName, getColumnName, getColumnLocation, getColumnDuration,
    				getColumnPrice, getColumnRented, getColumnExpire;
    //signs
    public String getSignTag;
    public int getMinPrice, getMaxPrice, getMinDays, getMaxDays, getRenewDays;
    
    //tools
    public int getRentToolId, getRenewToolId;
    public String getRentToolName, getRenewToolName;
    
    public Settings(SignRent plugin) {
		this.config = plugin.getConfig();
    	this.datasource = config.getString("plugin.datasource");
		this.getDatabase = config.getString("plugin.db.database");
		this.getTableName = config.getString("plugin.db.table.name");
		this.getColumnName = config.getString("plugin.db.table.columns.name");
		this.getColumnLocation = config.getString("plugin.db.table.columns.location");
		this.getColumnDuration = config.getString("plugin.db.table.columns.duration");
		this.getColumnRented = config.getString("plugin.db.table.columns.rented");
		this.getColumnExpire = config.getString("plugin.db.table.columns.expire");
		
		this.getSignTag = config.getString("sign.tag");
		this.getMinPrice = config.getInt("sign.minprice");
		this.getMaxPrice = config.getInt("sign.maxprice");
		this.getMinDays = config.getInt("sign.mindays");
		this.getMaxDays = config.getInt("sign.maxdays");
		this.getRenewDays = config.getInt("sign.renewdays");
		
		this.getRentToolId = config.getInt("tools.rent.id");
		this.getRenewToolId = config.getInt("tools.renew.id");
		this.getRentToolName = config.getString("tools.rent.name");
		this.getRenewToolName = config.getString("tools.renew.name");
		
	}
	private FileConfiguration config;
}
