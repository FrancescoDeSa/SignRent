package com.github.FrancescoDeSa;

import java.util.StringTokenizer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SerialBlock{
	private Location location;
	public SerialBlock(Block source){
		this.location = source.getLocation();
	}
	public SerialBlock(SignRent plugin, String data){
		StringTokenizer st = new StringTokenizer(data,"|");
		int X = Integer.parseInt(st.nextToken());
		int Y = Integer.parseInt(st.nextToken());
		int Z = Integer.parseInt(st.nextToken());
		String world  = st.nextToken();
		this.location = new Location(plugin.getServer().getWorld(world),X,Y,Z);
	}

	public boolean sameBlock(SerialBlock lui){
		if(this.getX() == lui.getX() && this.getY() == lui.getY() && this.getZ() == lui.getZ() && this.getWorldName().equals(lui.getWorldName())) return true;
		else return false;
	}
	
	public String toString(){
		return (
				this.getX()+"|"+
				this.getY()+"|"+
				this.getZ()+"|"+
				this.getWorldName()
				);
	}
	public int getX() {
		return location.getBlockX();
	}

	public int getY() {
		return location.getBlockY();
	}

	public int getZ() {
		return location.getBlockZ();
	}
	public Location getLocation(){
		return location;
	}
	public String getWorldName() {
		return location.getWorld().getName();
	}
	public World getWorld(){
		return location.getWorld();
	}
}
