public class FL_Builtin implements Evaluatable {

	enum Builtin { 
		IMPORT, 
		PRINT, 
		EXEC, 
		HEAD, 
		TAIL, 
		INPUT, 
		INPUTBOX,
		ABORT, 
		LENGTH,
		STD,
		READ
	}

	private final Object param;
	private final Builtin type;

	public FL_Builtin(Builtin type, Object param) {
		this.type = type;
		this.param = param;
	}

	public Object getParameter() {
		return this.param;
	}

	public Builtin getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "builtin:" + type.name().toLowerCase();
	}

	@Override
    public boolean equalsWithScope(Object o, Scope scope) { 
		return false;
	}
	
	public static FL_Builtin fromString(String str) throws Exception {
		if(!ForteLang.impureMode && str.startsWith("@")) {
			throw new Exception("Tried to execute impure function \"" + str + "\". Use -impure flag to allow impure code");
		}
		switch(str) {
			case "head": return new FL_Builtin(FL_Builtin.Builtin.HEAD, null);
			case "tail": return new FL_Builtin(FL_Builtin.Builtin.TAIL, null);
			case "std": return new FL_Builtin(FL_Builtin.Builtin.STD, null);
			case "@exec": return new FL_Builtin(FL_Builtin.Builtin.EXEC, null);
			case "@import": return new FL_Builtin(FL_Builtin.Builtin.IMPORT, null);
			case "@abort": return new FL_Builtin(FL_Builtin.Builtin.ABORT, null);
			case "@print": return new FL_Builtin(FL_Builtin.Builtin.PRINT, null);			
			case "@input": return new FL_Builtin(FL_Builtin.Builtin.INPUT, null);
			case "@inputbox": return new FL_Builtin(FL_Builtin.Builtin.INPUTBOX, null);
			case "@read": return new FL_Builtin(FL_Builtin.Builtin.READ, null);

		}
		Print.EVAL("Failed to parse Builtin from \"" + str + "\"");
		return null;
	}
}