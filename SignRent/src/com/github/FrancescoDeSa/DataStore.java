package com.github.FrancescoDeSa;

//import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
//import java.util.StringTokenizer;


public class DataStore {
	
	public DataStore(File file, SignRent plugin){
		this.file = file;
		this.plugin = plugin;
		cartelli = new ArrayList<Sign>();
		
		if(!this.file.exists()){
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void Carica(){
		
		try {
			/*DataInputStream input = new DataInputStream(new FileInputStream(this.file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			String line;
			while((line = reader.readLine()) != null){
				StringTokenizer ST = new StringTokenizer(line,"@");
				if(ST.countTokens() == ){
					
				}
			}*/
			FileInputStream instream = new FileInputStream(this.file);
			if(instream.available() > 5){
				ObjectInputStream inread = new ObjectInputStream(instream);
				plugin.getLogger().info("File non vuoto. Caricamento in corso...");
				Sign letto;
				cartelli = new ArrayList<Sign>();
				while((letto=((Sign)inread.readObject()))!=null && instream.available()>5){
					cartelli.add(letto);
				}
				inread.close();
				plugin.getLogger().info("Caricamento completato!");
			}
			else plugin.getLogger().info("File vuoto!");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public void Salva(){
		try {
			FileOutputStream fout = new FileOutputStream(this.file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			plugin.getLogger().info("Inizio scrittura su file...");
			for(Sign cartello : cartelli){
				oos.writeObject(cartello);
			}
			oos.close();
			plugin.getLogger().info("Scrittura completata!");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public Sign getRegistered(Sign cartello){
		for(Sign elemento : cartelli){
			if(cartello.sameBlock(elemento)){
				return elemento;
			}
		}
		return null;
	}
	public void removeSign(Sign cartello){
		for(Sign elemento : cartelli){
			if(cartello.sameBlock(elemento)){
				cartelli.remove(elemento);
				this.Salva();
				return;
			}
		}
	}
	public void regSign(Sign dati){
		cartelli.add(dati);
		this.Salva();
	}
	
	private File file;
	private ArrayList<Sign> cartelli;
	private SignRent plugin;
	
}
