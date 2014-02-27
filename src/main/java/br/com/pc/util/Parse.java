package br.com.pc.util;

public abstract class Parse {
	
	public static Long Long(Object o){
		try {
			return Long.valueOf(o.toString());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String String(Object o){
		try {
			return o.toString();
		} catch (Exception e) {
			return null;
		}
	}
}
