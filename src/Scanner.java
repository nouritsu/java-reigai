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
            // Single character lexemes
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
            // Possible double character lexemes
            case '!':
                add_token(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                add_token(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                add_token(match('=') ? TokenType.LESSER_EQUAL : TokenType.LESSER);
                break;
            case '>':
                add_token(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            // Comment lexeme
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !at_end()) {
                        advance();
                    }
                } else {
                    add_token(TokenType.SLASH);
                }
                break;
            // If not a lexeme, raise error
            default:
                Reigai.error(line, "Unexpected Character");
                break;
        }
    }

    private boolean match(char expected) {
        if (at_end()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char peek() {
        if (at_end()) {
            return '\0';
        }
        return source.charAt(current);
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
