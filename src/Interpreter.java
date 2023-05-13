class Interpreter implements Expr.Visitor<Object> {

    @Override
    public Object visit_literal_expr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visit_grouping_expr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visit_unary_expr(Expr.Unary expr) {
        Object right = expr.right;

        switch (expr.operator.type) {
            case MINUS:
                return -(double) right;
            case BANG:
                return !is_truthy(right);
            default: // Unreachable
                return null;
        }
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
        Object left = expr.left;
        Object right = expr.right;

        switch (expr.operator.type) {
            // COMPARISON
            case GREATER:
                return (Double) left > (Double) right;
            case GREATER_EQUAL:
                return (Double) left >= (Double) right;
            case LESSER:
                return (Double) left < (Double) right;
            case LESSER_EQUAL:
                return (Double) left <= (Double) right;

            // EQUALITY
            case EQUAL_EQUAL:
                return is_equal(left, right);
            case BANG_EQUAL:
                return !is_equal(left, right);

            // ARITHMETIC
            case MINUS:
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                break;
            case SLASH:
                return (double) left / (double) right;
            case STAR:
                return (double) left * (double) right;
            default: // Unreachable
                return null;
        }
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

}