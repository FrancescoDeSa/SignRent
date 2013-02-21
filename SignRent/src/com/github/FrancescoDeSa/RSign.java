package com.github.FrancescoDeSa;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitTask;


public class RSign implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RSign(SignRent plugin, SerialBlock block, String player, int duration, int price, boolean rented, long expire){
		this.block = block;
		this.owner = player;
		this.duration = duration;
		this.price = price;
		this.rented = rented;
		this.expire = expire;
		this.plugin = plugin;
	}
	public RSign(SignRent plugin, SerialBlock sign, int duration, int price){
		this.block = sign;
		this.duration = duration;
		this.price = price;
		this.rented = false;
		this.plugin = plugin;
	}
	public RSign(SerialBlock sign){
		this.block = sign;
		this.rented = false;
	}
	public int remainingDays(){
		GregorianCalendar now = new GregorianCalendar();
		//TODO tornare a giorni
		//int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		int MILLIS_IN_DAY = 1000;//seconds
		long endInstant = expire;
		int presumedDays = (int) ((endInstant - now.getTimeInMillis()) / MILLIS_IN_DAY);
		return presumedDays;
	}
	public boolean exists(){
		return (block!=null);
	}
	public boolean sameBlock(RSign elemento){
		if(this.block.sameBlock(elemento.getSign())){
			return true;
		}
		else return false;
	}
	public void extend(){
		GregorianCalendar dum = new GregorianCalendar();
		//todo tornare a giorni
		dum.add(GregorianCalendar.MINUTE, duration);
		expire = dum.getTimeInMillis();
	}
	
	public void update(){
		World loc = block.getWorld();
		Sign sign = (Sign)loc.getBlockAt(block.getLocation()).getState();
		int index = 0;
		List<String> Lines = null;
		if(rented){
			boolean scaduto = remainingDays() < -(plugin.settings.getMaxLate);
			boolean ritardo = remainingDays() < 0;
			if(scaduto){
				Lines = plugin.settings.getSignLines;
				rented = false;
				owner = null;
				expire = 0;
				stopTask();
			}
			else if(ritardo){
				Lines = plugin.settings.getSignLLines;
			}
			else{
				Lines = plugin.settings.getSignRLines;
			}
		}
		else Lines = plugin.settings.getSignLines;
		for(String line: Lines){
			sign.setLine(index++, Cosmetics.parseLine(line,this));
		}
		sign.update();
	}
	
	public void setTask(BukkitTask task){
		if(rented)this.task = task;
	}
	public BukkitTask getTask(){
		return this.task;
	}
	public void stopTask(){
		if (task != null){
			task.cancel();
			task = null;
		}
	}
	
	public String toString(){
		return("BLOCK: "+this.block.toString()+"; OWNER: "+this.owner);
	}
	public SerialBlock getSign() {
		return block;
	}
	
	public boolean isRented() {
		return rented;
	}
	public void setRented(boolean rented) {
		this.rented = rented;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration){
		this.duration = duration;
	}
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price){
		this.price = price;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner){
		this.owner = owner;
	}
	
	public long getScadenza() {
		return expire;
	}
	public void setScadenza(GregorianCalendar data){
		this.expire = data.getTimeInMillis();
	}
	public void setScadenza(){
		GregorianCalendar dum = new GregorianCalendar();
		//TODO cambiare da minute a giorni
		dum.add(GregorianCalendar.MINUTE, duration);
		this.expire = dum.getTimeInMillis();
	}
	public void setScadenza(long expire){
		this.expire = expire;
	}
	
	private SerialBlock block;
	private int duration;

	private BukkitTask task;
	private int price;
	private boolean rented;
	private SignRent plugin;
	private String owner;
	private long expire;
}
