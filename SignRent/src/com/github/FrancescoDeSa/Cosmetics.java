package com.github.FrancescoDeSa;

import java.util.HashMap;

public class Cosmetics {
	public static String parseLine(String line, RSign element){
		HashMap<String, String> rules = new HashMap<String, String>();
		rules.put("$d", ""+element.getDuration());
		rules.put("$p", ""+element.getPrice());
		rules.put("$e", ""+element.remainingDays());
		rules.put("$n", ""+element.getOwner());

		String result = line;
		for(String key: rules.keySet()){
			result = result.replace(key, rules.get(key));
		}
		return result;	
	}
}
