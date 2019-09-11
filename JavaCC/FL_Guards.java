import java.util.LinkedHashMap;

public class FL_Guards implements Evaluatable {
    private LinkedHashMap<Object, Object> statements;
    private Object finalStatement;

    public FL_Guards() { 
        statements = new LinkedHashMap<Object, Object>(); 
    }

    public void addStatement(Object key, Object value) {
        statements.put(key, value);
    }

    public void setFinalStatement(Object finalStatement) {
        this.finalStatement = finalStatement;
    }

    public LinkedHashMap<Object, Object> getStatements() {
        return this.statements;
    }

    public Object getFinalStatement() {
        return this.finalStatement;
    }

    @Override public String toString() {
      StringBuilder builder = new StringBuilder("Guard{ ");
      for(Object key : statements.keySet()) {
          builder.append("| ");
          builder.append(key);
          builder.append(" ->> ");
          builder.append(statements.get(key));
          builder.append(" ");
      }
      builder.append("| ->> ");
      builder.append(finalStatement);
      return builder.append(" }").toString();
    }

    @Override
    public boolean equalsWithScope(Object o, Scope scope) { 
        return false; 
    }
}