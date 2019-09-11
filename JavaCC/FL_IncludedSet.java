public class FL_IncludedSet implements Evaluatable {
	private final FL_Set set;
	private final Object expression;

	public FL_IncludedSet(FL_Set set, Object expression) {
		this.set = set;
		this.expression = expression;
	}

	public Object getExpression() {
		return this.expression;
	}

	public FL_Set getFLSet() {
		return this.set;
	}

	@Override public String toString() {
	  return "FL_IncludedSet[set=" + set + ", expr=" + expression + "]";
	}
	
    @Override
    public boolean equalsWithScope(Object o, Scope scope) { 
		return false; 
	}
}