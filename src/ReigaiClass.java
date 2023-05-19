import java.util.List;
import java.util.Map;

class ReigaiClass implements ReigaiCallable {
    final String name;
    private final Map<String, ReigaiFunction> methods;

    ReigaiClass(String name, Map<String, ReigaiFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        ReigaiInstance in = new ReigaiInstance(this);
        return in;
    }

    ReigaiFunction find_method(String name) {
        return methods.containsKey(name) ? methods.get(name) : null;
    }

    @Override
    public String toString() {
        return "<class " + name + ">";
    }
}
