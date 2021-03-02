package com.tcn.cosmoslibrary.impl.util;

import java.util.LinkedHashMap;

public class HashMapUtils {
	
	public static class Linked {
		public static Object getKeyByIndex(LinkedHashMap<?, ?> map, int index){
		    return map.keySet().toArray()[index];
		}
		
		public static Object getValueByIndex(LinkedHashMap<?, ?> map, int index) {
			return map.get((map.keySet().toArray())[index]);
		}
	}
}
