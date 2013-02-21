package com.github.FrancescoDeSa;
import java.util.ArrayList;


public class SignSession {
	
	public SignSession(ArrayList<RSign> signs, DataSource source){
		this.signs = signs;
		this.source = source;
	}
	public RSign getRegistered(RSign dummy) {
		if(signs.isEmpty()){
			System.out.println("session: NESSUN CARTELLO NELLA SESSIONE! (getreg)");
			return null;
		}
		for(RSign sign : signs){
			if(dummy.sameBlock(sign)){
				System.out.println("session: CARTELLO TROVATO (getreg)!");
				return sign;
			}
		}
		System.out.println("session: CARTELLO NON TROVATO NELLA SESSIONE! (getreg)");
		return null;
	}
	
	public boolean register(RSign sign) {
		if(source.save(sign)){
			signs.add(sign);
			System.out.println("session: CARTELLO MEMORIZZATO SIA SU DB CHE SU SESSIONE!(reg)");
			return true;
		}
		System.out.println("session: IMPOSSIBILE MEMORIZZARE SU DB!(reg)");
		return false;
	}
	public boolean update(RSign what){
		for(RSign sign : signs){
			if(what.sameBlock(sign)){
				System.out.println("CARTELLO TROVATO!");
				if(source.update(sign)){
					sign.setRented(what.isRented());
					sign.setOwner(what.getOwner());
					sign.setScadenza(what.getScadenza());
					System.out.println("UPDATE CARTELLO RIUSCITO");
					return true;
				}
				System.out.println("UPDATE CARTELLO FALLITO");
				return false;
			}
		}
		return false;
	}
	public boolean removeRegistered(RSign dummy) {
		for(RSign sign : signs){
			if(dummy.sameBlock(sign)){
				if(source.delete(sign)){
					sign.stopTask();
					signs.remove(sign);
					System.out.println("session: CARTELLO RIMOSSO SIA DAL DB CHE DALLA SESSIONE! (remreg)");
					return true;
				}
				System.out.println("session: IMPOSSIBILE ELIMINARE IL CARTELLO (remreg)");
				return false;
			}
		}
		System.out.println("session: CARTELLO NON TROVATO PER LA RIMOZIONE (remreg)");
		return false;
	}
	
	public void startScheduler(){
		for(RSign sign : signs){
			SignScheduler.setTask(sign);
		}
	}
	public boolean save(){
		for(RSign s: signs){
			if(!source.update(s)){
				System.out.println("IMPOSSIBILE SALVARE (AGGIORNARE) IL CARTELLO: "+s.toString());
				return false;
			}
			else System.out.println("SALVATO (AGGIORNATO) IL CARTELLO: "+s.toString());
		}
		return true;
	}
	private ArrayList<RSign> signs;
	private DataSource source;
}
