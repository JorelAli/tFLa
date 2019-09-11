import java.util.LinkedList;
import java.util.Collection;

@SuppressWarnings("serial")
public class FL_String extends LinkedList<Character> implements Evaluatable {

    public FL_String(Collection<Character> e) {
        super(e);
    }

    @Override
    public boolean addAll(Collection<? extends Character> e) throws RuntimeException {
        for(Object o : e) {
            if(!(o instanceof Character)) {
                throw new RuntimeException("Tried to add " + o.getClass().getSimpleName() + " to a String");
            }
        }
        return super.addAll(e);
    }

    public FL_String(String str) {
        for(char c : str.toCharArray()) {
            this.add(c);
        }
    }

    public String stringValue() {
        StringBuilder builder = new StringBuilder();
        for(char c : this) {
            builder.append(c);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "\"" + stringValue() + "\"";
    }

    @Override
    public boolean equalsWithScope(Object o, Scope scope) throws Exception { 
        if(o instanceof FL_String) {
            FL_String other = (FL_String) o;
            return this.stringValue().equals(other.stringValue());
        } else {
            return false; 
        }
    }
}  