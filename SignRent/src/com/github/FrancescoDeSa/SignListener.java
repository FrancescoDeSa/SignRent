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

	public SignListener(SignRent plugin){
		this.plugin = plugin;
	}
    @EventHandler
    public void cartelloCambiato(SignChangeEvent event) {
        Player giocatore = event.getPlayer();
        //String nome = giocatore.getName();
        String ln0 = event.getLine(0);
        if(ln0.equalsIgnoreCase("||sr||")){
        	plugin.getLogger().info("Intestazione giusta");
        	if(giocatore.hasPermission("signrent.sign.place") || true){
            	plugin.getLogger().info("Permessi ok");
            	String l1 = event.getLine(1);
            	if(l1!=""){
            		int prezzo = Integer.parseInt(l1);
	            	if(prezzo > 0){
	                	plugin.getLogger().info("prezzo ok");
	                	String l2 = event.getLine(2);
	                	if(l2 != ""){
		            		int giorni = Integer.parseInt(event.getLine(2));
		            		if(giorni > 0){
		                    	plugin.getLogger().info("giorni ok");
		            			giocatore.sendMessage("RentSign creato: "+prezzo+" ogni "+giorni+"giorni.");
		            			plugin.SignData.regSign(new Sign(new SerialBlock(event.getBlock()),giorni,prezzo));
		            		}
	            		}
	            		else giocatore.sendMessage("Errore: devi inserire il numero di giorni");
	            	}
            	}
            	else giocatore.sendMessage("Errore: il prezzo deve essere un numero positivo");
            }
        	//else giocatore.sendMessage("Errore: non hai i permessi per creare cartelli RentSign!");
        }
    }
    
    @EventHandler
    public void cartelloCliccato(PlayerInteractEvent event){
    	Player player = event.getPlayer();
    	if(event.hasBlock()){
    		Block block = event.getClickedBlock();
            Action azione = event.getAction();
            if(azione == Action.RIGHT_CLICK_BLOCK){
            	plugin.getLogger().info("azione giusta");
            	int tipo = block.getType().getId();
            	if(tipo == 63 || tipo == 68){
                	plugin.getLogger().info("è un cartello");
                	SerialBlock dumblock = new SerialBlock(block);
                	Sign dumsign = new Sign(dumblock);
            		Sign element = plugin.SignData.getRegistered(dumsign);
            		if(element != null){
                    	plugin.getLogger().info("è registrato");
            			if(element.isInuso()){
                			SerialPlayer realowner = element.getProprietario();
                			if(realowner.equals(new SerialPlayer(player))){
                				player.sendMessage("Questa proprietà ti appartiene. ti rimangono "+element.getScadenza());
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
    
    private SignRent plugin;
}
