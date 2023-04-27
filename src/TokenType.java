enum TokenType {
    LEFT_PAREN, RIGHT_PAREN, // Parenthesis
    LEFT_BRACE, RIGHT_BRACE, // Braces
    COMMA, DOT, SEMICOLON, // Punctuation
    MINUS, PLUS, SLASH, STAR, // Math, Assignment
    EQUAL, VAR, // Assignment / Initialization

    // Comparison Operators
    EQUAL_EQUAL,
    BANG_EQUAL,
    GREATER, GREATER_EQUAL,
    LESSER, LESSER_EQUAL,

    // Literals
    IDENTIFIER, STRING, NUMBER, NIL,

    // Boolean
    TRUE, FALSE,
    AND, OR, BANG,

    // Program Flow
    IF, ELSE,
    WHILE, FOR,

    // Functions
    FUN, RETURN,

    // Object oriented
    CLASS, SUPER, THIS,

    // Misc
    PRINT,
    EOF
}