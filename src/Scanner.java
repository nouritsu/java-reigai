import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scan_tokens() {
        while (!at_end()) {
            start = current;
            scan_token();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scan_token() {
        char c = advance();
        switch (c) {
            case '(':
                add_token(TokenType.LEFT_PAREN);
                break;
            case ')':
                add_token(TokenType.RIGHT_PAREN);
                break;
            case '{':
                add_token(TokenType.LEFT_BRACE);
                break;
            case '}':
                add_token(TokenType.LEFT_BRACE);
                break;
            case ',':
                add_token(TokenType.COMMA);
                break;
            case '.':
                add_token(TokenType.DOT);
                break;
            case '+':
                add_token(TokenType.PLUS);
                break;
            case '-':
                add_token(TokenType.MINUS);
                break;
            case ';':
                add_token(TokenType.SEMICOLON);
                break;
            case '*':
                add_token(TokenType.STAR);
                break;
            default:
                Reigai.error(line, "Unexpected Character");
                break;
        }
    }

    boolean at_end() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void add_token(TokenType type) {
        add_token(type, null);
    }

    private void add_token(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
