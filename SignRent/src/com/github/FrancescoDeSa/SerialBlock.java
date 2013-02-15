package com.github.FrancescoDeSa;

import java.io.Serializable;

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

	boolean talequale(SerialBlock lui){
		if(this.X == lui.getX() && this.Y == lui.getY() && this.Z == lui.getZ() && this.world.equals(lui.getWorld())) return true;
		else return false;
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
