import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Evaluator {
		
	public static Object evaluate(Closure closure) throws Exception {
		if(closure == null || closure.getExpression() == null) {
			throw new Exception("Tried to evaluate nothing");
		}
		Print.EVAL("Starting evaluation of " + closure + "(" + closure.getExpression().getClass().getSimpleName() + "), with scope " + closure.getScope());
		
		if(!(closure.getExpression() instanceof Evaluatable)) {
			return closure.getExpression();
		}
				
		if(closure.getExpression() instanceof FL_Builtin) {
			return evaluateBuiltin((FL_Builtin) closure.getExpression(), closure.getScope());
		} else if(closure.getExpression() instanceof FL_Function) {
		  	FL_Function function = (FL_Function) closure.getExpression();
		  	FL_FunctionCall newFunctionCall = new FL_FunctionCall();
		  	newFunctionCall.setInitFunction(function);
//		  	return newFunctionCall;
			return new Closure(new Scope(closure.getScope()), newFunctionCall);
		} else if(closure.getExpression() instanceof FL_FunctionCall) {
			return new SECD(closure).runSECD();
		} else if(closure.getExpression() instanceof FL_Guards) {
			FL_Guards guards = (FL_Guards) closure.getExpression();
			return evaluateGuards(guards, closure.getScope());
		} else if(closure.getExpression() instanceof FL_IncludedSet) {
			//Construct a new closure containing all of the stuff from the 
			//current closure's scope and the stuff in the included set
			
			FL_IncludedSet includedSet = (FL_IncludedSet) closure.getExpression();
			Scope scope = new Scope();
			scope.putAll(closure.getScope());
			scope.putAll(includedSet.getFLSet());
			Closure newClosure = new Closure(scope, includedSet.getExpression());
			
			return evaluate(newClosure);
			
		} else if(closure.getExpression() instanceof FL_List) {
			FL_List list = new FL_List((FL_List) closure.getExpression());
			ListIterator<Object> iterator = list.listIterator(0);
			while(iterator.hasNext()) {
				iterator.set(evaluate(new Closure(closure.getScope(), iterator.next())));
			}
			return list;
		} else if(closure.getExpression() instanceof FL_Match) {
			FL_Match match = (FL_Match) closure.getExpression();
			return evaluateMatch(match, closure.getScope());
		} else if(closure.getExpression() instanceof FL_OpExpr) {
			return evaluateOpExpr(closure.getScope(), (FL_OpExpr) closure.getExpression());
		} else if(closure.getExpression() instanceof FL_Set) {
			return Evaluator.evaluateFully(closure);
//			return closure;//.getExpression(); //TODO: Actually need to keep the closure though
		} else if(closure.getExpression() instanceof FL_String) {
			return closure.getExpression();
		} else if(closure.getExpression() instanceof FL_TypedParam) {
			throw new Exception("Wat? You can't evaluate a TypedParam");
		} else if(closure.getExpression() instanceof FL_Var) {
			//Resolves an FL_Var into whatever it is defined as in the current closure.
			//Since FL_TypedParam exists, FL_Var is therefore ALWAYS going to be a
			//function call of some form,
			
			//EXCEPTIONS:
			// - Set accessing
			
			FL_Var flVar = (FL_Var) closure.getExpression();
			Object result = closure.getScope().get(flVar.getName());
			if(result == null) {
				FL_Builtin potentialBuiltin = FL_Builtin.fromString(flVar.getName());
				if(potentialBuiltin != null) {
					return potentialBuiltin;
				}
				Exceptions.FUNCTION_NOT_DEFINED(flVar.getName());
			}
			
			return evaluate(new Closure(closure.getScope(), result));
		}
			
		return null;
	}
	
	private static Object evaluateGuards(FL_Guards guards, Scope closureScope) throws Exception {
		Object statement = guards.getFinalStatement();
		for(Object guardExpr : guards.getStatements().keySet()) {
			Object predicate = evaluate(new Closure(closureScope, guardExpr));
			if(predicate instanceof Boolean) {
				if((boolean) predicate) {
					statement = guards.getStatements().get(guardExpr);
					break;
				}
			} else {
				throw new Exception(predicate + " is not a valid Boolean object in guard expression!");
			}
		}
		return evaluate(new Closure(closureScope, statement));
	}
	
	private static Object evaluateMatch(FL_Match match, Scope closureScope) throws Exception {
		//Thing we're matching, e.g. `x` in `match x`
		Object matchOn = evaluate(new Closure(closureScope, match.getMatchOn()));
		Object statement = match.getFinalStatement();

		for(Object matchExpr : match.getStatements().keySet()) {
			// | result ->> statement
			Object result = evaluate(new Closure(closureScope, matchExpr));

			if(result instanceof Pattern && matchOn instanceof FL_String) {
				Pattern pattern = (Pattern) result;
				String matchOnString = ((FL_String) matchOn).stringValue();
				if(pattern.matcher(matchOnString).matches()) {
					statement = match.getStatements().get(matchExpr);
					break;
				}
			} else if(matchOn.equals(result)) {
				statement = match.getStatements().get(matchExpr);
				break;
			}
		}
		return evaluate(new Closure(closureScope, statement));
	}
	
	@SuppressWarnings("unchecked")
	public static Object evaluateBuiltin(FL_Builtin builtin, Scope closureScope) throws Exception {
		Print.EVAL("About to process builtin: " + builtin.getType());
		Object builtinParam = null;
		if(builtin.getParameter() != null) {
			Print.EVAL(builtin.getParameter().getClass().getName());
			Print.EVAL(builtin.getParameter());
			Print.EVAL("Builtin scope: ", closureScope);
			builtinParam = evaluate(new Closure(closureScope, builtin.getParameter()));
		}
		
		switch(builtin.getType()) {
			case IMPORT:
				if(!(builtinParam instanceof FL_String)) {
					throw new Exception("Expected a string for import file name");
				}
				File file = new File(((FL_String) builtinParam).stringValue());
				if(!file.exists()) {
					Exceptions.FILE_NOT_FOUND(file);
				}
				return new ForteLang(new FileInputStream(file)).input(closureScope);
			case PRINT:
				System.out.println(((FL_String) builtinParam).stringValue());
				return builtinParam;
			case EXEC:
				if(builtinParam instanceof FL_String) {
					int result = Runtime.getRuntime().exec(((FL_String) builtinParam).stringValue()).waitFor();
					return new BigDecimal(result);
				} else {
					throw new Exception("Needed a String, but got a " + builtinParam.getClass().getName());
				}
			case HEAD:
				if(!(builtinParam instanceof LinkedList)) {
					throw new Exception("head function requires a list as a parameter");
				} else {
				  	LinkedList<?> list = ((LinkedList<?>) builtinParam);
				  	if(list.isEmpty()) {
				  		Exceptions.HEAD_ON_EMPTY_LIST();
				  	}
				  	if(builtinParam instanceof FL_String) {
				  		return evaluate(new Closure(closureScope, new FL_String(String.valueOf(list.getFirst()))));
				  	} else {
				  		return evaluate(new Closure(closureScope, list.getFirst()));
				  	}
				  	
				} 
			case TAIL:
				if(!(builtinParam instanceof LinkedList)) {
					throw new Exception("tail function requires a list as a parameter, not a " + builtinParam.getClass().getName());
				} else {
				  	@SuppressWarnings("rawtypes")
					LinkedList list = ((LinkedList) builtinParam);
				  	if(list.isEmpty()) {
				  		Exceptions.TAIL_ON_EMPTY_LIST();
				  	}
				  	Evaluatable result = null;
				  	if(builtinParam instanceof FL_List) {
				  	  	result = new FL_List(list.subList(1, list.size()));
				  	} else if(builtinParam instanceof FL_String) {
						result = new FL_String(list.subList(1, list.size()));
				  	}
					return result;
				  	
				}
			case INPUT:
				if(!(builtinParam instanceof FL_String)) {
					throw new Exception("Expected a string for input");
				}
				System.out.println("[@input] " + ((FL_String) builtinParam).stringValue());
				System.out.print("@input> ");
				return new FL_String(ForteLang.getGlobalScanner().nextLine());
			case INPUTBOX:
				if(!(builtinParam instanceof FL_String)) {
					throw new Exception("Expected a string for inputbox");
				}
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				String inputBox = JOptionPane.showInputDialog(null, ((FL_String) builtinParam).stringValue(), "ForteLang @inputbox", -1);
				return new FL_String(inputBox == null ? "" : inputBox);
			case ABORT:
				System.out.println("Program aborted with " + builtinParam);
				System.exit(0);
				break;
			case STD:
				return new ForteLang(ForteLang.class.getResourceAsStream("std.fl")).input(closureScope);
			case READ:
				if(!(builtinParam instanceof FL_String)) {
					throw new Exception("Expected a string for file name");
				}
				File file2 = new File(((FL_String) builtinParam).stringValue());
				if(!file2.exists()) {
					Exceptions.FILE_NOT_FOUND(file2);
				}
				return new FL_String(new String(Files.readAllBytes(file2.toPath())));
			
		default:
			break;
		}
		return null;
	}
	
	static class LeftBracket { public String toString () { return "("; }}
	static class RightBracket { public String toString () { return ")"; }}

	static class OpFlattener {
		private final FL_OpExpr expr;

		public OpFlattener(FL_OpExpr expr) {
			this.expr = expr;
		}

		public LinkedList<Object> flatten() {
			LinkedList<Object> elements = new LinkedList<Object>();
			if(expr.hasBrackets()) {
				elements.add(new LeftBracket());
			}

			if(expr.getLeftExpr() instanceof FL_OpExpr) {
				elements.addAll(new OpFlattener((FL_OpExpr) expr.getLeftExpr()).flatten());
			} else {
				elements.add(expr.getLeftExpr());
			}
			
			elements.add(expr.getOperator());
			
			if(expr.getRightExpr() instanceof FL_OpExpr) {
				elements.addAll(new OpFlattener((FL_OpExpr) expr.getRightExpr()).flatten());
			} else {
				elements.add(expr.getRightExpr());
			}

			if(expr.hasBrackets()) {
				elements.add(new RightBracket());
			}
			return elements;
		}
	}

	static class Op {
		private Token token;
	  
		public Op(Object token) {
			this.token = (Token) token;
		}

		public int precedence() {
			switch(token.image) {
				case ".": return 10;
				case "?": return 9;
				case "++": return 8;
				case "*":
				case "%":
				case "/": return 7;
				case "+":
				case "-": return 6;
				case "/+":
				case "/-":
				case "//": return 5;
				case "<":
				case "<=":
				case ">":
				case ">=": return 4;
				case "==":
				case "!=": return 3;
				case "&&":
				case "||": return 2;
			}
			return 0;
		}

		public boolean rightAssociative(Object other) {
		  	switch(token.image) {
				case ".":
					return token.image.equals(((Token) other).image);
		  	}
		  	return false;
		}
	}
	

	public static Object evaluateOpExpr(Scope scope, FL_OpExpr flOpExpr) throws Exception {
		Print.OPEX("About to evaluate OpExpr");
	  	Print.OPEX("Current scope: ", scope);		
		Print.OPEX(flOpExpr);

		Print.OPEX("Phase 1: Flattening");

		//Flatten the elements of OpExpr into a linked list for evaluation
		LinkedList<Object> elements = new OpFlattener(flOpExpr).flatten();
		Print.OPEX("    Flattened: ", elements);
		
		Print.OPEX("Phase 2: Shunting-yard");

		Stack<Object> stack = new Stack<Object>();
		LinkedList<Object> output = new LinkedList<Object>();
		while(!elements.isEmpty()) {
		  	Object element = elements.pop();
		  	if(element instanceof Token) {
				while(
				  	!stack.isEmpty() &&
					stack.peek() instanceof Token &&
					(
					  new Op(stack.peek()).precedence() > new Op(element).precedence()
					  || new Op(stack.peek()).rightAssociative(element) 
					)
				) {
					output.add(stack.pop());
				}
				
				stack.push(element);
		  	} else if(element instanceof LeftBracket) {
				stack.push(element);
		  	} else if(element instanceof RightBracket) {
				while(!(stack.peek() instanceof LeftBracket)) {
					output.add(stack.pop());
				}
				stack.pop();
		  	} else { //It's a "number"
				output.add(element);
		  	}
		}
		while(!stack.isEmpty()) {
			output.add(stack.pop());
		}
		
		Print.OPEX("    Finished Shunting Yard: ", output);
		Print.OPEX("Phase 3: Evaluation");

		Stack<Object> evalStack = new Stack<Object>();
		while(!output.isEmpty()) {
			evalStack.push(output.pop());
			if(evalStack.peek() instanceof Token) {
				Token operator = (Token) evalStack.pop();
				Object secondExpr = evalStack.pop();
				Object firstExpr = evalStack.pop();

				Print.OPEX("    Applying the " + operator.image + " operator, on " + firstExpr + " and " + secondExpr);
				
				if(!(firstExpr instanceof Closure)) {
					firstExpr = new Closure(scope, firstExpr);
				}
				
				if(!(secondExpr instanceof Closure)) {
					secondExpr = new Closure(scope, secondExpr);
				}
				
				Object result = new OperatorParser(operator).apply((Closure) firstExpr, (Closure) secondExpr);
				evalStack.push(result);
			}
		}

		Object result = evalStack.pop();
		Print.OPEX("    Finished evaluation: ", result);

		if(result instanceof Evaluatable) {
//			return evaluate(scope, result);
		} else { 
			return result;
		}
		return result;
	}
	
	public static Object evaluateFully(Closure closure) throws Exception {
		Print.EVAL("Evaluating fully: " + closure);
		if(closure.getExpression() instanceof FL_Set) {
			FL_Set set = (FL_Set) closure.getExpression();
			FL_Set iteratorCopy = new FL_Set(set);
			
			Scope evaluationScope = closure.getScope();
			evaluationScope.putAll(set);
			
			for(String attr : iteratorCopy.keySet()) {
				Object result = Evaluator.evaluate(new Closure(evaluationScope, iteratorCopy.get(attr)));
				set.replace(attr, result);
			}
			return set;
		}
		Print.EVAL("Failed to evaluate fully");
		return closure;
		
	}
}