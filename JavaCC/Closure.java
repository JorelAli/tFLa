public class Closure { 

    private Scope scope;
    private Object expression;

    public Closure(Scope scope, Object expression) {
        this.scope = scope.copy();
        this.expression = expression;
    }

    public Closure(Object expression) {
        this(new Scope(), expression);
    }

    public Scope getScope() {
        return this.scope.copy();
    }

    public Object getExpression() {
        return this.expression;
    }
}  