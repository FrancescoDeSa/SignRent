package com.github.FrancescoDeSa;

import java.io.Serializable;
import java.util.StringTokenizer;

import org.bukkit.block.Block;

public class SerialBlock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int X;
	private int Y;
	private int Z;
	private String world;
	
	public SerialBlock(Block source){
		X = source.getX();
		Y = source.getY();
		Z = source.getZ();
		world = source.getWorld().getName();
	}
	public SerialBlock(String data){
		StringTokenizer st = new StringTokenizer(data,"|");
		this.X = Integer.parseInt(st.nextToken());
		this.Y = Integer.parseInt(st.nextToken());
		this.Z = Integer.parseInt(st.nextToken());
		this.world = st.nextToken();
	}

	public boolean sameBlock(SerialBlock lui){
		if(this.X == lui.getX() && this.Y == lui.getY() && this.Z == lui.getZ() && this.world.equals(lui.getWorld())) return true;
		else return false;
	}
	
	public String toString(){
		return (
				this.X+"|"+
				this.Y+"|"+
				this.Z+"|"+
				this.world
				);
	}
	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public int getZ() {
		return Z;
	}

	public String getWorld() {
		return world;
	}
}
