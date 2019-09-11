import java.io.File;
import java.math.BigDecimal;

public class Exceptions {
	
	//Parsing errors
	///////////////////////////////////////////////////////////////////////
	
	private static void parsingError(String str) {
		System.out.println("Parsing failure: " + str);
	}
	
	//When a program is empty
	public static void EMPTY_PROGRAM(String location) {
		parsingError("Program is empty, cannot evaluate anything" + location);
		System.exit(0);
	}
	
	//When a program is empty
	public static void ITEM_IN_SET(String attrName, String location) {
		parsingError("Attribute \"" + attrName + "\" already exists in set" + location);
		System.exit(0);
	}
	
	//Evaluation errors
	///////////////////////////////////////////////////////////////////////
	
	private static void evalError(String str) {
		System.out.println("Evaluation failure: " + str);
	}
	
	//When a program is empty
	public static void FUNCTION_NOT_DEFINED(String functionName) {
		evalError("Function \"" + functionName + "\" was called, but has not been defined");
		System.exit(0);
	}
	
	//When a file was not found
	public static void FILE_NOT_FOUND(File file) {
		evalError("File \"" + file.getAbsolutePath() + "\" could not be located");
		System.exit(0);
	}

	public static void DIV_BY_ZERO(BigDecimal f2) {
		evalError("Could not divide " + f2.toString() + " by zero");
		System.exit(0);
	}

	public static void HEAD_ON_EMPTY_LIST() {
		evalError("Could not retrieve the head of an empty list");
		System.exit(0);
	}
	
	public static void TAIL_ON_EMPTY_LIST() {
		evalError("Could not retrieve the tail of an empty list");
		System.exit(0);
	}
	
	//Type errors
	///////////////////////////////////////////////////////////////////////
	
	private static void typeError(String str) {
		System.out.println("Invalid types: " + str);
	}

	private static String evaluatableToType(Object object) {
		if(object instanceof Evaluatable) {
			if(object instanceof FL_Builtin) return "function";
			if(object instanceof FL_Function) return "function";
			if(object instanceof FL_FunctionCall) return "function";
			if(object instanceof FL_List) return "list";
			if(object instanceof FL_Set) return "set";
			if(object instanceof FL_String) return "string";
			else return null;
		} else {
			if(object instanceof BigDecimal) return "number";
			if(object instanceof Boolean) return "boolean";
			return object.getClass().getSimpleName();
		}
	}
	
	public static void CANNOT_CONCAT(Object o1, Object o2) {
		typeError("Cannot concatenate " + evaluatableToType(o1) + " with " + evaluatableToType(o2));
		System.exit(0);
	}
}
