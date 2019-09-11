
public class Print {

	public static boolean SECD = true;
	public static boolean EVAL = true;
	public static boolean OPEX = true;
	
	public static void SECD()                   { if(SECD) print("SECD"); }
	public static void SECD(Object o)           { if(SECD) print("SECD", o); }
	public static void SECD(String s, Object o) { if(SECD) print("SECD", s, o); }

	public static void EVAL()                   { if(EVAL) print("EVAL"); }
	public static void EVAL(Object o)           { if(EVAL) print("EVAL", o); }
	public static void EVAL(String s, Object o) { if(EVAL) print("EVAL", s, o); }

	public static void OPEX()                   { if(OPEX) print("OPEX"); }
	public static void OPEX(Object o)           { if(OPEX) print("OPEX", o); }
	public static void OPEX(String s, Object o) { if(OPEX) print("OPEX", s, o); }
	
	private static void print(String title)           { print(title, "", ""); }
	private static void print(String title, Object o) { print(title, "", o); }
	private static void print(String title, String init, Object o) {
	  	if(ForteLang.LOGGING_ENABLE) {
		  	System.out.println("[" + title + "] " + init + (init.isEmpty() ? "" : " ") + o);
	  	}
	}

	
}
