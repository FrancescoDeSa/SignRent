package com.github.FrancescoDeSa;

//import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.inventory.ItemStack;

public class SignListener implements Listener {

    @EventHandler
    public void cartelloCambiato(SignChangeEvent event) {
        Player giocatore = event.getPlayer();
        //String nome = giocatore.getName();
        String ln0 = event.getLine(0);
        if(ln0.equalsIgnoreCase("||SR||")){
        	if(giocatore.hasPermission("signrent.sign.place")){
            	int prezzo = Integer.parseInt(event.getLine(1));
            	if(prezzo > 0){
            		int giorni = Integer.parseInt(event.getLine(2));
            		if(giorni > 0){
            			DataStore db = new DataStore("SignRent.db");
            			giocatore.sendMessage("RentSign creato: "+prezzo+" ogni "+giorni+"giorni.");
            			db.regSign(new Sign(event.getBlock(),giorni,prezzo));
            		}
            		else giocatore.sendMessage("Errore: devi inserire il numero di giorni");
            	}
            	else giocatore.sendMessage("Errore: il prezzo deve essere un numero positivo");
            }
        	else giocatore.sendMessage("Errore: non hai i permessi per creare cartelli RentSign!");
        }
    }
    
    @EventHandler
    public void cartelloCliccato(PlayerInteractEvent event){
    	Player player = event.getPlayer();
    	if(event.hasBlock()){
    		Block block = event.getClickedBlock();
            Action azione = event.getAction();
            if(azione == Action.RIGHT_CLICK_BLOCK || azione == Action.LEFT_CLICK_BLOCK){
            	int tipo = block.getType().getId();
            	if(tipo == 63 || tipo == 68){
            		DataStore D = new DataStore("SignRent.db");
            		Sign element = D.getRegistered(new Sign(block));
            		if(element.exists()){
            			if(element.isInuso()){
                			Player realowner = element.getProprietario();
                			if(realowner.equals(player)){
                				realowner.sendMessage("Questa proprietà ti appartiene. ti rimangono "+element.getScadenza());
                			}
            			}
            			else{
            				player.sendMessage("Questo lotto è disponibile all'affitto per "+element.getPrezzo()+" ogni "+element.getDurata()+" giorni");
            			}
            		}
            	}
            }
    	}
    }
}
