package com.github.FrancescoDeSa;

public class SignScheduler{
	public static void setTask(RSign element){
		element.setTask(new ExpireChecker(element).runTaskTimer(plugin, 50, 50));
	}
	public static void setPlugin(SignRent plugin){
		SignScheduler.plugin = plugin;
	}
	public static SignRent plugin;
}
