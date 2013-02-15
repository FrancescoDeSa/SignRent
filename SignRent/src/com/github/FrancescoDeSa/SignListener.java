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
        String nome = giocatore.getName();
        giocatore.sendMessage("Complimenti,"+nome+", hai piazzato un cartello!");
        String linea = event.getLine(0);
        giocatore.sendMessage("E nella prima riga hai messo \""+linea+"\"!!!");
    }
    
    @EventHandler
    public void cartelloCliccato(PlayerInteractEvent event){
    	Player player = event.getPlayer();
    	if(event.hasBlock()){
    		Block block = event.getClickedBlock();
            Action azione = event.getAction();
            if(azione == Action.RIGHT_CLICK_BLOCK){
            	int tipo = block.getType().getId();
            	if(tipo == 63 || tipo == 68){
            		DataStore D = new DataStore("SignRent.db");
            		Sign element = D.getRegistered(new Sign(block,null,null));
            		if(element.exists()){
            			Player realowner = element.getProprietario();
            			if(realowner.equals(player)){
            				realowner.sendMessage("Questa proprietà ti appartiene. ti rimangono "+element.getScadenza());
            			}
            		}
            	}
            }
    	}
    }
}
