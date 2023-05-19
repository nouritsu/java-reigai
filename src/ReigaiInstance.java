import java.util.HashMap;
import java.util.Map;

class ReigaiInstance {
    private ReigaiClass cl;
    private final Map<String, Object> fields = new HashMap<>();

    ReigaiInstance(ReigaiClass cl) {
        this.cl = cl;
    }

    Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }
        throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    @Override
    public String toString() {
        return "<instance of class " + cl.name + ">";
    }

}
