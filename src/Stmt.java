
abstract class Stmt {
    interface Visitor<R> {
        R visit_expression_stmt(Expression stmt);

        R visit_print_stmt(Print stmt);
    }

    static class Expression extends Stmt {
        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visit_expression_stmt(this);
        }

        final Expr expression;
    }

    static class Print extends Stmt {
        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visit_print_stmt(this);
        }

        final Expr expression;
    }

    abstract <R> R accept(Visitor<R> visitor);
}
