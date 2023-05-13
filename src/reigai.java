import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Reigai {

    static boolean had_error = false;

    private static void run_file(String path) throws IOException {
        if (had_error) {
            System.exit(65);
        }

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void run_prompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("reiPL > ");
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
        Expr expression = parser.parse();

        if (had_error)
            return;

        System.out.println(new AstPrinter().print(expression));
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

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error -> " + where + ": " + message);
        had_error = true;
    }

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
}
