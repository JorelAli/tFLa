import java.util.List;
import java.util.ArrayList;

public class fl2nix {
	
	public static void main(String[] args) {
		String flCode = null;
		flCode = parseGuards(flCode);
		flCode = flCode.replace(" ->", ":");
		flCode = flCode.replace(",", "");

		/*
		 * Code changes:
		 *
		 * Functions:
		 * -> converts to :
		 *  guard conversion
		 *  set conversion
		 *  lists need to remove ,
		 *  includes changes
		 *
		 * */
	}

	static String parseGuards(String flCode) {
		return flCode.replace("->>", "[GUARD_ARROW]");
	}

}
