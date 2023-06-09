import java.util.List;

abstract class Expr {
    interface Visitor<R> {
        R visit_assign_expr(Assign expr);
        R visit_binary_expr(Binary expr);
        R visit_call_expr(Call expr);
        R visit_get_expr(Get expr);
        R visit_set_expr(Set expr);
        R visit_super_expr(Super expr);
        R visit_this_expr(This expr);
        R visit_grouping_expr(Grouping expr);
        R visit_literal_expr(Literal expr);
        R visit_logical_expr(Logical expr);
        R visit_unary_expr(Unary expr);
        R visit_variable_expr(Variable expr);
    }
    static class Assign extends Expr {
        Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_assign_expr(this);
        }

        final Token name;
        final Expr value;
    }
    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_binary_expr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }
    static class Call extends Expr {
        Call(Expr callee, Token paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_call_expr(this);
        }

        final Expr callee;
        final Token paren;
        final List<Expr> arguments;
    }
    static class Get extends Expr {
        Get(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_get_expr(this);
        }

        final Expr object;
        final Token name;
    }
    static class Set extends Expr {
        Set(Expr object, Token name, Expr value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_set_expr(this);
        }

        final Expr object;
        final Token name;
        final Expr value;
    }
    static class Super extends Expr {
        Super(Token keyword, Token method) {
            this.keyword = keyword;
            this.method = method;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_super_expr(this);
        }

        final Token keyword;
        final Token method;
    }
    static class This extends Expr {
        This(Token keyword) {
            this.keyword = keyword;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_this_expr(this);
        }

        final Token keyword;
    }
    static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_grouping_expr(this);
        }

        final Expr expression;
    }
    static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_literal_expr(this);
        }

        final Object value;
    }
    static class Logical extends Expr {
        Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_logical_expr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }
    static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_unary_expr(this);
        }

        final Token operator;
        final Expr right;
    }
    static class Variable extends Expr {
        Variable(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
                return visitor.visit_variable_expr(this);
        }

        final Token name;
    }

    abstract <R> R accept(Visitor<R> visitor);
}
