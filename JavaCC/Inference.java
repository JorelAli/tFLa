import java.math.BigDecimal;

public class Inference {

	static String infer(Object expression) {
		if(expression instanceof FL_Var) {
			// look it up
		} 
		//else
		if(expression instanceof BigDecimal) {
			return "num";
		}
		
		if(expression instanceof Boolean) {
			return "bool";
		}
		
		if(expression != null)
		
			return expression.getClass().getName();
		return null;
	}
	
}
