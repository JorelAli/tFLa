public class FL_Var implements Evaluatable {
	private final String name;

	public FL_Var(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override public String toString() {
		return "Var[" + name + "]";
	}

    @Override
    public boolean equalsWithScope(Object o, Scope scope) { 
		if(o instanceof FL_Var) {
			FL_Var var = (FL_Var) o;
			return var.getName().equals(this.name);
		}
		return false;
	}
}