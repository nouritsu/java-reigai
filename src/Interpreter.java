import java.util.List;
import java.util.ArrayList;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    final Environment globals = new Environment(null);
    private Environment environment = globals;

    Interpreter() {
        globals.define("clock", new ReigaiCallable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fun>";
            }
        });

        globals.define("len", new ReigaiCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                if (arguments.get(0) instanceof Double) {
                    return null;
                }
                return arguments.get(0).toString().length();
            }

            @Override
            public String toString() {
                return "<native fun>";
            }
        });

        globals.define("floor", new ReigaiCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                if (arguments.get(0) instanceof Double) {
                    return Math.floor((Double) arguments.get(0));
                }
                return null;
            }

            @Override
            public String toString() {
                return "<native fun>";
            }
        });

        globals.define("ceil", new ReigaiCallable() {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                if (arguments.get(0) instanceof Double) {
                    return Math.ceil((Double) arguments.get(0));
                }
                return null;
            }

            @Override
            public String toString() {
                return "<native fun>";
            }
        });
    }

    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Reigai.runtime_error(error);
        }
    }

    @Override
    public Object visit_literal_expr(Expr.Literal expr) {
        return expr.value; // no need to run evaluate() as it is atomic
    }

    @Override
    public Object visit_logical_expr(Expr.Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            if (is_truthy(left))
                return left;
        } else {
            if (!is_truthy(left)) {
                return left;
            }
        }

        return evaluate(expr.right);
    }

    @Override
    public Object visit_variable_expr(Expr.Variable expr) {
        return environment.get(expr.name);
    }

    @Override
    public Object visit_assign_expr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Object visit_grouping_expr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visit_unary_expr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                check_number_operand(expr.operator, right);
                return -(double) right;
            case BANG:
                return !is_truthy(right);
            default: // Unreachable
                return null;
        }
    }

    private void check_number_operand(Token operator, Object operand) {
        if (operand instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void check_number_operands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private boolean is_truthy(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;

        return true;
    }

    @Override
    public Object visit_binary_expr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            // COMPARISON
            case GREATER:
                check_number_operands(expr.operator, left, right);
                return (Double) left > (Double) right;
            case GREATER_EQUAL:
                check_number_operands(expr.operator, left, right);
                return (Double) left >= (Double) right;
            case LESSER:
                check_number_operands(expr.operator, left, right);
                return (Double) left < (Double) right;
            case LESSER_EQUAL:
                check_number_operands(expr.operator, left, right);
                return (Double) left <= (Double) right;

            // EQUALITY
            case EQUAL_EQUAL:
                return is_equal(left, right);
            case BANG_EQUAL:
                return !is_equal(left, right);

            // ARITHMETIC
            case MINUS:
                check_number_operands(expr.operator, left, right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                return left.toString() + right.toString();
            case SLASH:
                check_number_operands(expr.operator, left, right);
                return (double) left / (double) right;
            case STAR:
                check_number_operands(expr.operator, left, right);
                return (double) left * (double) right;
            case MOD:
                check_number_operands(expr.operator, left, right);
                return (double) left % (double) right;
            default: // Unreachable
                return null;
        }
    }

    @Override
    public Object visit_call_expr(Expr.Call expr) {
        Object callee = evaluate(expr.callee);

        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }

        if (!(callee instanceof ReigaiCallable)) {
            throw new RuntimeError(expr.paren, "Can only call functions and classes.");
        }

        ReigaiCallable function = (ReigaiCallable) callee;
        if (arguments.size() != function.arity()) {
            throw new RuntimeError(expr.paren,
                    "Expected " + function.arity() + " arguments, found " + arguments.size() + ".");
        }
        return function.call(this, arguments);
    }

    private boolean is_equal(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;

        return a.equals(b);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    public void execute_block(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment; // "Go" up a scope
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    @Override
    public Void visit_block_stmt(Stmt.Block stmt) {
        execute_block(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visit_expression_stmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visit_function_stmt(Stmt.Function stmt) {
        ReigaiFunction function = new ReigaiFunction(stmt);
        environment.define(stmt.name.lexeme, function);
        return null;
    }

    @Override
    public Void visit_if_stmt(Stmt.If stmt) {
        if (is_truthy(evaluate(stmt.condition))) {
            execute(stmt.then_branch);
        } else if (stmt.else_branch != null) {
            execute(stmt.else_branch);
        }
        return null;
    }

    @Override
    public Void visit_print_stmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visit_return_stmt(Stmt.Return stmt) {
        Object value = null;
        if (stmt.value != null)
            value = evaluate(stmt.value);
        throw new Return(value);
    }

    @Override
    public Void visit_var_stmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }

        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visit_while_stmt(Stmt.While stmt) {
        while (is_truthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    private String stringify(Object object) {
        if (object == null)
            return "nil";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }
}