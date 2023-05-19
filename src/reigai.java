import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Reigai {

    private static final Interpreter interpreter = new Interpreter();
    static boolean had_error = false;
    static boolean had_runtime_error = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: <executable> [script]");
            System.exit(64);
        } else if (args.length == 1) {
            run_file(args[0]);
        } else {
            run_prompt();
        }
    }

    private static void run_file(String path) throws IOException {
        if (had_error) {
            System.exit(65);
        }

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (had_error)
            System.exit(65);
        if (had_runtime_error)
            System.exit(70);
    }

    private static void run_prompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("reiPL :> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            had_error = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scan_tokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if (had_error)
            return;

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        if (had_error)
            return;

        interpreter.interpret(statements);
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtime_error(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        had_runtime_error = true;
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error -> " + where + ": " + message);
        had_error = true;
    }
}
