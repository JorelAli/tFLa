public class FL_Var {
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
}