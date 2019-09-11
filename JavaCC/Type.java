import java.math.BigDecimal;

public enum Type { 
    NUMBER, LIST, SET, FUNCTION, BOOLEAN, STRING, ANY;

    public static boolean isType(Object o, Type type) {
        switch(type) {
            case NUMBER:
                return o instanceof BigDecimal;
            case LIST:
                return o instanceof FL_List;
            case SET:
                return o instanceof FL_Set;
            case FUNCTION:
                return o instanceof FL_FunctionCall || o instanceof FL_Function;
            case BOOLEAN:
                return o instanceof Boolean;
            case STRING:
                return o instanceof FL_String;
            case ANY:
                return true;
        }
        return false;
    }
}  