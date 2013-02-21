package com.github.FrancescoDeSa;

import java.util.List;

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

    private SignRent plugin;
	private String signTag;
	private String rentName;
	private String renewName;
	private int minPrice;
	private int maxPrice;
	private int minDays;
	private int maxDays;
	private int rentId;
	private int renewId;
	private int renewDays;
	
	public SignListener(SignRent plugin){
		
		this.renewId = plugin.settings.getRenewToolId;
		this.rentName = plugin.settings.getRentToolName;
		this.rentId = plugin.settings.getRentToolId;
		this.signTag = plugin.settings.getSignTag;
		this.renewName = plugin.settings.getRenewToolName;
		this.maxDays = plugin.settings.getMaxDays;
		this.minDays = plugin.settings.getMinDays;
		this.minPrice = plugin.settings.getMinPrice;
		this.maxPrice = plugin.settings.getMaxPrice;
		this.renewDays = plugin.settings.getRenewDays;
		this.plugin = plugin;
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
		            			RSign temp = new RSign(plugin,dum,giorni,prezzo);
		            			if(plugin.session.register(temp)){
		            				giocatore.sendMessage(ChatColor.GREEN+"RentSign creato: "+SignRent.econ.format(prezzo)+" ogni "+giorni+" giorni.");
		            				
		            				int index = 0;
		            				List<String> Lines = plugin.settings.getSignLines;
		            				for(String line: Lines){
		            					event.setLine(index++, Cosmetics.parseLine(line,temp));
		            				}
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
                	RSign dumsign = new RSign(dumblock);
            		RSign element = plugin.session.getRegistered(dumsign);
            		if(element != null){
                    	//plugin.getLogger().info("è registrato");
            			String realowner = element.getOwner();
            			String serialized = player.getName();
            			if(event.hasItem()){
                        	//plugin.getLogger().info("è item");
	                    	int itemID = event.getItem().getType().getId();
	                    	if(itemID == rentId){
                    			//plugin.getLogger().info("Accettato");
	                    		if(element.isRented()){
	                    			//plugin.getLogger().info("Inuso");
	                    			if(realowner.equals(serialized)){
		                    			//plugin.getLogger().info("Giamia");
	                    				player.sendMessage("Questa proprietà ti appartiene gia! ti rimangono ancora "+element.remainingDays()+" giorni");
	                    				player.sendMessage("Per rinnovare, usa (click destro) "+renewName+" sul cartello!");
	                    			}
	                    			else{
	                        			//plugin.getLogger().info("Nonmia");
		                    			player.sendMessage("Spiacente, questo lotto è gia affittato!");
		                    			player.sendMessage("Questa proprietà appartiene a "+element.getOwner()+". Gli rimangono ancora "+element.remainingDays()+" giorni.");
	                    			}
	                    		}
	                    		else{
	                    			if(SignRent.econ.getBalance(serialized) >= element.getPrice()){
	                    				EconomyResponse res = SignRent.econ.withdrawPlayer(serialized, element.getPrice());
	                    				if(res.transactionSuccess()){
		                        			element.setOwner(serialized);
		                        			element.setRented(true);
		                        			element.setScadenza();
		                        			if(plugin.session.update(element)){
			                        			element.update();
			                        			player.sendMessage(ChatColor.GREEN+"Congratulazioni! Hai affittato questo lotto!");
					            				SignScheduler.setTask(element);
		                        			}
		                    				else{
		                    					player.sendMessage(ChatColor.RED+"Errore sconosciuto, impossibile affittare!");
		                    				}
	                    				}
	                    				else{
	                    					player.sendMessage(ChatColor.RED+res.errorMessage);
	                    				}
	                    			}
	                    			else player.sendMessage(ChatColor.RED+"Non puoi permetterti questo lotto: costa "+SignRent.econ.format(element.getPrice()));
	                    		}
	                    	}
	                    	else if(itemID == renewId){//flint
	                        	//plugin.getLogger().info("è flint");
	                    		if(element.isRented()){
	                            	//plugin.getLogger().info("è inuso");
	                    			if(realowner.equals(serialized)){
	                                	//plugin.getLogger().info("sono io");
	                    				if(element.remainingDays() <= renewDays){
	                                    	//plugin.getLogger().info("posso rinnovare");
	                    					if(SignRent.econ.getBalance(serialized) >= element.getPrice()){
	                                        	//plugin.getLogger().info("ho i soldi");
	                            				EconomyResponse res = SignRent.econ.withdrawPlayer(serialized, element.getPrice());
	                            				if(res.transactionSuccess()){
	                                            	//plugin.getLogger().info("tutto ok");
			                    					element.extend();
			                    					player.sendMessage("Affitto prolungato di "+element.getDuration()+" giorni!");
	                            				}
	                    					}
	                    					else player.sendMessage(ChatColor.RED+"Non puoi permetterti di prolungare questo affitto: costa "+SignRent.econ.format(element.getPrice()));
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
		            			if(element.isRented()){
		                        	//plugin.getLogger().info("è inuso");
		                			if(realowner.equals(serialized)){
		                            	//plugin.getLogger().info("sonoio");
		                				player.sendMessage("Questa proprietà ti appartiene. ti rimangono ancora "+element.remainingDays()+" giorni.");
		                			}
		                			else if(player.hasPermission("signrent.sign.other.read")){
		                            	//plugin.getLogger().info("nonsonoio");
		                				player.sendMessage("Questa proprietà appartiene a "+element.getOwner()+". Gli rimangono ancora "+element.remainingDays()+" giorni.");
		                			}
		            			}
		            			else{
		                        	//plugin.getLogger().info("è disponibile");
		            				player.sendMessage("Questo lotto è disponibile all'affitto per "+SignRent.econ.format(element.getPrice())+" ogni "+element.getDuration()+" giorni");
		            				player.sendMessage(ChatColor.BOLD+"Usa (tasto destro) una "+rentName+" sul cartello per affittare!");
		            			}
	                    	}
            			}
                    	else{
                        	plugin.getLogger().info("nienteitem");
	            			if(element.isRented()){
	                        	//plugin.getLogger().info("è inuso");
	                			if(realowner.equals(serialized)){
	                            	//plugin.getLogger().info("sonoio");
	                				player.sendMessage("Questa proprietà ti appartiene. ti rimangono ancora "+element.remainingDays()+ " giorni!");
	                			}
	                			else if(player.hasPermission("signrent.sign.other.read")){
	                            	//plugin.getLogger().info("è un altro");
	                				player.sendMessage("Questa proprietà appartiene a "+element.getOwner()+". Gli rimangono ancora "+element.remainingDays()+" giorni.");
	                			}
	            			}
	            			else{
	                        	//plugin.getLogger().info("è disponibile");
	            				player.sendMessage("Questo lotto è disponibile all'affitto per "+SignRent.econ.format(element.getPrice())+" ogni "+element.getDuration()+" giorni");
	            				player.sendMessage(ChatColor.BOLD+"Usa (tasto destro) una "+rentName+" sul cartello per affittare!");
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
        	RSign dumsign = new RSign(dumblock);
        	RSign element = plugin.session.getRegistered(dumsign);
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
}
