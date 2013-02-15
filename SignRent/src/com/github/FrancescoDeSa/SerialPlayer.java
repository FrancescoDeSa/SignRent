package com.github.FrancescoDeSa;

import org.bukkit.entity.Player;

public class SerialPlayer {
	
	public SerialPlayer(Player persona){
		this.name = persona.getName();
	}
	public String getName(){
		return name;
	}
	public boolean talequale(SerialPlayer lui){
		String suonome = lui.getName();
		String mionome = this.name;
		if(suonome.equals(mionome))return true;
		else return false;
	}
	private String name;
}
