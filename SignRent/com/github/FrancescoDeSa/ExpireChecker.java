package com.github.FrancescoDeSa;

import org.bukkit.scheduler.BukkitRunnable;

public class ExpireChecker extends BukkitRunnable {
	public ExpireChecker(RSign sign){
		this.sign = sign;
	}
	public void run(){
		sign.update();
	}
	private RSign sign;
}
