package com.github.FrancescoDeSa;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {

    @EventHandler
    public void cartelloCambiato(SignChangeEvent event) {
        Player giocatore = event.getPlayer();
        String nome = giocatore.getName();
        giocatore.sendMessage("Complimenti,"+nome+", hai piazzato un cartello!");
        String linea = event.getLine(0);
        giocatore.sendMessage("E nella prima riga hai messo \""+linea+"\"!!!");
    }
}
