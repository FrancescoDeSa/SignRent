package com.github.FrancescoDeSa;

//import java.util.ArrayList;
import java.io.Serializable;
import java.util.Calendar;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.StringTokenizer;
//import java.util.Date;
import java.util.GregorianCalendar;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;

public class Sign implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Sign(SerialBlock block, String player, int duration, int price, boolean rented, long expire){
		this.block = block;
		this.owner = player;
		this.duration = duration;
		this.price = price;
		this.rented = rented;
		this.expire = expire;
	}
	public Sign(SerialBlock cartello, int durata, int prezzo){
		this.block = cartello;
		this.duration = durata;
		this.price = prezzo;
		this.rented = false;
	}
	public Sign(SerialBlock cartello){
		this.block = cartello;
		this.rented = false;
	}
	public int giorniRimasti(){
		GregorianCalendar propriomo = new GregorianCalendar();
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		long endInstant = expire;
		int presumedDays = (int) ((endInstant - propriomo.getTimeInMillis()) / MILLIS_IN_DAY);
		Calendar cursor = (Calendar) propriomo.clone();
		cursor.add(Calendar.DAY_OF_YEAR, presumedDays);
		long instant = cursor.getTimeInMillis();
		if(instant == endInstant){
			return presumedDays;  
		}
		final int step = instant < endInstant ? 1 : -1;  
		do{
			cursor.add(Calendar.DAY_OF_MONTH, step);  
			presumedDays += step;
		}while (cursor.getTimeInMillis() != endInstant);  
		return presumedDays;
	}
	public boolean exists(){
		return (block!=null);
	}
	public boolean sameBlock(Sign elemento){
		if(this.block.sameBlock(elemento.getCartello())){
			return true;
		}
		else return false;
	}
	public void prolunga(){
		GregorianCalendar dum = new GregorianCalendar();
		dum.add(GregorianCalendar.DAY_OF_MONTH, duration);
		expire = dum.getTimeInMillis();
	}
	public SerialBlock getCartello() {
		return block;
	}
	
	public boolean isInuso() {
		return rented;
	}
	public void setInuso(boolean inuso) {
		this.rented = inuso;
	}

	public int getDurata() {
		return duration;
	}
	
	public int getPrezzo() {
		return price;
	}
	
	public String getProprietario() {
		return owner;
	}
	public void setProprietario(String tizio){
		this.owner = tizio;
	}
	
	public long getScadenza() {
		return expire;
	}
	public void setScadenza(GregorianCalendar data){
		this.expire = data.getTimeInMillis();
	}

	public void setScadenza(){
		GregorianCalendar dum = new GregorianCalendar();
		dum.add(GregorianCalendar.DAY_OF_MONTH, duration);
		this.expire = dum.getTimeInMillis();
	}
	
	private SerialBlock block;
	private int duration;

	private int price;
	private boolean rented;
	
	private String owner;
	private long expire;
}
