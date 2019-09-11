public class FL_TypedParam implements Evaluatable {
	private final String name;
	private final Type type;

	public FL_TypedParam(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public Type getType() {
		return this.type;
	}

	@Override public String toString() {
		return name + ":" + type.name().toLowerCase();
	}

	@Override
    public boolean equalsWithScope(Object o, Scope scope) { 
		return false;
	}
}