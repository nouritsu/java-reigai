import java.util.List;

class ReigaiFunction implements ReigaiCallable {
    private final Stmt.Function declaration;
    private final Environment closure;
    private final boolean is_initializer;

    ReigaiFunction(Stmt.Function declaration, Environment closure, boolean is_initializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.is_initializer = is_initializer;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.execute_block(declaration.body, environment);
        } catch (Return ret) {
            return is_initializer ? closure.get_at(0, "this") : ret.value;
        }

        if (is_initializer)
            return closure.get_at(0, "this");

        return null;
    }

    ReigaiFunction bind(ReigaiInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new ReigaiFunction(declaration, environment, is_initializer);
    }

    @Override
    public String toString() {
        return "<fun " + declaration.name.lexeme + ">";
    }
}
