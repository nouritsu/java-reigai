import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;
    private int start = 0;
    private int current = 0;
    private int line = 1;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("class", TokenType.CLASS);
        keywords.put("this", TokenType.THIS);
        keywords.put("super", TokenType.SUPER);
        keywords.put("fun", TokenType.FUN);
        keywords.put("return", TokenType.RETURN);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);
        keywords.put("var", TokenType.VAR);
        keywords.put("nil", TokenType.NIL);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("print", TokenType.PRINT);
    }

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
                add_token(TokenType.RIGHT_BRACE);
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
            case '"':
                string();
                break;
            // Ignore characters
            case '\r':
            case '\t':
            case ' ':
                break;
            // New lines
            case '\n':
                line++;
                break;
            // Identifiers, Numbers, Errors
            default:
                if (is_digit(c)) { // not using Character.isDigit() as only roman numbers are wanted
                    number();
                } else if (is_alpha(c)) {
                    identifier();
                } else {
                    Reigai.error(line, "Unexpected Character");
                }

                break;
        }

    }

    private void identifier() {
        while (is_alphanumeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) {
            type = TokenType.IDENTIFIER;
        }

        add_token(type);
    }

    private void string() {
        while (peek() != '"' && !at_end()) { // go on till closing "
            if (peek() == '\n') {
                line++;
            }
            advance();
        }

        if (at_end()) { // closing " not encountered
            Reigai.error(line, "Unterminated String");
            return;
        }

        advance(); // closing " character

        String literal = source.substring(start + 1, current - 1); // skip " characters
        add_token(TokenType.STRING, literal);
    }

    private void number() {
        while (is_digit(peek())) { // consume digits before .
            advance();
        }

        if (peek() == '.' && is_digit(peek_next())) {
            advance(); // consume the . if it exists

            while (is_digit(peek())) {
                advance(); // consume digits after .
            }
        }

        add_token(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));

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

    private char peek() { // Advance without consuming next character
        if (at_end()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peek_next() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private boolean is_digit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean is_alpha(char c) { // Underscores can be start of identifiers
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
    }

    private boolean is_alphanumeric(char c) {
        return is_digit(c) || is_alpha(c);
    }

    boolean at_end() {
        return current >= source.length();
    }

    private char advance() { // Consume next character
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
