
public class Playground {

/**
 * DEF ABCD xyz { BCD(xyz) } ;
DEF BCD xy { 2*CD(xy) } ;
DEF CD x { D(x)+EF(x) } ;
DEF D x { 10 } ;
DEF EF x { 10*x } ;
DEF MAIN { ABCD(1) } ;
 */

	//BCD 1
	//2 * [10 + 10*1]
	//2 * [10 + 10*1]
	//2 * [10 + 10]
	//2 * 20
	// 40
	
	
	public static void main(String[] args) {
		System.out.println(ABCD(1));
	}
	
	public static int ABCD(int xyz) {
		return BCD(xyz);
	}
	
	public static int BCD(int xy) {
		return 2 * CD(xy);
	}
	
	public static int CD(int x) {
		return D(x) + EF(x);
	}
	
	public static int D(int x) {
		return 10;
	}
	
	public static int EF(int x) {
		return 10 * x;
	}
	
}
