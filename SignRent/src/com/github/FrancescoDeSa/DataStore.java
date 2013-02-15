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
			ObjectInputStream inread = new ObjectInputStream(instream);
			Sign letto;
			cartelli = new ArrayList<Sign>();
			while((letto=((Sign)inread.readObject()))!=null){
				cartelli.add(letto);
			}
			inread.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void Salva(){
		try {
			FileOutputStream fout = new FileOutputStream(this.file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			for(Sign cartello : cartelli){
				oos.writeObject(cartello);
			}
			oos.close();
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
