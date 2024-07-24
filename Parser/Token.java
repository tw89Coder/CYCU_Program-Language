public class Parser {
    private Scanner scanner;
    private Token currentToken;
    private Token lookaheadToken;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.currentToken = new Token();
        this.lookaheadToken = new Token();
        this.lookaheadToken.setConsumed(true);
    }

    public int nextToken() {
        if (lookaheadToken.isConsumed()) {
            int tokenType = scanner.nextToken();
            // Skip comments
            while (tokenType == TokenType.LINECOMMENT_TOKEN || tokenType == TokenType.BLOCKCOMMENT_TOKEN) {
                tokenType = scanner.nextToken();
            }
            currentToken.setType(tokenType);
            currentToken.setText(scanner.getCurrentToken().getText());
            return tokenType;
        } else {
            currentToken.setType(lookaheadToken.getType());
            currentToken.setText(lookaheadToken.getText());
            lookaheadToken.setConsumed(true);
            return currentToken.getType();
        }
    }

    public int lookahead() {
        if (lookaheadToken.isConsumed()) {
            int tokenType = scanner.nextToken();
            // Skip comments
            while (tokenType == TokenType.LINECOMMENT_TOKEN || tokenType == TokenType.BLOCKCOMMENT_TOKEN) {
                tokenType = scanner.nextToken();
            }
            lookaheadToken.setType(tokenType);
            lookaheadToken.setText(scanner.getCurrentToken().getText());
            lookaheadToken.setConsumed(false);
            return tokenType;
        } else {
            return lookaheadToken.getType();
        }
    }
}

public class Token {
    private int type;
    private String text;
    private boolean consumed;

    public Token() {
        this.type = 0;
        this.text = "";
        this.consumed = true;
    }

    public Token(int type, String text) {
        this.type = type;
        this.text = text;
        this.consumed = true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }
}

// Placeholder for Scanner class
public class Scanner {
    // Assume we have a method to get the next token type
    public int nextToken() {
        // Implementation goes here
        return 0; // placeholder return value
    }

    // Assume we have a method to get the current token
    public Token getCurrentToken() {
        // Implementation goes here
        return new Token(); // placeholder return value
    }
}

public class TokenType {
    public static final int LINECOMMENT_TOKEN = 9; // Assuming LINECOMMENT_TOKEN is 9
    public static final int BLOCKCOMMENT_TOKEN = 10; // Assuming BLOCKCOMMENT_TOKEN is 10

    // Other token types...
}
