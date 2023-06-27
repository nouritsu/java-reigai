import java.util.List;
import java.util.Map;

class ReigaiClass implements ReigaiCallable {
    final String name;
    final ReigaiClass superclass;
    private final Map<String, ReigaiFunction> methods;

    ReigaiClass(String name, ReigaiClass superclass, Map<String, ReigaiFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }

    @Override
    public int arity() {
        ReigaiFunction init = find_method("init");
        if (init == null)
            return 0;
        return init.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        ReigaiInstance in = new ReigaiInstance(this);
        ReigaiFunction init = find_method("init");
        if (init != null)
            init.bind(in).call(interpreter, arguments);
        return in;
    }

    ReigaiFunction find_method(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        if (superclass != null) {
            return superclass.find_method(name);
        }
        return null;
    }

    @Override
    public String toString() {
        return "<class " + name + ">";
    }
}
