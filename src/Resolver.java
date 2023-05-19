import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class Resolver implements Expr.Visitor<Void>, Stmt.Visitor<Void> {
    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    private FunctionType current_function = FunctionType.NONE;

    Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    private enum FunctionType {
        NONE,
        FUNCTION,
    }

    void resolve(List<Stmt> statements) {
        for (Stmt statement : statements) {
            resolve(statement);
        }
    }

    @Override
    public Void visit_block_stmt(Stmt.Block stmt) {
        begin_scope();
        resolve(stmt.statements);
        end_scope();
        return null;
    }

    @Override
    public Void visit_class_stmt(Stmt.Class stmt) {
        declare(stmt.name);
        define(stmt.name);
        return null;
    }

    @Override
    public Void visit_expression_stmt(Stmt.Expression stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visit_function_stmt(Stmt.Function stmt) {
        declare(stmt.name);
        define(stmt.name);

        resolve_function(stmt, FunctionType.FUNCTION);
        return null;
    }

    @Override
    public Void visit_if_stmt(Stmt.If stmt) {
        resolve(stmt.condition);
        resolve(stmt.then_branch);
        if (stmt.else_branch != null)
            resolve(stmt.else_branch);
        return null;
    }

    @Override
    public Void visit_print_stmt(Stmt.Print stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visit_return_stmt(Stmt.Return stmt) {
        if (current_function == FunctionType.NONE) {
            Reigai.error(stmt.keyword, "Can't return from top-level code.");
        }

        if (stmt.value != null)
            resolve(stmt.value);
        return null;
    }

    @Override
    public Void visit_var_stmt(Stmt.Var stmt) {
        declare(stmt.name);
        if (stmt.initializer != null) {
            resolve(stmt.initializer);
        }
        define(stmt.name);
        return null;
    }

    @Override
    public Void visit_while_stmt(Stmt.While stmt) {
        resolve(stmt.condition);
        resolve(stmt.body);
        return null;
    }

    @Override
    public Void visit_assign_expr(Expr.Assign expr) {
        resolve(expr.value);
        resolve_local(expr, expr.name);
        return null;
    }

    @Override
    public Void visit_binary_expr(Expr.Binary expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visit_unary_expr(Expr.Unary expr) {
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visit_call_expr(Expr.Call expr) {
        resolve(expr.callee);

        for (Expr argument : expr.arguments) {
            resolve(argument);
        }

        return null;
    }

    @Override
    public Void visit_get_expr(Expr.Get expr) {
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visit_grouping_expr(Expr.Grouping expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Void visit_literal_expr(Expr.Literal expr) {
        return null; // Nothing to be done for literals
    }

    @Override
    public Void visit_logical_expr(Expr.Logical expr) {
        resolve(expr.right);
        resolve(expr.left);
        return null;
    }

    @Override
    public Void visit_variable_expr(Expr.Variable expr) {
        if (!scopes.isEmpty() && scopes.peek().get(expr.name.lexeme) == Boolean.FALSE) {
            Reigai.error(expr.name, "Can't read local varaible in its own initializer.");
        }

        resolve_local(expr, expr.name);
        return null;
    }

    private void resolve(Stmt stmt) {
        stmt.accept(this);
    }

    private void resolve(Expr expr) {
        expr.accept(this);
    }

    private void begin_scope() {
        scopes.push(new HashMap<String, Boolean>());
    }

    private void end_scope() {
        scopes.pop();
    }

    private void declare(Token name) {
        if (scopes.isEmpty())
            return;

        Map<String, Boolean> scope = scopes.peek();

        if (scope.containsKey(name.lexeme)) {
            Reigai.error(name, "Variable with this name already exists in current scope.");
        }

        scope.put(name.lexeme, false);
    }

    private void define(Token name) {
        if (scopes.isEmpty())
            return;

        scopes.peek().put(name.lexeme, true);
    }

    private void resolve_local(Expr expr, Token name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size() - 1 - i);
                return;
            }
        }
    }

    private void resolve_function(Stmt.Function function, FunctionType type) {
        FunctionType enclosing_function = current_function;
        current_function = type;

        begin_scope();
        for (Token param : function.params) {
            declare(param);
            define(param);
        }
        resolve(function.body);
        end_scope();

        current_function = enclosing_function;
    }
}
