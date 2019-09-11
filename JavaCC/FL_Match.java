import java.util.LinkedHashMap;

public class FL_Match implements Evaluatable {
    private final LinkedHashMap<Object, Object> statements;
    private Object finalStatement;
    private final Object matchOn;

    public FL_Match(Object matchOn) { 
        this.statements = new LinkedHashMap<Object, Object>(); 
        this.matchOn = matchOn;
    }

    public void addStatement(Object key, Object value) {
        this.statements.put(key, value);
    }

    public void setFinalStatement(Object finalStatement) {
        this.finalStatement = finalStatement;
    }

    public Object getMatchOn() {
        return this.matchOn;
    }

    public LinkedHashMap<Object, Object> getStatements() {
        return this.statements;
    }

    public Object getFinalStatement() {
        return this.finalStatement;
    }

    @Override public String toString() {
      return "match " + matchOn + " [stmts=" + statements + ", finalStmt=" + finalStatement + "]";
    }

    @Override
    public boolean equalsWithScope(Object o, Scope scope) { 
		return false;
    }
}