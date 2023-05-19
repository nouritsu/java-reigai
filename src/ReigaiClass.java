import java.util.List;
import java.util.Map;

class ReigaiClass implements ReigaiCallable {
    final String name;

    ReigaiClass(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "<class " + name + ">";
    }
}
