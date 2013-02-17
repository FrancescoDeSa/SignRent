package com.github.FrancescoDeSa;

import java.io.Serializable;
import java.util.GregorianCalendar;


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
	
	public String toString(){
		return("BLOCCO: "+this.block.toString()+" PROPRIETARIO: "+this.owner);
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
	public void setDurata(int duration){
		this.duration = duration;
	}
	
	public int getPrezzo() {
		return price;
	}
	public void setPrezzo(int price){
		this.price = price;
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
	public void setScadenza(long expire){
		this.expire = expire;
	}
	
	private SerialBlock block;
	private int duration;

	private int price;
	private boolean rented;
	
	private String owner;
	private long expire;
}
