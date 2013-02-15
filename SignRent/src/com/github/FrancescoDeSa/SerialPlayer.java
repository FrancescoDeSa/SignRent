package com.github.FrancescoDeSa;

import org.bukkit.entity.Player;

public class SerialPlayer {
	
	public SerialPlayer(Player persona){
		this.name = persona.getName();
	}
	public String getName(){
		return name;
	}
	private String name;
}
