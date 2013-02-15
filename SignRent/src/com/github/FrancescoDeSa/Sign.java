package com.github.FrancescoDeSa;

import java.util.Calendar;
//import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Sign {

	public Sign(Block cartello){
		this.cartello = cartello;
		this.proprietario = null;
		this.scadenza = null;
	}
	public Sign(Block cartello, Player proprietario){
		this.cartello = cartello;
		this.proprietario = proprietario;
		this.scadenza = null;
	}
	public Sign(Block cartello, Player proprietario, int giorni){
		this.cartello = cartello;
		this.proprietario = proprietario;
		this.scadenza = new GregorianCalendar();
		this.scadenza.add(Calendar.DAY_OF_MONTH, giorni);
	} 
	public Sign(Block cartello, Player proprietario, GregorianCalendar scadenza){
		this.cartello = cartello;
		this.proprietario = proprietario;
		this.scadenza = scadenza;
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
		return !(cartello==null || proprietario==null || scadenza==null);
	}
	public Block getCartello() {
		return cartello;
	}
	public Player getProprietario() {
		return proprietario;
	}
	public GregorianCalendar getScadenza() {
		return scadenza;
	}
	private Block cartello;
	private Player proprietario;
	private GregorianCalendar scadenza;
}
