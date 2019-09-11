import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

public class OperatorParser {

	enum Operator {
		BOOLEAN, NUMERICAL, SET, COMPARATOR, CONCAT, CONTAINS, SELECT
	};

	Operator operatorKind;
	String op;

	public OperatorParser(Token operator) throws Exception {

		switch (operator.kind) {
			case ForteLangConstants.BOOLEAN_OP:
				operatorKind = Operator.BOOLEAN;
				break;
			case ForteLangConstants.OP:
				operatorKind = Operator.NUMERICAL;
				break;
			case ForteLangConstants.SET_OP:
				operatorKind = Operator.SET;
				break;
			case ForteLangConstants.COMPARATOR_OP:
				operatorKind = Operator.COMPARATOR;
				break;
			case ForteLangConstants.CONCAT:
				operatorKind = Operator.CONCAT;
				break;
			case ForteLangConstants.CONTAINS:
				operatorKind = Operator.CONTAINS;
				break;
			case ForteLangConstants.SELECT:
				operatorKind = Operator.SELECT;
				break;
			default:
				throw new Exception("Invalid operator for OperatorParser: " + operator.image);
		}
		op = operator.image;
	}

	public Object apply(Closure expr1, Closure expr2) throws Exception {

		Object o1 = null;
		Object o2 = null;
		
		if (operatorKind != Operator.SELECT) {
			o1 = Evaluator.evaluate(expr1);
			o2 = Evaluator.evaluate(expr2);
		}

		Print.OPEX("    Operation to evaluate: " + o1 + " " + op + " " + o2);
		
		switch (operatorKind) {
			case BOOLEAN:
				return applyBoolean((boolean) o1, (boolean) o2);
			case NUMERICAL:
				return applyNumber((BigDecimal) o1, (BigDecimal) o2);
			case SET:
				return applySetObjects((FL_Set) o1, (FL_Set) o2);
			case COMPARATOR:
				return applyComparator(o1, o2);
			case CONCAT:
				return applyConcat(o1, o2);
			case CONTAINS:
				return applyContains(o1, o2);
			case SELECT:
				return applySelect(expr1, expr2);
		}
		throw new Exception("Failed to apply any operators");
	}

	public Object applySelect(Closure expr1, Closure expr2) throws Exception {
		Object o1 = Evaluator.evaluate(expr1);
		
		FL_Set set = null;
		
		if (o1 instanceof Closure) {
			Closure o1C = (Closure) o1;
			if(o1C.getExpression() instanceof FL_Set) {
				set = (FL_Set) o1C.getExpression();
			}
		} else {
			if(o1 instanceof FL_Set) {
				set = (FL_Set) o1;
			}
		}
		
		if(set == null) {
			throw new Exception("Cannot select from an object that's not a set");
		}
			
		//If it's a String, e.g. { a = 2; }."a"
		if (expr2.getExpression() instanceof FL_String) {
			FL_String flString = (FL_String) expr2.getExpression();
			Object result = set.get(flString.stringValue());
			if (result == null) {
				throw new Exception("Cannot find element \"" + flString + "\" in set containing " + set.keySet());
			} else {
				return result;
			}
			
		//If it's a function call, e.g. { a = 2; }.a
		} else if (expr2.getExpression() instanceof FL_FunctionCall) {
			FL_FunctionCall func = (FL_FunctionCall) expr2.getExpression();
			
			if(func.hasBrackets()) {
				return applySelect(expr1, new Closure(expr2.getScope(), Evaluator.evaluate(expr2)));
			}
			
			if (func.getInitFunction() instanceof FL_Var) {
				FL_Var var = (FL_Var) func.getInitFunction();
				Object result = set.get(var.getName());
				if (result == null) {
					throw new Exception("Cannot find element \"" + var.getName() + "\" in set containing " + set.keySet());
				} else {
					
					if(func.getArguments().isEmpty()) {
						return result;
					} else {
						func.setInitFunction(result);
//								func.setInitFunction(new Closure(expr2.getScope(), result));
						Scope newScope = new Scope(set);
						newScope.putAll(expr2.getScope());
						return Evaluator.evaluate(new Closure(newScope, func));
					}
					
//						return result;
				}
			}
		}
			
		return null;
	}

	public Object applyContains(Object o1, Object o2) throws Exception {
		if(o1 instanceof Closure) {
			Closure closure1 = (Closure) o1;
			o1 = closure1.getExpression();
		}
		if (o1 instanceof FL_List) {
			FL_List l1 = (FL_List) o1;
			return l1.contains(o2); 
		} else if (o1 instanceof FL_String) {
			FL_String s1 = (FL_String) o1;
			if (o2 instanceof FL_String) {
				FL_String s2 = (FL_String) o2;
				return s1.stringValue().contains(s2.stringValue());
			}
		} else if (o1 instanceof FL_Set) {
			FL_Set s1 = (FL_Set) o1;
			if (o2 instanceof FL_String) {
				FL_String s2 = (FL_String) o2;
				return s1.containsKey(s2.stringValue());
			}
		}
		throw new Exception("Cannot check for existance within a " + o1.getClass().getName());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object applyConcat(Object o1, Object o2) throws Exception {
		if (o1 instanceof LinkedList && o2 instanceof LinkedList) {
			LinkedList l1 = (LinkedList) o1;
			LinkedList l2 = (LinkedList) o2;
			l1.addAll(l2);
			return l1;
		} else {
			Exceptions.CANNOT_CONCAT(o1, o2);
			return null;
		}
	}

	public boolean applyComparator(Object o1, Object o2) throws Exception {
		switch (op) {
		case ">":
			return ((BigDecimal) o1).compareTo((BigDecimal) o2) > 0;
		case "<":
			return ((BigDecimal) o1).compareTo((BigDecimal) o2) < 0;
		case "<=":
			return ((BigDecimal) o1).compareTo((BigDecimal) o2) <= 0;
		case ">=":
			return ((BigDecimal) o1).compareTo((BigDecimal) o2) >= 0;
		case "==":
			if(o1 instanceof Closure && o2 instanceof Closure) {
				Closure c1 = (Closure) o1;
				Closure c2 = (Closure) o2;
				return c1.getExpression().equals(c2.getExpression());
			}
			return o1.equals(o2);
		case "!=":
			if(o1 instanceof Closure && o2 instanceof Closure) {
				Closure c1 = (Closure) o1;
				Closure c2 = (Closure) o2;
				return c1.getExpression().equals(c2.getExpression());
			}
			return !o1.equals(o2);
		}
		throw new Exception("Invalid operator (expected comparator operator");
	}

	public BigDecimal applyNumber(BigDecimal f1, BigDecimal f2) throws Exception {
		if (operatorKind != Operator.NUMERICAL) {
			throw new Exception("Invalid operator (expected numerical operator)");
		}

		switch (op) {
		case "+":
			return f1.add(f2);
		case "*":
			return f1.multiply(f2);
		case "-":
			return f1.subtract(f2);
		case "/":
			try {
				return f1.divide(f2, 128, RoundingMode.HALF_DOWN);
			} catch (ArithmeticException e) {
				Exceptions.DIV_BY_ZERO(f1);
			}
		case "%":
			return f1.remainder(f2);
		}
		throw new Exception("Invalid operator " + op);
	}

	public boolean applyBoolean(boolean b1, boolean b2) throws Exception {
		if (operatorKind != Operator.BOOLEAN) {
			throw new Exception("Invalid operator (expected boolean operator)");
		}
		switch (op) {
		case "||":
			return b1 || b2;
		case "&&":
			return b1 && b2;
		}
		throw new Exception("Invalid operator " + op);
	}

	public FL_Set applySetObjects(FL_Set s1, FL_Set s2) throws Exception {
		if (operatorKind != Operator.SET) {
			throw new Exception("Invalid operator (expected set operator)");
		}

		FL_Set newSet = s1.copy();

		switch (op) {
		// union
		case "/+": {
			newSet.putAll(s2);
			break;
		}
		// intersect
		case "/-": {
			newSet.putAll(s1);
			newSet.keySet().retainAll(s2.keySet());
			for (String key : newSet.keySet()) {
				newSet.put(key, s1.get(key));
			}
			break;
		}
		// difference
		case "//": {
			for (String key : s2.keySet()) {
				newSet.remove(key);
			}
			break;
		}
		}
		return newSet;
	}
}