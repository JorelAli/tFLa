

public class FL_OpExpr implements Evaluatable {
	private Object leftExpr;
	private Object rightExpr;
	private Token operator;

	//Indicates whether this OpExpr is surrounded by brackets
	private final boolean bracketed;
	
	public FL_OpExpr(Object left, Object right, Token operator, boolean bracketed) {
		this.leftExpr = left;
		this.rightExpr = right;
		this.operator = operator;
		this.bracketed = bracketed;
	}

	public Object getLeftExpr() {
		return this.leftExpr;
	}

	public Object getRightExpr() {
		return this.rightExpr;
	}

	public Token getOperator() {
		return this.operator;
	}

	public boolean hasBrackets() {
		return this.bracketed;
	}

	@Override public String toString() {
		StringBuilder builder = new StringBuilder("(");
		builder.append(leftExpr);
		builder.append(" ");
		builder.append(operator.image);
		builder.append(" ");
		builder.append(rightExpr);
		return builder.append(")").toString();
	}

	@Override
    public boolean equalsWithScope(Object o, Scope scope) { 
		return false;
	}
}