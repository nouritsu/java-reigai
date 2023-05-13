import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAST {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: <executable> <output-dir>");
            System.exit(64);
        }
        String output_dir = args[0];
        define_ast(output_dir, "Expr", Arrays.asList(
                "Assign     : Token name, Expr value",
                "Binary     : Expr left, Token operator, Expr right",
                "Grouping   : Expr expression",
                "Literal    : Object value",
                "Unary      : Token operator, Expr right",
                "Variable   : Token name"));
        define_ast(output_dir, "Stmt", Arrays.asList(
                "Block          : List<Stmt> statements",
                "Expression     : Expr expression",
                "Print          : Expr expression",
                "Var            : Token name, Expr initializer"));
    }

    private static void define_ast(String output_dir, String base_name, List<String> types) throws IOException {
        String path = output_dir + "/" + base_name + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + base_name + " {");

        define_visitor(writer, base_name, types);

        for (String type : types) {
            String class_name = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();

            define_type(writer, base_name, class_name, fields);
        }

        writer.println();
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void define_type(PrintWriter writer, String base_name, String class_name, String field_list) {
        writer.println("    static class " + class_name + " extends " + base_name + " {"); // Class header
        writer.println("        " + class_name + "(" + field_list + ") {"); // Constructor header
        String[] fields = field_list.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }"); // Constructor body

        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor){");
        writer.println("                return visitor.visit_" + class_name.toLowerCase() + "_"
                + base_name.toLowerCase() + "(this);");
        writer.println("        }");

        writer.println();
        for (String field : fields) { // Fields
            writer.println("        final " + field + ";");
        }

        writer.println("    }");

    }

    private static void define_visitor(PrintWriter writer, String base_name, List<String> types) {
        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String type_name = type.split(":")[0].trim();
            writer.println(
                    "        R visit_" + type_name.toLowerCase() + "_" + base_name.toLowerCase() + "(" + type_name + " "
                            + base_name.toLowerCase()
                            + ");");

        }

        writer.println("    }");
    }
}
