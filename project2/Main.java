// package PL112_10927262;
// javac -encoding utf-8 Main.java
// java Main

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

enum TokenType {

    IDENTIFIER,
    CONSTANT_INT,      // 23, 023 
    CONSTANT_FLOAT,    // .0200, 0.200, 3.200 
    CONSTANT_CHAR,     // 'c'
    CONSTANT_STRING,   // "This is a string"
    CONSTANT_FALSE,    // false
    CONSTANT_TRUE,     // true
    INT,               // int
    FLOAT,             // float
    CHAR,              // char
    BOOL,              // bool
    STRING,            // string
    VOID,              // void
    IF,                // if
    ELSE,              // else
    WHILE,             // while
    DO,                // do
    RETURN,            // return 
    LEFT_PAREN,        // (
    RIGHT_PAREN,       // )
    LEFT_BRACKET,      // [
    RIGHT_BRACKET,     // ]
    LEFT_CURLY_BRACE,  // {
    RIGHT_CURLY_BRACE, // }
    PLUS,              // +
    MINUS,             // -
    MULTIPLY,          // *
    DIVIDE,            // /
    MOD,               // %
    XOR,               // ^
    GREATER_THAN,      // >
    LESS_THAN,         // <
    GE,                // >=
    LE,                // <=
    EQ,                // ==
    NEQ,               // <>
    BITWISE_AND,       // &
    BITWISE_OR,        // |
    ASSIGNMENT,        // =
    NOT,               // !
    AND,               // &&
    OR,                // ||
    PE,                // +=
    ME,                // -=
    TE,                // *=
    DE,                // /=
    RE,                // %=
    PP,                // ++
    MM,                // --
    RS,                // >>
    LS,                // <<
    SEMICOLON,         // ;
    COMMA,             // ,
    CONDITIONAL,       // ?
    COLON,             // :
    COUT,              // cout
    CIN,               // cin
    ERROR              // lexical error
}

class Token {

    private String name;
    private TokenType type;
    private int line;

    // Default constructor
    public Token() {
        this.name = "";
        this.type = null;
        this.line = 0;
    }

    // Constructor with parameters
    public Token(String name, TokenType type, int line) {
        this.name = name;
        this.type = type;
        this.line = line;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for type
    public TokenType getType() {
        return type;
    }

    // Setter for type
    public void setType(TokenType type) {
        this.type = type;
    }

    // Getter for line
    public int getLine() {
        return line;
    }

    // Setter for line
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", line=" + line +
                '}';
    }
}

class Variable {
    private String name;
    private String type;
    private String value;
    private String function;

    // Constructor
    public Variable(String name, String type, String value, String function) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.function = function;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for type
    public String getType() {
        return type;
    }

    // Setter for type
    public void setType(String type) {
        this.type = type;
    }

    // Getter for value
    public String getValue() {
        return value;
    }

    // Setter for value
    public void setValue(String value) {
        this.value = value;
    }

    // Getter for function
    public String getFunction() {
        return function;
    }

    // Setter for function
    public void setFunction(String function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                ", function=" + function +
                '}';
    }
}

class Array {
    private String name;
    private String type;
    private String function;
    private String range;
    private ArrayList<String> values;
    private ArrayList<Integer> indices;

    // Constructor
    public Array(String name, String type, String function, String range) {
        this.name = name;
        this.type = type;
        this.function = function;
        this.range = range;
        this.values = new ArrayList<String>();
        this.indices = new ArrayList<Integer>();
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for type
    public String getType() {
        return type;
    }

    // Setter for type
    public void setType(String type) {
        this.type = type;
    }

    // Getter for function
    public String getFunction() {
        return function;
    }

    // Setter for function
    public void setFunction(String function) {
        this.function = function;
    }

    // Getter for range
    public String getRange() {
        return range;
    }

    // Setter for range
    public void setRange(String range) {
        this.range = range;
    }

    // Getter for values
    public ArrayList<String> getValues() {
        return values;
    }

    // Setter for values
    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    // Getter for indices
    public ArrayList<Integer> getIndices() {
        return indices;
    }

    // Setter for indices
    public void setIndices(ArrayList<Integer> indices) {
        this.indices = indices;
    }

    // Method to add a value and its corresponding index
    public void addValue(String value, String index) {
        int intIndex = Integer.parseInt(index);
        int intRange = Integer.parseInt(range);

        if (intIndex < intRange) {
            this.values.add(value);
            this.indices.add(intRange);
        } else {
            throw new IndexOutOfBoundsException("Index exceeds the array range.");
        }
    }
}

class Function {
    private String name;
    private String type;
    private ArrayList<Token> content;
    private ArrayList<Variable> parameterList;

    // Constructor
    public Function(String name, String type) {
        this.name = name;
        this.type = type;
        this.content = new ArrayList<Token>();
        this.parameterList = new ArrayList<Variable>();
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for type
    public String getType() {
        return type;
    }

    // Setter for type
    public void setType(String type) {
        this.type = type;
    }

    // Getter for content
    public ArrayList<Token> getContent() {
        return content;
    }

    // Setter for content
    public void setContent(ArrayList<Token> content) {
        this.content = content;
    }

    // Method to add a Token to the function's content
    public void addToken(Token token) {
        this.content.add(token);
    }

    // Getter for parameterList
    public ArrayList<Variable> getParameterList() {
        return parameterList;
    }

    // Setter for parameterList
    public void setParameterList(ArrayList<Variable> parameterList) {
        this.parameterList = parameterList;
    }

    // Method to add a Variable to the function's parameterList
    public void addVariable(Variable variable) {
        this.parameterList.add(variable);
    }
}

class ErrorMessage {

    public void lexicalError(char firstChar, int line) {
        System.out.println("> Line " + line + " : unrecognized token with first char : '" + firstChar + "'");
    }

    public void syntacticalError(Token token) {
        System.out.println("> Line " + token.getLine() + " : unexpected token : '" + token.getName() + "'");
    }

    public void semanticError(Token token) {
        System.out.println("> Line " + token.getLine() + " : undefined identifier : '" + token.getName() + "'");
    }
}

class Scope {
    private Map<String, Variable> variables;
    private Map<String, Array> arrays;
    private Map<String, Function> functions;
    private Scope parentScope;

    public Scope(Scope parentScope) {
        this.variables = new HashMap<>();
        this.arrays = new HashMap<>();
        this.functions = new HashMap<>();
        this.parentScope = parentScope;
    }

    // Add a variable to the current scope
    public void addVariable(Variable variable) {
        variables.put(variable.getName(), variable);
    }

    // Get a variable from the current scope or parent scopes
    public Variable getVariable(String name) {
        Variable variable = variables.get(name);
        if (variable == null && parentScope != null) {
            return parentScope.getVariable(name);
        }
        return variable;
    }

    // Add an array to the current scope
    public void addArray(Array array) {
        arrays.put(array.getName(), array);
    }

    // Get an array from the current scope or parent scopes
    public Array getArray(String name) {
        Array array = arrays.get(name);
        if (array == null && parentScope != null) {
            return parentScope.getArray(name);
        }
        return array;
    }

    // Add a function to the current scope
    public void addFunction(Function function) {
        functions.put(function.getName(), function);
    }

    // Get a function from the current scope or parent scopes
    public Function getFunction(String name) {
        Function function = functions.get(name);
        if (function == null && parentScope != null) {
            return parentScope.getFunction(name);
        }
        return function;
    }
}

class Tokenizer {
    private Scanner scanner;
    private String userInput;
    private int currentIndex;
    private int line;
    private ArrayList<Token> tokenList;
    private ErrorMessage errorMessage;

    public Tokenizer() {
        this.scanner = new Scanner(System.in);
        this.userInput = "";
        this.currentIndex = 0;
        this.line = 1;
        this.tokenList = new ArrayList<Token>();
        this.errorMessage = new ErrorMessage();
    }

    public void reset() {
        this.userInput = "";
        this.currentIndex = 0;
        this.line = 1;
        this.tokenList.clear();
    }

    public void resetLine() {
        this.line = 1;
    }

    public Token getNextToken() {
        while (true) {
            // If userInput is empty or fully consumed, read a new line
            if (userInput == null || currentIndex >= userInput.length()) {
                // System.out.print(">>> Enter input: ");  // test line
                userInput = scanner.nextLine();
                userInput += '\n';
                currentIndex = 0;
            }

            // Skip whitespaces and check for newlines
            while (currentIndex < userInput.length()) {
                char currentChar = userInput.charAt(currentIndex);
                switch (currentChar) {
                    case ' ':
                    case '\t':
                    case '\r':
                        currentIndex++;
                        break;
                    case '\n':
                        line++;
                        currentIndex++;
                        break;
                    default:
                        break;
                }
                if (currentChar != ' ' && currentChar != '\t' && currentChar != '\r' && currentChar != '\n') {
                    break;
                }
            }

            if (currentIndex >= userInput.length()) {
                continue; // Prompt for input again if nothing was found
            }

            char currentChar = userInput.charAt(currentIndex);

            // Handle different token types
            switch (currentChar) {
                case '/':
                    if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '/') {
                        currentIndex = userInput.length();
                        line++;
                        continue;
                    } else if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '=') {
                        currentIndex += 2;
                        tokenList.add(new Token("/", TokenType.DE, line));

                        return new Token("/", TokenType.DE, line);
                    }
                    currentIndex++;
                    tokenList.add(new Token("/", TokenType.DIVIDE, line));

                    return new Token("/", TokenType.DIVIDE, line);

                case '+':
                    return handlePlus();

                case '-':
                    return handleMinus();

                case '*':
                    return handleMultiply();

                case '=':
                    return handleAssignment();

                case '"':
                    return handleStringLiteral();

                case '\'':
                    return handleCharacterLiteral();

                case '%':
                    return handleModulus();

                case '^':
                    currentIndex++;
                    tokenList.add(new Token("^", TokenType.XOR, line));

                    return new Token("^", TokenType.XOR, line);

                case '>':
                    return handleGreaterThan();

                case '<':
                    return handleLessThan();

                case '&':
                    return handleBitwiseAnd();

                case '|':
                    return handleBitwiseOr();

                case '!':
                    return handleNot();

                case ';':
                    currentIndex++;
                    tokenList.add(new Token(";", TokenType.SEMICOLON, line));

                    return new Token(";", TokenType.SEMICOLON, line);

                case ',':
                    currentIndex++;
                    tokenList.add(new Token(",", TokenType.COMMA, line));

                    return new Token(",", TokenType.COMMA, line);

                case '?':
                    currentIndex++;
                    tokenList.add(new Token("?", TokenType.CONDITIONAL, line));


                    return new Token("?", TokenType.CONDITIONAL, line);

                case ':':
                    currentIndex++;
                    tokenList.add(new Token(":", TokenType.COLON, line));

                    return new Token(":", TokenType.COLON, line);

                case '(':
                    currentIndex++;
                    tokenList.add(new Token("(", TokenType.LEFT_PAREN, line));

                    return new Token("(", TokenType.LEFT_PAREN, line);

                case ')':
                    currentIndex++;
                    tokenList.add(new Token(")", TokenType.RIGHT_PAREN, line));

                    return new Token(")", TokenType.RIGHT_PAREN, line);

                case '[':
                    currentIndex++;
                    tokenList.add(new Token("[", TokenType.LEFT_BRACKET, line));

                    return new Token("[", TokenType.LEFT_BRACKET, line);

                case ']':
                    currentIndex++;
                    tokenList.add(new Token("]", TokenType.RIGHT_BRACKET, line));

                    return new Token("]", TokenType.RIGHT_BRACKET, line);

                case '{':
                    currentIndex++;
                    tokenList.add(new Token("{", TokenType.LEFT_CURLY_BRACE, line));

                    return new Token("{", TokenType.LEFT_CURLY_BRACE, line);

                case '}':
                    currentIndex++;
                    tokenList.add(new Token("}", TokenType.RIGHT_CURLY_BRACE, line));

                    return new Token("}", TokenType.RIGHT_CURLY_BRACE, line);

                default:
                    if (Character.isLetter(currentChar)) {
                        return handleIdentifierOrKeyword();
                    } else if (Character.isDigit(currentChar) || currentChar == '.') {
                        return handleNumber();
                    } else {
                        errorMessage.lexicalError(currentChar, line);
                        currentIndex++;
                        tokenList.add(new Token("", TokenType.ERROR, line));

                        return new Token("", TokenType.ERROR, line);
                    }
            }
        }
    }

    private Token handlePlus() {
        if (currentIndex + 1 < userInput.length()) {
            if (userInput.charAt(currentIndex + 1) == '+') {
                currentIndex += 2;
                tokenList.add(new Token("++", TokenType.PP, line));

                return new Token("++", TokenType.PP, line);
            } else if (userInput.charAt(currentIndex + 1) == '=') {
                currentIndex += 2;
                tokenList.add(new Token("+=", TokenType.PE, line));

                return new Token("+=", TokenType.PE, line);
            }
        }
        currentIndex++;
        tokenList.add(new Token("+", TokenType.PLUS, line));

        return new Token("+", TokenType.PLUS, line);
    }

    private Token handleMinus() {
        if (currentIndex + 1 < userInput.length()) {
            if (userInput.charAt(currentIndex + 1) == '-') {
                currentIndex += 2;
                tokenList.add(new Token("--", TokenType.MM, line));

                return new Token("--", TokenType.MM, line);
            } else if (userInput.charAt(currentIndex + 1) == '=') {
                currentIndex += 2;
                tokenList.add(new Token("-=", TokenType.ME, line));

                return new Token("-=", TokenType.ME, line);
            }
        }
        currentIndex++;
        tokenList.add(new Token("-", TokenType.MINUS, line));
        
        return new Token("-", TokenType.MINUS, line);
    }

    private Token handleMultiply() {
        if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '=') {
            currentIndex += 2;
            tokenList.add(new Token("*=", TokenType.TE, line));

            return new Token("*=", TokenType.TE, line);
        }
        currentIndex++;
        tokenList.add(new Token("*", TokenType.MULTIPLY, line));

        return new Token("*", TokenType.MULTIPLY, line);
    }

    private Token handleAssignment() {
        if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '=') {
            currentIndex += 2;
            tokenList.add(new Token("==", TokenType.EQ, line));

            return new Token("==", TokenType.EQ, line);
        }
        currentIndex++;
        tokenList.add(new Token("=", TokenType.ASSIGNMENT, line));

        return new Token("=", TokenType.ASSIGNMENT, line);
    }

    private Token handleModulus() {
        if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '=') {
            currentIndex += 2;
            tokenList.add(new Token("%=", TokenType.RE, line));

            return new Token("%=", TokenType.RE, line);
        }
        currentIndex++;
        tokenList.add(new Token("%", TokenType.MOD, line));

        return new Token("%", TokenType.MOD, line);
    }

    private Token handleGreaterThan() {
        if (currentIndex + 1 < userInput.length()) {
            if (userInput.charAt(currentIndex + 1) == '=') {
                currentIndex += 2;
                tokenList.add(new Token(">=", TokenType.GE, line));

                return new Token(">=", TokenType.GE, line);
            } else if (userInput.charAt(currentIndex + 1) == '>') {
                currentIndex += 2;
                tokenList.add(new Token(">>", TokenType.RS, line));

                return new Token(">>", TokenType.RS, line);
            }
        }
        currentIndex++;
        tokenList.add(new Token(">", TokenType.GREATER_THAN, line));

        return new Token(">", TokenType.GREATER_THAN, line);
    }

    private Token handleLessThan() {
        if (currentIndex + 1 < userInput.length()) {
            if (userInput.charAt(currentIndex + 1) == '=') {
                currentIndex += 2;
                tokenList.add(new Token("<=", TokenType.LE, line));

                return new Token("<=", TokenType.LE, line);
            } else if (userInput.charAt(currentIndex + 1) == '<') {
                currentIndex += 2;
                tokenList.add(new Token("<<", TokenType.LS, line));

                return new Token("<<", TokenType.LS, line);
            }
        }
        currentIndex++;
        tokenList.add(new Token("<", TokenType.LESS_THAN, line));

        return new Token("<", TokenType.LESS_THAN, line);
    }

    private Token handleBitwiseAnd() {
        if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '&') {
            currentIndex += 2;
            tokenList.add(new Token("&&", TokenType.AND, line));

            return new Token("&&", TokenType.AND, line);
        }
        currentIndex++;
        tokenList.add(new Token("&", TokenType.BITWISE_AND, line));

        return new Token("&", TokenType.BITWISE_AND, line);
    }

    private Token handleBitwiseOr() {
        if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '|') {
            currentIndex += 2;
            tokenList.add(new Token("||", TokenType.OR, line));

            return new Token("||", TokenType.OR, line);
        }
        currentIndex++;
        tokenList.add(new Token("|", TokenType.BITWISE_OR, line));

        return new Token("|", TokenType.BITWISE_OR, line);
    }

    private Token handleNot() {
        if (currentIndex + 1 < userInput.length() && userInput.charAt(currentIndex + 1) == '=') {
            currentIndex += 2;
            tokenList.add(new Token("!=", TokenType.NEQ, line));

            return new Token("!=", TokenType.NEQ, line);
        }
        currentIndex++;
        tokenList.add(new Token("!", TokenType.NOT, line));

        return new Token("!", TokenType.NOT, line);
    }

    private Token handleStringLiteral() {
        currentIndex++;  // Skip the opening "
        StringBuilder tokenName = new StringBuilder();

        while (currentIndex < userInput.length() && userInput.charAt(currentIndex) != '"') {
            if (userInput.charAt(currentIndex) == '\n') {
                line++;
            }
            tokenName.append(userInput.charAt(currentIndex));
            currentIndex++;
        }

        if (currentIndex < userInput.length() && userInput.charAt(currentIndex) == '"') {
            currentIndex++;  // Skip the closing "
            tokenList.add(new Token(tokenName.toString(), TokenType.CONSTANT_STRING, line));

            return new Token(tokenName.toString(), TokenType.CONSTANT_STRING, line);
        } else {
            errorMessage.lexicalError('"', line);
            tokenList.add(new Token(tokenName.toString(), TokenType.ERROR, line));

            return new Token(tokenName.toString(), TokenType.ERROR, line);
        }
    }

    private Token handleCharacterLiteral() {

        if (currentIndex + 2 < userInput.length() && userInput.charAt(currentIndex + 2) == '\'') {
            char charValue = userInput.charAt(currentIndex + 1);
            currentIndex += 3;  // Skip the entire character literal
            tokenList.add(new Token(Character.toString(charValue), TokenType.CONSTANT_CHAR, line));
            
            return new Token(Character.toString(charValue), TokenType.CONSTANT_CHAR, line);
        } else {
            StringBuilder tokenName = new StringBuilder();
            errorMessage.lexicalError('\'', line);
            while (currentIndex < userInput.length()) {
                currentIndex++;
            }

            tokenName.append(userInput.charAt(currentIndex));
            tokenList.add(new Token(tokenName.toString(), TokenType.ERROR, line));
            
            return new Token(tokenName.toString(), TokenType.ERROR, line);
        }
    }

    private Token handleNumber() {
        StringBuilder tokenName = new StringBuilder();
        boolean isFloat = false;
    
        while (currentIndex < userInput.length()) {
            char currentChar = userInput.charAt(currentIndex);
    
            if (Character.isDigit(currentChar)) {
                tokenName.append(currentChar);
                currentIndex++;
            } else if (currentChar == '.' && !isFloat) {
                isFloat = true;
                tokenName.append(currentChar);
                currentIndex++;
            } else {
                break;  // <<< Exit loop when the number ends
            }
        }
    
        TokenType tokenType = isFloat ? TokenType.CONSTANT_FLOAT : TokenType.CONSTANT_INT;
        tokenList.add(new Token(tokenName.toString(), tokenType, line));

        return new Token(tokenName.toString(), tokenType, line);
    }

    private Token handleIdentifierOrKeyword() {
        StringBuilder tokenName = new StringBuilder();
        boolean hasError = false;
    
        while (currentIndex < userInput.length()) {
            char currentChar = userInput.charAt(currentIndex);
    
            if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                tokenName.append(currentChar);
                currentIndex++;
            } else if (currentChar == '$' || currentChar == '@' || currentChar == '#') {
                // Handle illegal characters
                errorMessage.lexicalError(currentChar, line);
                tokenName.append(currentChar);  // Append illegal character to the token
                currentIndex++;
                hasError = true;
                break;
            } else {
                break;
            }
        }
    
        String tokenStr = tokenName.toString();
        TokenType tokenType;
    
        if (hasError) {
            tokenType = TokenType.ERROR;
        } else {
            switch (tokenStr) {
                case "true":
                    tokenType = TokenType.CONSTANT_TRUE;
                    break;
                case "false":
                    tokenType = TokenType.CONSTANT_FALSE;
                    break;
                case "int":
                    tokenType = TokenType.INT;
                    break;
                case "float":
                    tokenType = TokenType.FLOAT;
                    break;
                case "char":
                    tokenType = TokenType.CHAR;
                    break;
                case "bool":
                    tokenType = TokenType.BOOL;
                    break;
                case "string":
                    tokenType = TokenType.STRING;
                    break;
                case "void":
                    tokenType = TokenType.VOID;
                    break;
                case "if":
                    tokenType = TokenType.IF;
                    break;
                case "else":
                    tokenType = TokenType.ELSE;
                    break;
                case "while":
                    tokenType = TokenType.WHILE;
                    break;
                case "do":
                    tokenType = TokenType.DO;
                    break;
                case "return":
                    tokenType = TokenType.RETURN;
                    break;
                case "cout":
                    tokenType = TokenType.COUT;
                    break;
                case "cin":
                    tokenType = TokenType.CIN;
                    break;
                default:
                    tokenType = TokenType.IDENTIFIER;
                    break;
            }
        }

        tokenList.add(new Token(tokenStr, tokenType, line));
    
        return new Token(tokenStr, tokenType, line);
    }    

    // Getter method
    public ArrayList<Token> getTokenList() {
        return new ArrayList<Token>(tokenList);
    }
    
    // Setter method
    public void setTokenList(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }
}

class Parser {
    private boolean isTest;
    private boolean isDone;
    private boolean isFunction;
    private String functionName;
    private Tokenizer tokenizer;
    private ArrayList<Variable> tempVariable;
    private ArrayList<Variable> variable;
    private ArrayList<Array> tempArray;
    private ArrayList<Array> array;
    private ArrayList<Function> tempFunction;
    private ArrayList<Function> function;
    private ArrayList<String> tempDefIdentifier;
    private ArrayList<String> defIdentifier;
    private ErrorMessage errorMessage;
    private Token currentToken;
    

    public Parser() {
        this.isTest = true;
        this.isDone = false;
        this.isFunction = false;
        this.functionName = "Main";
        this.tokenizer = new Tokenizer();
        this.tempVariable = new ArrayList<Variable>();
        this.variable = new ArrayList<Variable>();
        this.tempArray = new ArrayList<Array>();
        this.array = new ArrayList<Array>();
        this.tempFunction = new ArrayList<Function>();
        this.function = new ArrayList<Function>();
        this.tempDefIdentifier = new ArrayList<String>();
        this.defIdentifier = new ArrayList<String>();
        this.errorMessage = new ErrorMessage();
        this.currentToken = new Token();
    }

    private boolean match(TokenType expectedType, boolean needToDefine) {
        if (currentToken.getType() == TokenType.ERROR) {
            isFunction = false;

            return false;
        } else if (currentToken.getType() == expectedType) {
            if (needToDefine) {
                if(isFunction) {
                    if (tempDefIdentifier.contains(currentToken.getName())) {
                        currentToken = tokenizer.getNextToken(); // Advance to the next token
                        if (currentToken.getType() == TokenType.ERROR) {
                            isFunction = false;
                
                            return false;
                        } 

                        return true;
                    } else {
                        errorMessage.semanticError(currentToken);
                        isFunction = false;

                        return false;
                    }
                } else {
                    if (defIdentifier.contains(currentToken.getName())) {
                        currentToken = tokenizer.getNextToken(); // Advance to the next token
                        if (currentToken.getType() == TokenType.ERROR) {
                            isFunction = false;
                
                            return false;
                        } 

                        return true;
                    } else {
                        errorMessage.semanticError(currentToken);

                        return false;
                    }
                }
            } else {
                currentToken = tokenizer.getNextToken(); // Advance to the next token
                if (currentToken.getType() == TokenType.ERROR) {
                    isFunction = false;
        
                    return false;
                } 

                return true;
            }
        } else {
            errorMessage.syntacticalError(currentToken);
            isFunction = false;

            return false;
        }
    }

    private void resetAll() {
        tokenizer.reset();
        tempVariable.clear();
        tempArray.clear();
        tempFunction.clear();
        tempDefIdentifier.clear();
        isFunction = false;
        functionName = "Main";
    }

    public void userInput() {
        if (isTest) System.out.println(">>> Enter userInput");

        while (!isDone) {
            tokenizer.resetLine();

            currentToken = tokenizer.getNextToken();
            if (currentToken.getType() == TokenType.VOID || typeSpecifier()) {
                if (definition()) {
                    if (isFunction) {
                        for (int i = 0; i < tempFunction.size(); i++) {
                            Function funct = tempFunction.get(i);
                            boolean found = false;
                        
                            for (int j = 0; j < function.size(); j++) {
                                Function existingFunct = function.get(j);
                        
                                // Check if the token with the same name exists
                                if (existingFunct.getName().equals(funct.getName())) {
                                    // If found, update the existing token
                                    function.set(j, funct);
                                    System.out.println("> New definition of " + funct.getName() + "() entered ...");
                                    found = true;
                                    break;
                                }
                            }
                        
                            if (!found) {
                                // If not found, add the new token to the list
                                function.add(funct);
                                System.out.println("> Definition of " + funct.getName() + "() entered ...");
                            }
                        }

                        tempFunction.clear();
                        isFunction = false;
                    } else {
                        // Handling variables
                        for (Variable tempVar : tempVariable) {
                            String varName = tempVar.getName();

                            if (defIdentifier.contains(varName)) {
                                // Update the existing variable definition
                                for (int j = 0; j < variable.size(); j++) {
                                    if (variable.get(j).getName().equals(varName)) {
                                        variable.set(j, tempVar); // Update the variable definition
                                        break;
                                    }
                                }
                            } else {
                                // Add new variable definition
                                variable.add(tempVar);
                            }
                        }

                        tempVariable.clear();

                        // Handling arrays
                        for (Array tempArr : tempArray) {
                            String arrName = tempArr.getName();

                            if (defIdentifier.contains(arrName)) {
                                // Update the existing array definition
                                for (int j = 0; j < array.size(); j++) {
                                    if (array.get(j).getName().equals(arrName)) {
                                        array.set(j, tempArr); // Update the array definition
                                        break;
                                    }
                                }
                            } else {
                                // Add new array definition
                                array.add(tempArr);
                            }
                        }

                        tempArray.clear();

                        for (int i = 0; i < tempDefIdentifier.size(); i++) {
                            String idString = tempDefIdentifier.get(i);

                            if (defIdentifier.contains(idString)) {
                                System.out.println("> New definition of " + idString + " entered ...");
                            } else {
                                defIdentifier.add(idString);
                                System.out.println("> Definition of " + idString + " entered ...");
                            }
                        }

                        tempDefIdentifier.clear();
                    }

                    if (isTest) System.out.println("> Definition executed ...");
                } else {
                    resetAll();
                }
            } else {
                if (statement()) {
                    if(!isDone && !isFunction) System.out.println("> Statement executed ...");
                } else {
                    resetAll();
                }
            }
        }

        if (isTest) System.out.println("<<< Exit userInput");
    }

    private boolean definition() {
        // :           VOID Identifier function_definition_without_ID 
        // | type_specifier Identifier function_definition_or_declarators
        if (isTest) System.out.println(">>> Enter definition");
        boolean isAccept = false;
        String varType = currentToken.getName();
        String varOrFunctName = "";

        if (currentToken.getType() == TokenType.VOID) {
            // VOID Identifier function_definition_without_ID 
            if (match(TokenType.VOID, false)) {
                functionName = currentToken.getName();

                if (match(TokenType.IDENTIFIER, false)) {
                    if (functionDefinitionWithoutID("void", functionName)) {
                        isAccept = true;
                    }
                }
            }
        } else {
            // type_specifier Identifier function_definition_or_declarators
            if (match(currentToken.getType(), false)) {
                varOrFunctName = currentToken.getName();

                if (match(TokenType.IDENTIFIER, false)) {
                    if (functionDefinitionOrDeclarators(varType, varOrFunctName)) {
                        isAccept = true;
                    }
                }
            }
        }

        if (isTest) System.out.println("<<< Exit definition");

        return isAccept;
    }

    private boolean typeSpecifier() {
        // : INT | CHAR | FLOAT | STRING | BOOL
        if (isTest) System.out.println(">>> Enter type_specifier");
        boolean isAccept = false;

        TokenType type = currentToken.getType();
        if (type == TokenType.INT || type == TokenType.CHAR || type == TokenType.FLOAT ||
            type == TokenType.STRING || type == TokenType.BOOL) {
            isAccept = true;
        }

        if (isTest) System.out.println("<<< Exit type_specifier");

        return isAccept;
    }

    private boolean functionDefinitionOrDeclarators(String varType, String varOrFunctName) {
        // : function_definition_without_ID
        // | rest_of_declarators
        if (isTest) System.out.println(">>> Enter function_definition_or_declarators");
        boolean isAccept = false;

        if (currentToken.getType() == TokenType.LEFT_PAREN) {
            // function_definition_without_ID
            if (functionDefinitionWithoutID(varType, varOrFunctName)) {
                isAccept = true;
            }
        } else {
            // rest_of_declarators
            if (restOfDeclarators(varType, varOrFunctName)) {
                isAccept = true;
            }
        }

        if (isTest) System.out.println("<<< Exit function_definition_or_declarators");

        return isAccept;
    }

    private boolean restOfDeclarators(String varType, String varOrFunctName) {
        // : [ '[' Constant ']' ] 
        //   { ',' Identifier [ '[' Constant ']' ] } ';'
        if (isTest) System.out.println(">>> Enter rest_of_declarators");
        boolean isAccept = false;

        if (currentToken.getType() == TokenType.SEMICOLON) {
            tempDefIdentifier.add(varOrFunctName);
            tempVariable.add(new Variable(varOrFunctName, varType, "", functionName));

            if (isFunction) {
                match(TokenType.SEMICOLON, false);
            } else {
                isAccept = true;
            }
        } else if (currentToken.getType() == TokenType.LEFT_BRACKET) {
            if (match(TokenType.LEFT_BRACKET, false)) {
                // [ '[' Constant ']' ] 
                String arrConst = currentToken.getName();

                if (constant()) {
                    if (match(TokenType.RIGHT_BRACKET, false)) {
                        tempDefIdentifier.add(varOrFunctName);
                        tempArray.add(new Array(varOrFunctName, varType, functionName, arrConst));

                        if (currentToken.getType() == TokenType.SEMICOLON) {
                            if (isFunction) {
                                match(TokenType.SEMICOLON, false);
                            } else {
                                isAccept = true;
                            }
                        } else {
                            boolean isStop = false;
                            while (!isStop) {
                                if (match(TokenType.COMMA, false)) {
                                    varOrFunctName = currentToken.getName();
                                    if (match(TokenType.IDENTIFIER, false)) {
                                        if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                                            arrConst = currentToken.getName();
                                            if (constant()) {
                                                if (match(TokenType.RIGHT_BRACKET, false)) {
                                                    if (currentToken.getType() == TokenType.SEMICOLON) {
                                                        isStop = true;
                                                        tempDefIdentifier.add(varOrFunctName);
                                                        tempArray.add(new Array(varOrFunctName, varType, functionName, arrConst));
    
                                                        if (isFunction) {
                                                            match(TokenType.SEMICOLON, false);
                                                        } else {
                                                            isAccept = true;
                                                        }
                                                    }
                                                } else {
                                                    isStop = true;
                                                }
                                            }
                                        } else {
                                            tempVariable.add(new Variable(varOrFunctName, varType, "", functionName));

                                            if (currentToken.getType() == TokenType.SEMICOLON) {
                                                isStop = true;

                                                if (isFunction) {
                                                    match(TokenType.SEMICOLON, false);
                                                } else {
                                                    isAccept = true;
                                                }
                                            }
                                        }
                                    } else {
                                        isStop = true;
                                    }
                                } else {
                                    isStop = true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            tempDefIdentifier.add(varOrFunctName);
            tempVariable.add(new Variable(varOrFunctName, varType, "", functionName));

            boolean isStop = false;
            while (!isStop) {
                if (match(TokenType.COMMA, false)) {
                    varOrFunctName = currentToken.getName();
                    if (match(TokenType.IDENTIFIER, false)) {
                        if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                            String arrConst = currentToken.getName();
                            if (constant()) {
                                if (match(TokenType.RIGHT_BRACKET, false)) {
                                    if (currentToken.getType() == TokenType.SEMICOLON) {
                                        isStop = true;
                                        tempDefIdentifier.add(varOrFunctName);
                                        tempArray.add(new Array(varOrFunctName, varType, functionName, arrConst));

                                        if (isFunction) {
                                            match(TokenType.SEMICOLON, false);
                                        } else {
                                            isAccept = true;
                                        }
                                    }
                                } else {
                                    isStop = true;
                                }
                            }
                        } else {
                            tempVariable.add(new Variable(varOrFunctName, varType, "", functionName));
                            
                            if (currentToken.getType() == TokenType.SEMICOLON) {
                                isStop = true;

                                if (isFunction) {
                                    match(TokenType.SEMICOLON, false);
                                } else {
                                    isAccept = true;
                                }
                            }
                        }
                    } else {
                        isStop = true;
                    }
                } else {
                    isStop = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_declarators");

        return isAccept;
    }

    private boolean functionDefinitionWithoutID(String varType, String varOrFunctName) {
        // : '(' [ VOID | formal_parameter_list ] ')' compound_statement
        if (isTest) System.out.println(">>> Enter function_definition_without_ID");
        boolean isAccept = false;

        if (match(TokenType.LEFT_PAREN, false)) {
            if (currentToken.getType() == TokenType.VOID) {
                if (match(TokenType.VOID, false)) {
                    if (match(TokenType.RIGHT_PAREN, false)) {
                        isAccept = compoundStatement();
                    }
                } 
            } else if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                if (match(TokenType.RIGHT_PAREN, false)) {
                    isAccept = compoundStatement();
                }
            } else {
                // formal_parameter_list
                if (typeSpecifier()) {
                    if (match(currentToken.getType(), false)) {
                        if (formalParameterList()) {
                            if (match(TokenType.RIGHT_PAREN, false)) {
                                isAccept = compoundStatement();
                            }
                        }
                    }
                }
            }
        }

        if (isTest) System.out.println("<<< Exit function_definition_without_ID");

        return isAccept;
    }

    private boolean formalParameterList() {
        // : type_specifier [ '&' ] Identifier [ '[' Constant ']' ] 
        //   { ',' type_specifier [ '&' ] Identifier [ '[' Constant ']' ] }
        if (isTest) System.out.println(">>> Enter formal_parameter_list");
        boolean isAccept = false;

        // Don't use type_specifier because typeSpecifier() has already used
        if (match(currentToken.getType(), false)) {
            if (currentToken.getType() == TokenType.BITWISE_AND) {
                if (match(TokenType.BITWISE_AND, false)) {
                    if (match(TokenType.IDENTIFIER, false)) {
                        if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                            if (match(TokenType.LEFT_BRACKET, false)) {
                                if (constant()) {
                                    if (match(TokenType.RIGHT_BRACKET, false)) {
                                        if (currentToken.getType() == TokenType.COMMA) {
                                            if (match(TokenType.COMMA, false)) {
                                                if (typeSpecifier()) {
                                                    formalParameterList();
                                                } else {
                                                    isAccept = match(TokenType.ERROR, false);
                                                }
                                            }
                                        } else {
                                            isAccept = true;
                                        }
                                    }
                                }
                            }
                        } else {
                            if (currentToken.getType() == TokenType.COMMA) {
                                if (match(TokenType.COMMA, false)) {
                                    if (typeSpecifier()) {
                                        formalParameterList();
                                    } else {
                                        isAccept = match(TokenType.ERROR, false);
                                    }
                                }
                            } else {
                                isAccept = true;
                            }
                        }
                    }
                }
            } else {
                if (match(TokenType.IDENTIFIER, false)) {
                    if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                        if (match(TokenType.LEFT_BRACKET, false)) {
                            if (constant()) {
                                if (match(TokenType.RIGHT_BRACKET, false)) {
                                    if (currentToken.getType() == TokenType.COMMA) {
                                        if (match(TokenType.COMMA, false)) {
                                            if (typeSpecifier()) {
                                                formalParameterList();
                                            } else {
                                                isAccept = match(TokenType.ERROR, false);
                                            }
                                        }
                                    } else {
                                        isAccept = true;
                                    }
                                }
                            }
                        }
                    } else {
                        if (currentToken.getType() == TokenType.COMMA) {
                            if (match(TokenType.COMMA, false)) {
                                if (typeSpecifier()) {
                                    formalParameterList();
                                } else {
                                    isAccept = match(TokenType.ERROR, false);
                                }
                            }
                        } else {
                            isAccept = true;
                        }
                    }
                }
            }
        } 

        if (isTest) System.out.println("<<< Exit formal_parameter_list");

        return isAccept;
    }

    private boolean compoundStatement() {
        // '{' { declaration | statement } '}'
        if (isTest) System.out.println(">>> Enter compound_statement");
        boolean isAccept = false;
        boolean isStop = false;
        
        if (match(TokenType.LEFT_CURLY_BRACE, false)) {
            if (currentToken.getType() == TokenType.RIGHT_CURLY_BRACE) {
                isAccept = true;
            } else {
                while (!isStop) {
                    if (typeSpecifier()) {
                        if (!declaration()) {
                            isStop = true;
                        }
                    } else {
                        if (statement()) {
                            if(!isDone && !isFunction) System.out.println("> Statement executed ...");
                        } else {
                            isStop = true;
                        }
                    }
        
                    currentToken = tokenizer.getNextToken();
                    if (currentToken.getType() == TokenType.RIGHT_CURLY_BRACE) {
                        isStop = true;
                        isAccept = true;
                    }
                }
            }
        }

        if (isTest) System.out.println("<<< Exit compound_statement");

        return isAccept;
    }

    private boolean declaration() {
        // type_specifier Identifier rest_of_declarators
        if (isTest) System.out.println(">>> Enter declaration");
        boolean isAccept = false;
        String varType = currentToken.getName();

        // Don't use type_specifier because typeSpecifier() has already used
        if (match(currentToken.getType(), false)) {
            String varOrFunctName = currentToken.getName();
            if (match(TokenType.IDENTIFIER, false)) {
                if (restOfDeclarators(varType, varOrFunctName)) {
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit declaration");

        return isAccept;
    }

    private boolean statement() {
        //    : ';'     // the null statement
        // | expression ';'  /* expression here should not be empty */
        // | RETURN [ expression ] ';'
        // | compound_statement
        // | IF '(' expression ')' statement [ ELSE statement ]
        // | WHILE '(' expression ')' statement
        // | DO statement WHILE '(' expression ')' ';'
        if (isTest) System.out.println(">>> Enter statement");
        boolean isAccept = false;
        Token peeToken = new Token();
        // tokenizer.getNextToken()

        if (currentToken.getType() == TokenType.SEMICOLON) {
            // ;
            System.out.println("List all define: " + defIdentifier);

            if (isFunction) {
                match(TokenType.SEMICOLON, false);
            } else {
                isAccept = true;
            }
        } else if (match(TokenType.RETURN, false)) {
            // RETURN [ expression ] ';'
            if (currentToken.getType() == TokenType.SEMICOLON) {
                isAccept = true;
            } else if (expression()) {
                if (currentToken.getType() == TokenType.SEMICOLON) {
                    isAccept = true;
                }
            }
        } else if (currentToken.getType() ==  TokenType.LEFT_CURLY_BRACE) {
            // compound_statement
            isAccept = compoundStatement();
        } else if (match(TokenType.IF, false)) {

        } else if (match(TokenType.WHILE, false)) {

        } else if (match(TokenType.DO, false)) {

        } else if (match(TokenType.CIN, false)) {

        } else if (match(TokenType.COUT, false)) {

        } else if (currentToken.getName().equals("Done") && peeToken.getType() == TokenType.LEFT_PAREN) {
            // Done();
            if (match(TokenType.IDENTIFIER, false)) {
                if (match(TokenType.RIGHT_PAREN, false)) {
                    if (match(TokenType.SEMICOLON, false)) {
                        isAccept = true;
                        isDone = true;
                    }
                }
            }
        } else {
            // expression
        }
        if (isTest) System.out.println("<<< Exit statement");

        return isAccept;
    }

    private boolean expression() {
        if (isTest) System.out.println(">>> Enter expression");
        boolean isAccept = false;
        if (isTest) System.out.println("<<< Exit expression");

        return isAccept;
    }

    private boolean constant() {
        if (isTest) System.out.println(">>> Enter Constant");
        boolean isAccept = false;
        TokenType type = currentToken.getType();

        if (type == TokenType.CONSTANT_CHAR || type == TokenType.CONSTANT_FALSE || type == TokenType.CONSTANT_FLOAT ||
            type == TokenType.CONSTANT_INT || type == TokenType.CONSTANT_STRING || type == TokenType.CONSTANT_TRUE) {
            isAccept = match(type, false);
        } else {
            isAccept = match(TokenType.ERROR, false);
        }

        if (isTest) System.out.println("<<< Exit Constant");

        return isAccept;
    }

    private boolean sample() {
        if (isTest) System.out.println(">>> Enter sample");
        boolean isAccept = false;
        if (isTest) System.out.println("<<< Exit sample");

        return isAccept;
    }


}

public class Main {
    static final boolean TokenizerDEBUG = false;
    static final boolean ParserDEBUG = true;
    static Scanner scannerOurC = new Scanner( System.in );
    public static void main(String[] args) {

        if (TokenizerDEBUG) {
            Tokenizer tokenizer = new Tokenizer();
            System.out.println("Our-C running ...");

            while (true) {
                Token token = tokenizer.getNextToken();
                System.out.println(token);

                if (token.getName().equals("Done")) {
                    System.out.println("> Our-C exited ...");
                    break;
                }
            }
        } else if (ParserDEBUG) {
            Parser parser = new Parser();
            String ourC = scannerOurC.nextLine(); // eat one line
            System.out.println("Our-C running ...");
            parser.userInput();
            System.out.println("> Our-C exited ...");
        }
        
    }
}