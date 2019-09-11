import java.util.LinkedList;
import java.util.Collection;

@SuppressWarnings("serial")
public class FL_List extends LinkedList<Object> implements Evaluatable {

    public FL_List(Collection<Object> e) {
        super(e);
    }

    public FL_List() {
        super();
    }

    @Override
    public boolean equalsWithScope(Object o, Scope scope) throws Exception { 
//        if(o instanceof FL_List) {
//            FL_List other = (FL_List) o;
//            if(this.size() == other.size()) {
//                for(int i = 0; i < this.size(); i++) {
//                    Object o1 = ForteLang.evaluate(scope, this.get(i));
//                    Object o2 = ForteLang.evaluate(scope, other.get(i));
//                    if(!o1.equals(o2)) {
//                        return false;
//                    }
//                }
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false; 
//        }
        return false;
    }
}  