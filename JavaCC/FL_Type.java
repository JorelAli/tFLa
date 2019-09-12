import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FL_Type {
	
	LinkedList<String> types;
	
	public String getType() {
		return types.toString().replace("[", "").replace("]", "").replace(",", " ->");
	}

	public FL_Type(Token token) {
		types = new LinkedList<>();
		types.add(token.image);
	}
	
	public FL_Type(LinkedList<FL_Type> tokens) {
		types = new LinkedList<>();
		
		List<String> a = tokens.stream().map(t -> t.getType()).collect(Collectors.toList());
		types.addAll(a);
	}
	
}
