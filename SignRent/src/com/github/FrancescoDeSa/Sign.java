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
	public Sign(SerialBlock cartello, int durata, int prezzo){
		this.cartello = cartello;
		this.durata = durata;
		this.prezzo = prezzo;
		this.inuso = false;
	}
	public Sign(SerialBlock cartello){
		this.cartello = cartello;
		this.inuso = false;
	}
	public int giorniRimasti(){
		GregorianCalendar propriomo = new GregorianCalendar();
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		long endInstant = scadenza.getTimeInMillis();
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
		return (cartello!=null);
	}
	public boolean sameBlock(Sign elemento){
		if(this.cartello.talequale(elemento.getCartello())){
			return true;
		}
		else return false;
	}
	public SerialBlock getCartello() {
		return cartello;
	}
	
	public boolean isInuso() {
		return inuso;
	}
	public void setInuso(boolean inuso) {
		this.inuso = inuso;
	}

	public int getDurata() {
		return durata;
	}
	
	public int getPrezzo() {
		return prezzo;
	}
	
	public SerialPlayer getProprietario() {
		return proprietario;
	}
	public void setProprietario(SerialPlayer tizio){
		this.proprietario = tizio;
	}
	
	public GregorianCalendar getScadenza() {
		return scadenza;
	}
	public void setScadenza(GregorianCalendar data){
		this.scadenza = data;
	}
	public void setScadenza(){
		this.scadenza = new GregorianCalendar();
		this.scadenza.add(GregorianCalendar.DAY_OF_MONTH, durata);
	}
	
	private SerialBlock cartello;
	private int durata;

	private int prezzo;
	private boolean inuso;
	
	private SerialPlayer proprietario;
	private GregorianCalendar scadenza;
}
