package com.github.FrancescoDeSa;

//import org.bukkit.block.Block;
//import org.bukkit.entity.Player;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

public class DataStore {
	
	public DataStore(String filename){
		config = Db4oEmbedded.newConfiguration();
		db = Db4oEmbedded.openFile(config, filename);
	}
	
	public Sign getRegistered(Sign cartello){
		ObjectSet<Sign> result = db.queryByExample(cartello);
		if(result.isEmpty()){
			return null;
		}
		else{
			Sign lui = result.next();
			return new Sign(lui.getCartello(),lui.getProprietario(),lui.getScadenza());
		}
	}

	private EmbeddedConfiguration config;
	private ObjectContainer db;
}
