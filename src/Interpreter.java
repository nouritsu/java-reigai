class Interpreter implements Expr.Visitor<Object> {

    void interpret(Expr expr) {
        try {
            Object value = evaluate(expr);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Reigai.runtime_error(error);
        }
    }

    @Override
    public Object visit_literal_expr(Expr.Literal expr) {
        return expr.value; // no need to run evaluate() as it is atomic
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
                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }

                throw new RuntimeError(expr.operator, "Operands must be two numbers OR two strings.");
            case SLASH:
                check_number_operands(expr.operator, left, right);
                return (double) left / (double) right;
            case STAR:
                check_number_operands(expr.operator, left, right);
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