package com.github.FrancescoDeSa;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

	String signTag, rentName,renewName;
	int minPrice, maxPrice, minDays, maxDays, rentId, renewId, renewDays;
	
	public SignListener(SignRent plugin){
		this.plugin = plugin;
		this.signTag = plugin.settings.getSignTag;
		this.rentName = plugin.settings.getRentToolName;
		this.rentId = plugin.settings.getRentToolId;
		this.renewId = plugin.settings.getRenewToolId;
		this.renewName = plugin.settings.getRenewToolName;
		this.maxDays = plugin.settings.getMaxDays;
		this.minDays = plugin.settings.getMinDays;
		this.minPrice = plugin.settings.getMinPrice;
		this.maxPrice = plugin.settings.getMaxPrice;
		this.renewDays = plugin.settings.getRenewDays;
	}
    @EventHandler
    public void cartelloCambiato(SignChangeEvent event) {
        Player giocatore = event.getPlayer();
        String ln0 = event.getLine(0);
        if(ln0.equalsIgnoreCase(signTag)){
        	if(giocatore.hasPermission("signrent.sign.place")){
            	String l1 = event.getLine(1);
            	if(!l1.isEmpty()){
            		int prezzo = Integer.parseInt(l1);
	            	if(prezzo >= minPrice && prezzo <= maxPrice){
	                	String l2 = event.getLine(2);
	                	if(!l2.isEmpty()){
		            		int giorni = Integer.parseInt(event.getLine(2));
		            		if(giorni >= minDays && giorni <= maxDays){
		                    	//plugin.getLogger().info("giorni ok");
		            			SerialBlock dum = new SerialBlock(event.getBlock());
		            			Sign temp = new Sign(dum,giorni,prezzo);
		            			if(plugin.session.register(temp)){
		            				giocatore.sendMessage(ChatColor.GREEN+"RentSign creato: "+SignRent.econ.format(prezzo)+" ogni "+giorni+" giorni.");
		            			}
		            		}
		            		else giocatore.sendMessage(ChatColor.LIGHT_PURPLE+"Errore: il numero di giorni non rientra nei limiti");
	            		}
	            		else giocatore.sendMessage(ChatColor.LIGHT_PURPLE+"Errore: devi inserire il numero di giorni");
	            	}
	            	else giocatore.sendMessage(ChatColor.LIGHT_PURPLE+"Errore: Il prezzo non rientra nei limiti");
            	}
            	else giocatore.sendMessage(ChatColor.LIGHT_PURPLE+"Errore: non hai inserito il prezzo");
            }
        	else giocatore.sendMessage(ChatColor.LIGHT_PURPLE+"Errore: non hai i permessi per creare cartelli SignRent!");
        }
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
                	//plugin.getLogger().info("è un cartello");
                	SerialBlock dumblock = new SerialBlock(block);
                	Sign dumsign = new Sign(dumblock);
            		Sign element = plugin.session.getRegistered(dumsign);
            		if(element != null){
                    	//plugin.getLogger().info("è registrato");
            			String realowner = element.getProprietario();
            			String serialized = player.getName();
            			if(event.hasItem()){
                        	//plugin.getLogger().info("è item");
	                    	int itemID = event.getItem().getType().getId();
	                    	if(itemID == rentId){
                    			//plugin.getLogger().info("Accettato");
	                    		if(element.isInuso()){
	                    			//plugin.getLogger().info("Inuso");
	                    			if(realowner.equals(serialized)){
		                    			//plugin.getLogger().info("Giamia");
	                    				player.sendMessage("Questa proprietà ti appartiene gia! ti rimangono ancora "+element.giorniRimasti()+" giorni");
	                    				player.sendMessage("Per rinnovare, usa (click destro) "+renewName+" sul cartello!");
	                    			}
	                    			else{
	                        			//plugin.getLogger().info("Nonmia");
		                    			player.sendMessage("Spiacente, questo lotto è gia affittato!");
		                    			player.sendMessage("Questa proprietà appartiene a "+element.getProprietario()+". Gli rimangono ancora "+element.giorniRimasti()+" giorni.");
	                    			}
	                    		}
	                    		else{
	                    			if(SignRent.econ.getBalance(serialized) >= element.getPrezzo()){
	                    				EconomyResponse res = SignRent.econ.withdrawPlayer(serialized, element.getPrezzo());
	                    				if(res.transactionSuccess()){
		                        			element.setProprietario(serialized);
		                        			element.setInuso(true);
		                        			element.setScadenza();
		                        			plugin.session.update(element);
		                        			player.sendMessage(ChatColor.GREEN+"Congratulazioni! Hai affittato questo lotto!");
	                    				}
	                    				else{
	                    					player.sendMessage(ChatColor.RED+res.errorMessage);
	                    				}
	                    			}
	                    			else player.sendMessage(ChatColor.RED+"Non puoi permetterti questo lotto: costa "+SignRent.econ.format(element.getPrezzo()));
	                    		}
	                    	}
	                    	else if(itemID == renewId){//flint
	                        	//plugin.getLogger().info("è flint");
	                    		if(element.isInuso()){
	                            	//plugin.getLogger().info("è inuso");
	                    			if(realowner.equals(serialized)){
	                                	//plugin.getLogger().info("sono io");
	                    				if(element.giorniRimasti() <= renewDays){
	                                    	//plugin.getLogger().info("posso rinnovare");
	                    					if(SignRent.econ.getBalance(serialized) >= element.getPrezzo()){
	                                        	//plugin.getLogger().info("ho i soldi");
	                            				EconomyResponse res = SignRent.econ.withdrawPlayer(serialized, element.getPrezzo());
	                            				if(res.transactionSuccess()){
	                                            	//plugin.getLogger().info("tutto ok");
			                    					element.prolunga();
			                    					player.sendMessage("Affitto prolungato di "+element.getDurata()+" giorni!");
	                            				}
	                    					}
	                    					else player.sendMessage(ChatColor.RED+"Non puoi permetterti di prolungare questo affitto: costa "+SignRent.econ.format(element.getPrezzo()));
	                    				}
	                    				else{
	                                    	//plugin.getLogger().info("non posso rinnovare");
	                    					player.sendMessage("Spiacente, è troppo presto per rinnovare.");
	                    					player.sendMessage("Puoi tornare a "+plugin.getConfig().getInt("sign.renewdays")+" giorni dalla data di scadenza");
	                    				}
	                    			}
	                    		}
	                        	//plugin.getLogger().info("vavattenne");
	                    	}
	                    	else{
	                        	//plugin.getLogger().info("altro item");
		            			if(element.isInuso()){
		                        	//plugin.getLogger().info("è inuso");
		                			if(realowner.equals(serialized)){
		                            	//plugin.getLogger().info("sonoio");
		                				player.sendMessage("Questa proprietà ti appartiene. ti rimangono ancora "+element.giorniRimasti()+" giorni.");
		                			}
		                			else if(player.hasPermission("signrent.sign.other.read")){
		                            	//plugin.getLogger().info("nonsonoio");
		                				player.sendMessage("Questa proprietà appartiene a "+element.getProprietario()+". Gli rimangono ancora "+element.giorniRimasti()+" giorni.");
		                			}
		            			}
		            			else{
		                        	//plugin.getLogger().info("è disponibile");
		            				player.sendMessage("Questo lotto è disponibile all'affitto per "+SignRent.econ.format(element.getPrezzo())+" ogni "+element.getDurata()+" giorni");
		            				player.sendMessage(ChatColor.BOLD+"Usa (tasto destro) una stick sul cartello per affittare!");
		            			}
	                    	}
            			}
                    	else{
                        	plugin.getLogger().info("nienteitem");
	            			if(element.isInuso()){
	                        	//plugin.getLogger().info("è inuso");
	                			if(realowner.equals(serialized)){
	                            	//plugin.getLogger().info("sonoio");
	                				player.sendMessage("Questa proprietà ti appartiene. ti rimangono ancora "+element.giorniRimasti()+ " giorni!");
	                			}
	                			else if(player.hasPermission("signrent.sign.other.read")){
	                            	//plugin.getLogger().info("è un altro");
	                				player.sendMessage("Questa proprietà appartiene a "+element.getProprietario()+". Gli rimangono ancora "+element.giorniRimasti()+" giorni.");
	                			}
	            			}
	            			else{
	                        	//plugin.getLogger().info("è disponibile");
	            				player.sendMessage("Questo lotto è disponibile all'affitto per "+SignRent.econ.format(element.getPrezzo())+" ogni "+element.getDurata()+" giorni");
	            				player.sendMessage(ChatColor.BOLD+"Usa (tasto destro) una stick sul cartello per affittare!");
	            			}
                    	}
            		}
            	}
            }
    	}
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
    	Player player = event.getPlayer();
    	Block block = event.getBlock();
    	int blockID = block.getType().getId();
    	if(blockID==63 || blockID==68){
    		SerialBlock dumblock = new SerialBlock(block);
        	Sign dumsign = new Sign(dumblock);
        	Sign element = plugin.session.getRegistered(dumsign);
    		if(element != null){
    			if(player.hasPermission("signrent.sign.remove")){
        			plugin.session.removeRegistered(element);
        			player.sendMessage(ChatColor.YELLOW+" Cartello eliminato!!!");
    			}
    			else{
        			player.sendMessage(ChatColor.RED+" Non puoi eliminare i cartelli SignRent!!!");
        			event.setCancelled(true);
    			}
    		}
    	}
    }
    
    private SignRent plugin;
}
