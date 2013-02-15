package com.github.FrancescoDeSa;

//import org.bukkit.Material;
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
//import org.bukkit.inventory.ItemStack;

public class SignListener implements Listener {

	public SignListener(SignRent plugin){
		this.plugin = plugin;
	}
    @EventHandler
    public void cartelloCambiato(SignChangeEvent event) {
        Player giocatore = event.getPlayer();
        String ln0 = event.getLine(0);
        if(ln0.equalsIgnoreCase(plugin.getConfig().getString("sign.tag"))){
        	if(giocatore.hasPermission("signrent.sign.place")){
            	String l1 = event.getLine(1);
            	if(!l1.isEmpty()){
            		int prezzo = Integer.parseInt(l1);
	            	if(prezzo >= plugin.getConfig().getInt("sign.minprice") && prezzo <=plugin.getConfig().getInt("sign.maxprice")){
	                	String l2 = event.getLine(2);
	                	if(!l2.isEmpty()){
		            		int giorni = Integer.parseInt(event.getLine(2));
		            		if(giorni >= plugin.getConfig().getInt("sign.mindays") && giorni <= plugin.getConfig().getInt("sign.maxdays")){
		                    	//plugin.getLogger().info("giorni ok");
		            			giocatore.sendMessage(ChatColor.GREEN+"RentSign creato: "+prezzo+" ogni "+giorni+"giorni.");
		            			plugin.SignData.regSign(new Sign(new SerialBlock(event.getBlock()),giorni,prezzo));
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
            	//plugin.getLogger().info("azione giusta");
            	int tipo = block.getType().getId();
            	if(tipo == 63 || tipo == 68){
                	//plugin.getLogger().info("è un cartello");
                	SerialBlock dumblock = new SerialBlock(block);
                	Sign dumsign = new Sign(dumblock);
            		Sign element = plugin.SignData.getRegistered(dumsign);
            		if(element != null){
            			SerialPlayer realowner = element.getProprietario();
            			SerialPlayer serialized = new SerialPlayer(player);
                    	//plugin.getLogger().info("è registrato");
                    	int itemID = event.getItem().getType().getId();
                    	if(itemID == plugin.getConfig().getInt("tools.rent.id")){
                    		if(element.isInuso()){
                    			if(realowner.talequale(serialized)){
                    				player.sendMessage("Questa proprietà ti appartiene gia! ti rimangono ancora "+element.giorniRimasti());
                    				player.sendMessage("Per rinnovare, usa (click destro) "+plugin.getConfig().getString("tool.rent.name")+" sul cartello!");
                    			}
                    			else{
	                    			player.sendMessage("Spiacente, questo lotto è gia affittato!");
	                    			player.sendMessage("Questa proprietà appartiene a "+element.getProprietario().getName()+". Gli rimangono ancora "+element.giorniRimasti());
                    			}
                    		}
                    		else{
                    			if(SignRent.econ.getBalance(serialized.getName()) >= element.getPrezzo()){
                    				EconomyResponse res = SignRent.econ.withdrawPlayer(serialized.getName(), element.getPrezzo());
                    				if(res.transactionSuccess()){
	                        			element.setProprietario(serialized);
	                        			element.setInuso(true);
	                        			element.setScadenza();
	                        			player.sendMessage(ChatColor.GREEN+"Congratulazioni! Hai affittato questo lotto!");
	                        			plugin.SignData.Salva();
                    				}
                    			}
                    		}
                    	}
                    	else if(itemID == plugin.getConfig().getInt("tools.renew.id")){//flint
                    		if(element.isInuso()){
                    			if(realowner.talequale(serialized)){
                    				if(element.giorniRimasti() <= plugin.getConfig().getInt("sign.renewdays")){
                    					if(SignRent.econ.getBalance(serialized.getName()) >= element.getPrezzo()){
                            				EconomyResponse res = SignRent.econ.withdrawPlayer(serialized.getName(), element.getPrezzo());
                            				if(res.transactionSuccess()){
		                    					element.prolunga();
		                    					player.sendMessage("Affitto prolungato di "+element.getDurata()+" giorni!");
                            				}
                    					}
                    				}
                    				else{
                    					player.sendMessage("Spiacente, è troppo presto per rinnovare.");
                    					player.sendMessage("Puoi tornare a "+plugin.getConfig().getInt("sign.renewdays")+" giorni dalla data di scadenza");
                    				}
                    			}
                    		}
                    	}
                    	else{
	            			if(element.isInuso()){
	                			if(realowner.equals(serialized)){
	                				player.sendMessage("Questa proprietà ti appartiene. ti rimangono ancora "+element.giorniRimasti());
	                			}
	                			else if(player.hasPermission("signrent.sign.other.read")){
	                				player.sendMessage("Questa proprietà appartiene a "+element.getProprietario().getName()+". Gli rimangono ancora "+element.giorniRimasti());
	                			}
	            			}
	            			else{
	            				player.sendMessage("Questo lotto è disponibile all'affitto per "+element.getPrezzo()+" ogni "+element.getDurata()+" giorni");
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
        	Sign element = plugin.SignData.getRegistered(dumsign);
    		if(element != null){
    			if(player.hasPermission("signrent.sign.remove")){
        			plugin.SignData.removeSign(element);
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
