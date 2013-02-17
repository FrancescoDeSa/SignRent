package com.github.FrancescoDeSa;
import java.util.ArrayList;


public class SignSession {
	
	public SignSession(ArrayList<Sign> signs, DataSource source){
		this.signs = signs;
		this.source = source;
	}
	public Sign getRegistered(Sign dummy) {
		if(signs.isEmpty()){
			System.out.println("session: NESSUN CARTELLO NELLA SESSIONE! (getreg)");
			return null;
		}
		for(Sign sign : signs){
			if(dummy.sameBlock(sign)){
				System.out.println("session: CARTELLO TROVATO (getreg)!");
				return sign;
			}
		}
		System.out.println("session: CARTELLO NON TROVATO NELLA SESSIONE! (getreg)");
		return null;
	}
	
	public boolean register(Sign sign) {
		if(source.save(sign)){
			signs.add(sign);
			System.out.println("session: CARTELLO MEMORIZZATO SIA SU DB CHE SU SESSIONE!(reg)");
			return true;
		}
		System.out.println("session: IMPOSSIBILE MEMORIZZARE SU DB!(reg)");
		return false;
	}
	public boolean update(Sign what){
		for(Sign sign : signs){
			if(what.sameBlock(sign)){
				System.out.println("CARTELLO TROVATO!");
				if(source.update(sign)){
					sign.setInuso(what.isInuso());
					sign.setProprietario(what.getProprietario());
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
	public boolean removeRegistered(Sign dummy) {
		for(Sign sign : signs){
			if(dummy.sameBlock(sign)){
				if(source.delete(sign)){
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
	
	public boolean save(){
		for(Sign s: signs){
			if(!source.update(s)){
				System.out.println("IMPOSSIBILE SALVARE (AGGIORNARE) IL CARTELLO: "+s.toString());
				return false;
			}
			else System.out.println("SALVATO (AGGIORNATO) IL CARTELLO: "+s.toString());
		}
		return true;
	}
	private ArrayList<Sign> signs;
	private DataSource source;
}
