import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class FL_Set extends LinkedHashMap<String, Object> implements Evaluatable {

	public FL_Set() {
		super();
	}

	public FL_Set(Map<String, Object> map) {
		super(map);
	}

	public FL_Set copy() {
		return new FL_Set(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof FL_Set) {
			
			try {
				
				FL_Set set = (FL_Set) o;
				
				FL_Set thisSet = new FL_Set(this);
				FL_Set other = new FL_Set(set);
				
				thisSet = (FL_Set) Evaluator.evaluateFully(new Closure(thisSet));
				other = (FL_Set) Evaluator.evaluateFully(new Closure(other));
				
				for(String key : this.keySet()) {
					if(!set.containsKey(key)) {
						return false;
					}
					if(!this.get(key).equals(set.get(key))) {
						return false;
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
		
	}

	@Override
	public boolean equalsWithScope(Object o, Scope scope) throws Exception {
//		if(o instanceof FL_Set) {
//			FL_Set set = (FL_Set) o;
//			if(this.keySet().equals(set.keySet())) {
//				for(String key : this.keySet()) {
//					Object o1 = ForteLang.evaluate(scope, this.get(key));
//					Object o2 = ForteLang.evaluate(scope, set.get(key));
//					if(!o1.equals(o2)) {
//						return false;
//					}
//				}
//				return true;
//			} else {
//				System.out.println("Different keysets");
//				return false;
//			}
//		} else {
//			System.out.println("Not FL_Set");
//			return false;
//		}
		return false;
	}
}