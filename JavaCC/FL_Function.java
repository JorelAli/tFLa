public class FL_Function implements Evaluatable {
	private final FL_TypedParam parameter;
	private final Object expression;

	public FL_Function(FL_TypedParam parameter, Object expression) {
		this.parameter = parameter;
		this.expression = expression;
	}

	public FL_TypedParam getParameter() {
		return this.parameter;
	}

	public Object getExpression() {
		return this.expression;
	}

	@Override 
	public String toString() { 
		return parameter + " ->"/* + expression*/; 
	}

	@Override
    public boolean equalsWithScope(Object o, Scope scope) { 
		return false;
	}
}