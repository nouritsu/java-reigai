import java.util.List;

interface ReigaiCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);
}
