package com.github.FrancescoDeSa;
import java.util.ArrayList;


public class SignSession {
	
	public SignSession(ArrayList<Sign> signs, DataSource source){
		this.signs = signs;
		this.source = source;
	}
	public Sign getRegistered(Sign dummy) {
		for(Sign sign : signs){
			if(dummy.sameBlock(sign)){
				return sign;
			}
		}
		return null;
	}
	
	public boolean register(Sign sign) {
		signs.add(sign);
		return source.save(sign);
	}
	
	public boolean removeRegistered(Sign dummy) {
		for(Sign sign : signs){
			if(dummy.sameBlock(sign)){
				signs.remove(sign);
				return source.delete(sign);
			}
		}
		return false;
	}
	
	public boolean save(){
		for(Sign s: signs){
			if(!source.save(s))return false;
		}
		return true;
	}
	private ArrayList<Sign> signs;
	private DataSource source;
}
