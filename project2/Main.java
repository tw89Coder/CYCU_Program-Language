// javac -encoding utf-8 Main.java
// java Main

import java.util.Scanner;
import java.util.List;
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
    ERROR,             // lexical error
    OBJECT             // any type
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
    private TokenType type;
    private String value;
    private String operator; // *(Pointer Operator) or &(Address Operator) ; can be empty

    // Constructor
    public Variable(String name, TokenType type, String value, String operator) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.operator = operator;
    }

    // Constructor (Auto default)
    public Variable(String name, TokenType type, String operator) {
        this.name = name;
        this.type = type;

        switch (type) {
            case CHAR:
                this.value = "";
                break;

            case STRING:
                this.value = "null";
                break;

            case INT:
                this.value = "-99999";
                break;

            case FLOAT:
                this.value = "-7777.700";
                break;

            case BOOL:
                this.value = "false";
                break;
        
            default:
                this.value = "";
                break;
        }
        
        this.operator = operator;
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

    // Getter for value
    public String getValue() {
        return value;
    }

    // Setter for value
    public void setValue(String value) {
        this.value = value;
    }

    // Getter for function
    public String getOperator() {
        return operator;
    }

    // Setter for function
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void printListVariable() {
        String typeString = "";

        switch (type) {
            case CHAR:
                typeString = "char";
                break;

            case STRING:
                typeString = "string";
                break;

            case INT:
                typeString = "int";
                break;

            case FLOAT:
                typeString = "float";
                break;

            case BOOL:
                typeString = "bool";
                break;
        
            default:
                typeString = "void";
                break;
        }

        System.out.print(typeString + " " + name + " ;\n");
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                ", operator=" + operator +
                '}';
    }
}

class Array {
    private String name;
    private TokenType type;
    private String operator;              // *(Pointer Operator) or &(Address Operator) ; can be empty
    private String range;
    private Map<String, String> elements; // Key: index, Value: value

    // Constructor
    public Array(String name, TokenType type, String operator, String range) {
        this.name = name;
        this.type = type;
        this.operator = operator;
        this.range = range;
        this.elements = new HashMap<>();

        // Check if range is an integer and set default values accordingly
        try {
            int intRange = Integer.parseInt(range);

            for (int i = 0; i < intRange; i++) {
                String defaultValue = "";

                switch (type) {
                    case CHAR:
                        defaultValue = "";
                        break;

                    case STRING:
                        defaultValue = "null";
                        break;

                    case INT:
                        defaultValue = "-99999";
                        break;

                    case FLOAT:
                        defaultValue = "-7777.700";
                        break;

                    case BOOL:
                        defaultValue = "false";
                        break;

                    default:
                        defaultValue = "";
                        break;
                }
                this.elements.put(String.valueOf(i), defaultValue);
            }
        } catch (NumberFormatException e) {
            // Handle non-integer range or invalid input
            System.err.println("Invalid range: " + range);
        }
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

    // Getter for operator
    public String getOperator() {
        return operator;
    }

    // Setter for operator
    public void setOperator(String operator) {
        this.operator = operator;
    }

    // Getter for range
    public String getRange() {
        return range;
    }

    // Setter for range
    public void setRange(String range) {
        this.range = range;
    }

    // Getter for elements
    public Map<String, String> getElements() {
        return elements;
    }

    // Setter for elements
    public void setElements(Map<String, String> elements) {
        this.elements = elements;
    }

    // Method to add a value and its corresponding index
    public void addValue(String value, String index) {
        try {
            int intIndex = Integer.parseInt(index);
            int intRange = Integer.parseInt(range);

            if (intIndex < intRange) {
                this.elements.put(index, value);
            } else {
                throw new IndexOutOfBoundsException("Index exceeds the array range.");
            }
        } catch (NumberFormatException e) {
            // Handle non-integer index or range
            System.err.println("Invalid index or range: index=" + index + ", range=" + range);
        }
    }

    public void printListVariable() {
        String typeString = "";

        switch (type) {
            case CHAR:
                typeString = "char";
                break;

            case STRING:
                typeString = "string";
                break;

            case INT:
                typeString = "int";
                break;

            case FLOAT:
                typeString = "float";
                break;

            case BOOL:
                typeString = "bool";
                break;
        
            default:
                typeString = "void";
                break;
        }

        System.out.print(typeString + " " + name + "[ " + range + " ] ;\n");
    }
}

class Function {
    private String name;
    private TokenType type;
    private ArrayList<Token> content;
    private ArrayList<Object> parameterList; // Stores Array and Variable objects

    // Constructor
    public Function(String name, TokenType type) {
        this.name = name;
        this.type = type;
        this.content = new ArrayList<Token>();
        this.parameterList = new ArrayList<Object>();
    }

    // Constructor
    public Function(String name, TokenType type, ArrayList<Token> content, ArrayList<Object> parameterList) {
        this.name = name;
        this.type = type;
        this.content = new ArrayList<Token>(content);
        this.parameterList = new ArrayList<Object>(parameterList);
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

    // Getter for content
    public ArrayList<Token> getContent() {
        return content;
    }

    // Setter for content
    public void setContent(ArrayList<Token> content) {
        this.content = new ArrayList<Token>(content);
    }

    // Method to add a Token to the function's content
    public void addToken(Token token) {
        this.content.add(token);
    }

    // Getter for parameterList
    public ArrayList<Object> getParameterList() {
        return parameterList;
    }

    // Setter for parameterList
    public void setParameterList(ArrayList<Object> parameterList) {
        this.parameterList = new ArrayList<Object>(parameterList);
    }

    // Method to add a Variable to the function's parameterList
    public void addVariable(Variable variable) {
        this.parameterList.add(variable);
    }

    // Method to add an Array to the function's parameterList
    public void addArray(Array array) {
        this.parameterList.add(array);
    }

    // Method to retrieve a Variable from parameterList by index
    public Variable getVariable(int index) {
        Object param = parameterList.get(index);
        if (param instanceof Variable) {
            return (Variable) param;
        } else {
            throw new ClassCastException("The parameter at index " + index + " is not a Variable.");
        }
    }

    // Method to retrieve an Array from parameterList by index
    public Array getArray(int index) {
        Object param = parameterList.get(index);
        if (param instanceof Array) {
            return (Array) param;
        } else {
            throw new ClassCastException("The parameter at index " + index + " is not an Array.");
        }
    }

    public void printListFunction() {
        // Variable to track whether we are at the start of a new line
        boolean isNewLine = true;
        // Variable to keep track of the current indentation level
        int indentLevel = 0;
    
        // Iterate over each token in the content list
        for (int i = 0; i < content.size(); i++) {
            // Get the current token and its type and name
            Token currentToken = content.get(i);
            TokenType tokenType = currentToken.getType();
            String tokenName = currentToken.getName();
    
            // If we are at the start of a new line, print spaces for indentation
            if (isNewLine) {
                // If we are at the last token, reset the indent level to 0
                if (i + 1 == content.size()) indentLevel = 0;
    
                // Print spaces based on the current indentation level
                for (int j = 0; j < indentLevel * 2; j++) {
                    System.out.print(" ");
                }
                isNewLine = false;
            }
    
            // Print the current token
            System.out.print(tokenName);
    
            // Determine the next action based on the type of the current token
            if (tokenType == TokenType.SEMICOLON) {
                // If the current token is a semicolon, end the current line
                System.out.print("\n");
                isNewLine = true;  // The next token starts a new line
            } else if (tokenType == TokenType.LEFT_CURLY_BRACE) {
                // If the current token is a left curly brace, increase the indentation level
                System.out.print("\n");
                indentLevel++;  // Increase indent level for left curly brace
                isNewLine = true;
            } else if (tokenType == TokenType.RIGHT_CURLY_BRACE) {
                // If the current token is a right curly brace
                // Output a newline character
                System.out.print("\n");
                isNewLine = true;
            } else if (i + 1 < content.size()) {
                // If there are more tokens, check the next token
                Token nextToken = content.get(i + 1);
                TokenType nextTokenType = nextToken.getType();
                // If the next token is not a left parenthesis and the current token is not an identifier, print a space
                if (tokenType == TokenType.IDENTIFIER) {
                    if (!(nextTokenType == TokenType.LEFT_PAREN || nextTokenType == TokenType.LEFT_BRACKET ||
                          nextTokenType == TokenType.PP || nextTokenType == TokenType.MM || nextTokenType == TokenType.COMMA)) {
                        System.out.print(" ");
                    }
                } else if (tokenType == TokenType.PP || tokenType == TokenType.MM) {
                    if (!(nextTokenType == TokenType.IDENTIFIER)) {
                        System.out.print(" ");
                    }
                } else if (tokenType == TokenType.LEFT_PAREN) {
                    if (!(nextTokenType == TokenType.RIGHT_PAREN)) {
                        System.out.print(" ");
                    }
                } else {
                    if (!(nextTokenType == TokenType.COMMA)) {
                        System.out.print(" ");
                    }
                }
            }

            // If the next token is a right curly brace, decrease the indentation level
            if (i + 1 < content.size() && content.get(i + 1).getType() == TokenType.RIGHT_CURLY_BRACE) {
                indentLevel--;
            }
        }
    }       
}

class ErrorMessage {

    public void lexicalError(char firstChar, int line) {
        System.out.println("Line " + line + " : unrecognized token with first char : '" + firstChar + "'");
    }

    public void syntacticalError(Token token) {
        System.out.println("Line " + token.getLine() + " : unexpected token : '" + token.getName() + "'");
    }

    public void semanticError(Token token) {
        System.out.println("Line " + token.getLine() + " : undefined identifier : '" + token.getName() + "'");
    }
}

class Scope {
    private Map<String, Variable> variables;
    private Map<String, Array> arrays;
    private Map<String, Function> functions;
    private Map<String, Variable> predefineVariable;
    private Map<String, Function> predefineFunctions;
    private Scope parentScope;

    public Scope(Scope parentScope) {
        this.variables = new HashMap<String, Variable>();
        this.arrays = new HashMap<String, Array>();
        this.functions = new HashMap<String, Function>();
        this.predefineVariable = new HashMap<String, Variable>();
        this.predefineFunctions = new HashMap<String, Function>();
        this.parentScope = parentScope;

        addPredefineVariables();
        addPredefinedFunctions();
    }

    // Getter for parentScope
    public Scope getParentScope() {
        return parentScope;
    }

    private void addPredefineVariables() {

        Variable cin = new Variable("cin", TokenType.OBJECT, "");
        this.predefineVariable.put("cin", cin);

        Variable cout = new Variable("cout", TokenType.OBJECT, "");
        this.predefineVariable.put("cout", cout);

    }

    private void addPredefinedFunctions() {

        Function listAllVariables = new Function("ListAllVariables", TokenType.VOID);
        this.predefineFunctions.put("ListAllVariables", listAllVariables);

        Function listAllFunctions = new Function("ListAllFunctions", TokenType.VOID);
        this.predefineFunctions.put("ListAllFunctions", listAllFunctions);

        Function listVariable = new Function("ListVariable", TokenType.VOID);
        this.predefineFunctions.put("ListVariable", listVariable);

        Function listFunction = new Function("ListFunction", TokenType.VOID);
        this.predefineFunctions.put("ListFunction", listFunction);

        Function done = new Function("Done", TokenType.VOID);
        this.predefineFunctions.put("Done", done);

    }

    // Add or update a variable in the current scope
    public void addOrUpdateVariable(Variable variable) {
        String name = variable.getName();
        if (variables.containsKey(name)) {
            // If variable with the same name exists, update it
            variables.put(name, variable);
        } else if (arrays.containsKey(name)) {
            // If name is the same as in arrays, remove from arrays and add to variables
            arrays.remove(name);
            variables.put(name, variable);
        } else {
            // Otherwise, add the new variable
            variables.put(name, variable);
        }
    }

    // Get a variable from the current scope or parent scopes
    public Variable getVariable(String name) {
        Variable variable = variables.get(name);
        if (variable == null && parentScope != null) {
            return parentScope.getVariable(name);
        }
        return variable;
    }

    public void addOrUpdateArray(Array array) {
        String name = array.getName();
        if (arrays.containsKey(name)) {
            // If array with the same name exists, update it
            arrays.put(name, array);
        } else if (variables.containsKey(name)) {
            // If name is the same as in variables, remove from variables and add to arrays
            variables.remove(name);
            arrays.put(name, array);
        } else {
            // Otherwise, add the new array
            arrays.put(name, array);
        }
    }

    // Get an array from the current scope or parent scopes
    public Array getArray(String name) {
        Array array = arrays.get(name);
        if (array == null && parentScope != null) {
            return parentScope.getArray(name);
        }
        return array;
    }

    // Add or update a function in the current scope
    public void addOrUpdateFunction(Function function) {
        String name = function.getName();
        if (functions.containsKey(name)) {
            // If function with the same name exists, update it
            functions.put(name, function);
        } else {
            // Otherwise, add the new function
            functions.put(name, function);
        }
    }

    // Get a function from the current scope or parent scopes
    public Function getFunction(String name) {
        Function function = functions.get(name);
        if (function == null && parentScope != null) {
            return parentScope.getFunction(name);
        }
        return function;
    }

    // Enter a new scope
    public Scope enterScope() {
        return new Scope(this);
    }

    // Exit the current scope and return to the parent scope
    public Scope exitScope() {
        return parentScope;
    }

    // Bubble sort implementation
    private void bubbleSort(List<String> list) {
        int n = list.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    // Swap list[j] and list[j + 1]
                    String temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break; // If no two elements were swapped, then the list is sorted
        }
    }

    // List all variables in the current scope alphabetically by name
    public void listVariables() {
        System.out.println("Variables in current scope:");
        List<String> varNames = new ArrayList<String>(variables.keySet());
        bubbleSort(varNames);
        for (String varName : varNames) {
            System.out.println(varName + " : " + variables.get(varName));
        }
    }

    // List all arrays in the current scope alphabetically by name
    public void listArrays() {
        System.out.println("Arrays in current scope:");
        List<String> arrayNames = new ArrayList<String>(arrays.keySet());
        bubbleSort(arrayNames);
        for (String arrayName : arrayNames) {
            System.out.println(arrayName + " : " + arrays.get(arrayName));
        }
    }

    // List all functions in the current scope alphabetically by name
    public void listFunctions() {
        System.out.println("Functions in current scope:");
        List<String> funcNames = new ArrayList<String>(functions.keySet());
        bubbleSort(funcNames);
        for (String funcName : funcNames) {
            System.out.println(funcName + " : " + functions.get(funcName));
        }
    }

    // List all variable, array, and function names in the current scope alphabetically by name
    public void listNamesSortedByName() {

        // Collect names from both variables and arrays
        List<String> names = new ArrayList<String>();
        names.addAll(variables.keySet());
        names.addAll(arrays.keySet());

        // Sort names alphabetically
        bubbleSort(names);

        // Print names sorted alphabetically
        for (String name : names) {
            System.out.println(name);
        }
    }

    // List all functions sorted by name
    public void listFunctionsSortedByName() {

        // Collect function names
        List<String> funcNames = new ArrayList<String>(functions.keySet());

        // Sort function names alphabetically
        bubbleSort(funcNames);

        // Print function names sorted alphabetically
        for (String funcName : funcNames) {
            System.out.println(funcName + "()");
        }
    }

    // Check if a name exists in variables, arrays, or functions
    public boolean containsName(String name) {
        if (variables.containsKey(name) || arrays.containsKey(name) || functions.containsKey(name)) {
            return true;
        }
        if (parentScope != null) {
            return parentScope.containsName(name);
        }
        return false;
    }

    // Check if a variable name exists in the current scope or parent scopes
    public boolean containsVariableName(String name) {
        if (variables.containsKey(name)) {
            return true;
        }
        if (parentScope != null) {
            return parentScope.containsVariableName(name);
        }
        return false;
    }

    // Check if an array name exists in the current scope or parent scopes
    public boolean containsArrayName(String name) {
        if (arrays.containsKey(name)) {
            return true;
        }
        if (parentScope != null) {
            return parentScope.containsArrayName(name);
        }
        return false;
    }

    // Check if a function name exists in the current scope or parent scopes
    public boolean containsFunctionName(String name) {
        if (functions.containsKey(name)) {
            return true;
        }
        if (parentScope != null) {
            return parentScope.containsFunctionName(name);
        }
        return false;
    }

    public boolean containsPredefineFunctionsName(String name) {
        return predefineFunctions.containsKey(name);
    }

    public boolean containsPredefineVariableName(String name) {
        return predefineVariable.containsKey(name);
    }
}

class Tokenizer {
    private Scanner scanner;
    private String userInput;
    private int currentIndex;
    private int line;
    private ArrayList<Token> tokenList;
    private ErrorMessage errorMessage;
    private boolean isTest;

    public Tokenizer() {
        this.scanner = new Scanner(System.in);
        this.userInput = "";
        this.currentIndex = 0;
        this.line = 1;
        this.tokenList = new ArrayList<Token>();
        this.errorMessage = new ErrorMessage();
        this.isTest = false;                       // TODO: --TestControl -TC

        if (scanner.hasNextLine()) {
            String ourC = scanner.nextLine();      // eat one line
        }
    }

    public void reset(boolean isPause) {
        if (!isPause) userInput = "";
        if (!isPause) currentIndex = 0;
        line = 1;
        tokenList.clear();
    }

    public void resetLine() {
        // Determine whether userInput should be cleared
        boolean shouldClear = true;
    
        // Scan the input from the current index to check for significant content
        for (int i = currentIndex; i < userInput.length(); i++) {
            char c = userInput.charAt(i);
    
            // Skip whitespace characters
            if (Character.isWhitespace(c)) continue;
    
            // Handle single-line comments
            if (c == '/' && i + 1 < userInput.length()) {
                if (userInput.charAt(i + 1) == '/') {
                    // Move index to end of line
                    i = userInput.indexOf('\n', i);
                    continue;
                }
            }
    
            // Found significant content, so don't clear userInput
            shouldClear = false;
            break;
        }
    
        // Clear userInput only if no significant content remains and a reset character is found
        if (shouldClear && currentIndex < userInput.length()) {
            userInput = ""; // Clear the input
            currentIndex = 0;
        }
    
        // Reset the line counter
        line = 1;
    }
    

    public void resetTokenList() {
        tokenList.clear();
    }

    public Token getNextToken() {
        while (true) {
            // If userInput is empty or fully consumed, read a new line
            if (userInput == null || currentIndex >= userInput.length()) {
                // System.out.print(">>> Enter input: ");  // test line
                if (scanner.hasNextLine()) {
                    userInput = scanner.nextLine();
                }
                
                userInput += '\n'; // scanner won't get user Enter. Need to added by myself.
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

                        if (isTest) System.out.println("Tokenizer Test Line: " + line + " CurrentIndex: " + currentIndex);
                        
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
                        currentIndex = userInput.length() - 1; // move to "\n"

                        if (isTest) System.out.println("Tokenizer Test Line(In COMMENT): " + line + " CurrentIndex: " + currentIndex);

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
    
        // Check if the token is just a single '.' and treat it as an error
        if (tokenName.length() == 1 && tokenName.charAt(0) == '.') {
            errorMessage.lexicalError('.', line);
            tokenList.add(new Token(tokenName.toString(), TokenType.ERROR, line));
            return new Token(tokenName.toString(), TokenType.ERROR, line);
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
            } else if (currentChar == '$' || currentChar == '@' || currentChar == '#'|| 
                       currentChar == '~'|| currentChar == '`' || currentChar == '\\') {
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

    // Getter userInput
    public String getUserInput() {
        return userInput;
    }

    // Getter currentIndex
    public int getCurrentIndex() {
        return currentIndex;
    }

    // Getter line
    public int getLine() {
        return line;
    }
}

class Parser {
    private boolean isTest;
    private boolean isDone;
    private boolean isPause;
    private Tokenizer tokenizer;
    private ErrorMessage errorMessage;
    private Token currentToken;
    private Scope globalScope;
    

    public Parser() {
        this.isTest = false;                               // TODO: --TestControl -TC
        this.isDone = false;
        this.isPause = false;                              // Avoid undefined function error removal token
        this.tokenizer = new Tokenizer();
        this.errorMessage = new ErrorMessage();
        this.currentToken = new Token();
        this.globalScope = new Scope(null);
    }

    private boolean match(TokenType expectedType) {
        // match() don't consider undefine. If match() deal undefined symbols, logic will be error.
        if (currentToken.getType() == TokenType.ERROR || expectedType == TokenType.ERROR) {
            if (expectedType == TokenType.ERROR) errorMessage.syntacticalError(currentToken);
            resetAll();

            return false;
        } else if (currentToken.getType() == expectedType) {
            currentToken = tokenizer.getNextToken(); // Advance to the next token

            if (currentToken.getType() == TokenType.ERROR) {
                resetAll();
    
                return false;
            } 

            return true;
        } else {
            errorMessage.syntacticalError(currentToken);
            resetAll();

            return false;
        }
    }

    private boolean checkDeclaration(Token token, boolean isFunctionName, Scope scope) {
        boolean isDefine = false;
        String name = token.getName();

        if (isFunctionName) {
            if (scopeContainsFunction(scope, name)) {
                isDefine = true;
            } else {
                if (currentToken.getLine() > token.getLine()) {
                    isPause = true;
                    currentToken.setLine(1);
                }

                errorMessage.semanticError(token);
                resetAll();
            }
        } else {
            if (scopeContainsName(scope, name)) {
                isDefine = true;
            } else {
                if (currentToken.getLine() > token.getLine()) {
                    isPause = true;
                    currentToken.setLine(1);
                }

                errorMessage.semanticError(token);
                resetAll();
            }
        }

        return isDefine;
    }

    private boolean scopeContainsName(Scope scope, String name) {
        // Check current scope for the variable or array name
        if (scope.containsVariableName(name) || scope.containsArrayName(name) || 
            scope.containsPredefineVariableName(name)) {
            return true;
        }
        // If not found and there's a parent scope, check parent scope
        if (scope.getParentScope() != null) {
            return scopeContainsName(scope.getParentScope(), name);
        }
        // If no parent scope, return false
        return false;
    }

    private boolean scopeContainsFunction(Scope scope, String name) {
        // Check current scope for the variable or array name
        if (scope.containsFunctionName(name) || scope.containsPredefineFunctionsName(name)) {
            return true;
        }
        // If not found and there's a parent scope, check parent scope
        if (scope.getParentScope() != null) {
            return scopeContainsFunction(scope.getParentScope(), name);
        }
        // If no parent scope, return false
        return false;
    }

    private void resetAll() {
        tokenizer.reset(isPause);
    }

    // Overloaded method to define a token for a variable
    private void defineToken(Variable variable, Scope scope) {
        if (scope == globalScope) {
            String name = variable.getName();

            if (globalScope.containsArrayName(name) || globalScope.containsName(name)) {
                System.out.println("New definition of " + name + " entered ...");
            } else {
                System.out.println("Definition of " + name + " entered ...");
            }
            
            globalScope.addOrUpdateVariable(variable);
        } else {
            scope.addOrUpdateVariable(variable);
        }
    }

    // Overloaded method to define a token for an array
    private void defineToken(Array array, Scope scope) {
        if (scope == globalScope) {
            String name = array.getName();
            
            if (globalScope.containsArrayName(name) || globalScope.containsName(name)) {
                System.out.println("New definition of " + name + " entered ...");
            } else {
                System.out.println("Definition of " + name + " entered ...");
            }
            
            globalScope.addOrUpdateArray(array);
        } else {
            scope.addOrUpdateArray(array);
        }
    }

    // Overloaded method to define a token for a function
    private void defineToken(Function function, Scope scope) {
        if (scope == globalScope) {
            String name = function.getName();

            if (globalScope.containsFunctionName(name)) {
                System.out.println("New definition of " + name + "() entered ...");
            } else {
                System.out.println("Definition of " + name + "() entered ...");
            }
            
            globalScope.addOrUpdateFunction(function);
        } else {
            scope.addOrUpdateFunction(function);
        }
        
    }

    public void userInput() {
        if (isTest) System.out.println(">>> Enter userInput");

        System.out.println("Our-C running ...");

        while (!isDone) {
            System.out.print("> ");
            tokenizer.resetTokenList();
            tokenizer.resetLine();

            if (isPause) {
                isPause = false;
                currentToken.setLine(1);
            } else {
                currentToken = tokenizer.getNextToken();
            }

            if (currentToken.getType() == TokenType.VOID || typeSpecifier()) {

                if (definition(globalScope)) {
                    if (isTest) System.out.println("> Definition executed ...");
                } else {
                    resetAll();
                }
            } else {
                if (statement(globalScope)) {
                    if(!isDone) System.out.println("Statement executed ...");
                } else {
                    resetAll();
                }
            }
        }

        System.out.println("Our-C exited ...");

        if (isTest) System.out.println("<<< Exit userInput");
    }

    private boolean definition(Scope scope) {
        // :           VOID Identifier function_definition_without_ID 
        // | type_specifier Identifier function_definition_or_declarators
        if (isTest) System.out.println(">>> Enter definition");
        boolean isAccept = false;
        TokenType varType = currentToken.getType();
        String varOrFunctName = "";

        if (currentToken.getType() == TokenType.VOID) {
            // VOID Identifier function_definition_without_ID 
            if (match(TokenType.VOID)) {
                varOrFunctName = currentToken.getName(); // must be function

                if (match(TokenType.IDENTIFIER)) {
                    if (functionDefinitionWithoutID(varType, varOrFunctName, scope)) {
                        isAccept = true;
                    }
                }
            }
        } else {
            // type_specifier Identifier function_definition_or_declarators
            if (match(currentToken.getType())) {
                varOrFunctName = currentToken.getName();

                if (match(TokenType.IDENTIFIER)) {
                    if (functionDefinitionOrDeclarators(varType, varOrFunctName, scope)) {
                        isAccept = true;
                    }
                }
            }
        }

        if (isTest) System.out.println("<<< Exit definition. isAccept = " + isAccept);

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

        if (isTest) System.out.println("<<< Exit type_specifier. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean functionDefinitionOrDeclarators(TokenType varType, String varOrFunctName, Scope scope) {
        // : function_definition_without_ID
        // | rest_of_declarators
        if (isTest) System.out.println(">>> Enter function_definition_or_declarators");
        boolean isAccept = false;

        if (currentToken.getType() == TokenType.LEFT_PAREN) {
            // function_definition_without_ID
            if (functionDefinitionWithoutID(varType, varOrFunctName, scope)) {
                isAccept = true;
            }
        } else {
            // rest_of_declarators
            if (restOfDeclarators(varType, varOrFunctName, scope)) {
                isAccept = true;
            }
        }

        if (isTest) System.out.println("<<< Exit function_definition_or_declarators. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfDeclarators(TokenType varType, String varOrFunctName, Scope scope) {
        // : [ '[' Constant ']' ] 
        //   { ',' Identifier [ '[' Constant ']' ] } ';'
        if (isTest) System.out.println(">>> Enter rest_of_declarators");
        boolean isAccept = false;
        ArrayList<Object> variablesTemp = new ArrayList<Object>();

        if (currentToken.getType() == TokenType.SEMICOLON) {
            defineToken(new Variable(varOrFunctName, varType, ""), scope);
            isAccept = true;
        } else if (currentToken.getType() == TokenType.LEFT_BRACKET) {
            if (match(TokenType.LEFT_BRACKET)) {
                // [ '[' Constant ']' ] 
                String arrConst = currentToken.getName();

                if (constant(scope)) {
                    if (match(TokenType.RIGHT_BRACKET)) {
                        // Array
                        if (currentToken.getType() == TokenType.SEMICOLON) {
                            defineToken(new Array(varOrFunctName, varType, "", arrConst), scope);
                            isAccept = true;
                        } else {
                            boolean isStop = false;
                            variablesTemp.add(new Array(varOrFunctName, varType, "", arrConst));

                            while (!isStop) {
                                if (match(TokenType.COMMA)) {
                                    varOrFunctName = currentToken.getName();

                                    if (match(TokenType.IDENTIFIER)) {
                                        if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                                            if (match(TokenType.LEFT_BRACKET)) {
                                                arrConst = currentToken.getName();

                                                if (constant(scope)) {
                                                    if (match(TokenType.RIGHT_BRACKET)) {
                                                        // Array
                                                        variablesTemp.add(new Array(varOrFunctName, varType, "", arrConst));

                                                        if (currentToken.getType() == TokenType.SEMICOLON) {
                                                            for (Object obj : variablesTemp) {
                                                                if (obj instanceof Variable) {
                                                                    defineToken((Variable) obj, scope);
                                                                } else if (obj instanceof Array) {
                                                                    defineToken((Array) obj, scope);
                                                                }else {
                                                                    throw new IllegalArgumentException("Unknown type: " + obj.getClass().getName());
                                                                }
                                                            }

                                                            isStop = true;
                                                            isAccept = true;
                                                        }
                                                    } else {
                                                        isStop = true;
                                                    }
                                                }
                                            }
                                            
                                        } else {
                                            // Variable
                                            variablesTemp.add(new Variable(varOrFunctName, varType, ""));
                                            
                                            if (currentToken.getType() == TokenType.SEMICOLON) {
                                                for (Object obj : variablesTemp) {
                                                    if (obj instanceof Variable) {
                                                        defineToken((Variable) obj, scope);
                                                    } else if (obj instanceof Array) {
                                                        defineToken((Array) obj, scope);
                                                    }else {
                                                        throw new IllegalArgumentException("Unknown type: " + obj.getClass().getName());
                                                    }
                                                }

                                                isStop = true;
                                                isAccept = true;
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
            variablesTemp.add(new Variable(varOrFunctName, varType, ""));

            boolean isStop = false;
            while (!isStop) {
                if (match(TokenType.COMMA)) {
                    varOrFunctName = currentToken.getName();

                    if (match(TokenType.IDENTIFIER)) {
                        if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                            if (match(TokenType.LEFT_BRACKET)) {
                                String arrConst = currentToken.getName();
                                
                                if (constant(scope)) {
                                    if (match(TokenType.RIGHT_BRACKET)) {
                                        // Array
                                        variablesTemp.add(new Array(varOrFunctName, varType, "", arrConst));
                                        
                                        if (currentToken.getType() == TokenType.SEMICOLON) {
                                            for (Object obj : variablesTemp) {
                                                if (obj instanceof Variable) {
                                                    defineToken((Variable) obj, scope);
                                                } else if (obj instanceof Array) {
                                                    defineToken((Array) obj, scope);
                                                }else {
                                                    throw new IllegalArgumentException("Unknown type: " + obj.getClass().getName());
                                                }
                                            }
            
                                            isStop = true;
                                            isAccept = true;
                                        }
                                    } else {
                                        isStop = true;
                                    }
                                }
                            }
                        } else {
                            variablesTemp.add(new Variable(varOrFunctName, varType, ""));
                            
                            if (currentToken.getType() == TokenType.SEMICOLON) {
                                for (Object obj : variablesTemp) {
                                    if (obj instanceof Variable) {
                                        defineToken((Variable) obj, scope);
                                    } else if (obj instanceof Array) {
                                        defineToken((Array) obj, scope);
                                    }else {
                                        throw new IllegalArgumentException("Unknown type: " + obj.getClass().getName());
                                    }
                                }

                                isStop = true;
                                isAccept = true;
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

        if (isTest) System.out.println("<<< Exit rest_of_declarators. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean functionDefinitionWithoutID(TokenType varType, String varOrFunctName, Scope scope) {
        // : '(' [ VOID | formal_parameter_list ] ')' compound_statement
        if (isTest) System.out.println(">>> Enter function_definition_without_ID");
        boolean isAccept = false;
        Scope functionScope = scope.enterScope();

        if (match(TokenType.LEFT_PAREN)) {
            ArrayList<Object> functPar = new ArrayList<Object>();

            if (currentToken.getType() == TokenType.VOID) {
                functPar.add(new Variable("void", TokenType.VOID, ""));

                if (match(TokenType.VOID)) {
                    if (match(TokenType.RIGHT_PAREN)) {
                        if (compoundStatement(functionScope)) {
                            defineToken(new Function(varOrFunctName, varType, tokenizer.getTokenList(), functPar), scope);
                            tokenizer.resetTokenList();
                            isAccept = true;
                        }
                    }
                } 
            } else if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                if (match(TokenType.RIGHT_PAREN)) {
                    if (compoundStatement(functionScope)) {
                        defineToken(new Function(varOrFunctName, varType, tokenizer.getTokenList(), functPar), scope);
                        tokenizer.resetTokenList();
                        isAccept = true;
                    }
                }
            } else {
                // formal_parameter_list
                if (typeSpecifier()) {
                    if (formalParameterList(functPar, varOrFunctName, functionScope)) {
                        for (Object obj : functPar) {
                            if (obj instanceof Variable) {
                                defineToken((Variable) obj, functionScope);
                            } else if (obj instanceof Array) {
                                defineToken((Array) obj, functionScope);
                            }else {
                                throw new IllegalArgumentException("Unknown type: " + obj.getClass().getName());
                            }
                        }

                        if (match(TokenType.RIGHT_PAREN)) {
                            if (compoundStatement(functionScope)) {
                                defineToken(new Function(varOrFunctName, varType, tokenizer.getTokenList(), functPar), scope);
                                tokenizer.resetTokenList();
                                isAccept = true;
                            }
                        }
                    }
                } else {
                    isAccept = match(TokenType.ERROR);
                }
            }
        }

        functionScope.exitScope();

        if (isTest) System.out.println("<<< Exit function_definition_without_ID. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean formalParameterList(ArrayList<Object> functPar, String varOrFunctName, Scope scope) {
        // : type_specifier [ '&' ] Identifier [ '[' Constant ']' ] 
        //   { ',' type_specifier [ '&' ] Identifier [ '[' Constant ']' ] }
        if (isTest) System.out.println(">>> Enter formal_parameter_list");
        boolean isAccept = false;
        String parName = "";
        String operator = "";

        if (typeSpecifier()) {
            TokenType parType = currentToken.getType();

            if (match(currentToken.getType())) {
                if (currentToken.getType() == TokenType.BITWISE_AND) {
                    operator = "&";

                    if (match(TokenType.BITWISE_AND)) {
                        parName = currentToken.getName();

                        if (match(TokenType.IDENTIFIER)) {
                            if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                                // Array
                                if (match(TokenType.LEFT_BRACKET)) {
                                    String range =currentToken.getName();
    
                                    if (constant(scope)) {
                                        if (match(TokenType.RIGHT_BRACKET)) {
                                            functPar.add(new Array(parName, parType, operator, range));
                                            
                                            if (currentToken.getType() == TokenType.COMMA) {
                                                if (match(TokenType.COMMA)) {
                                                    if (typeSpecifier()) {
                                                        isAccept = formalParameterList(functPar, varOrFunctName, scope);
                                                    } else {
                                                        isAccept = match(TokenType.ERROR);
                                                    }
                                                }
                                            } else {                                                
                                                isAccept = true;
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Variable
                                functPar.add(new Variable(parName, parType, operator));

                                if (currentToken.getType() == TokenType.COMMA) {
                                    if (match(TokenType.COMMA)) {
                                        if (typeSpecifier()) {
                                            isAccept = formalParameterList(functPar, varOrFunctName, scope);
                                        } else {
                                            isAccept = match(TokenType.ERROR);
                                        }
                                    }
                                } else {
                                    isAccept = true;
                                }
                            }
                        }
                    }
                } else {
                    parName = currentToken.getName();
    
                    if (match(TokenType.IDENTIFIER)) {
                        if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                            // Array
                            if (match(TokenType.LEFT_BRACKET)) {
                                String range = currentToken.getName();

                                if (constant(scope)) {
                                    if (match(TokenType.RIGHT_BRACKET)) {
                                        functPar.add(new Array(parName, parType, operator, range));
                                        
                                        if (currentToken.getType() == TokenType.COMMA) {
                                            if (match(TokenType.COMMA)) {
                                                if (typeSpecifier()) {
                                                    isAccept = formalParameterList(functPar, varOrFunctName, scope);
                                                } else {
                                                    isAccept = match(TokenType.ERROR);
                                                }
                                            }
                                        } else {                                                
                                            isAccept = true;
                                        }
                                    }
                                }
                            }
                        } else {
                            // Variable
                            functPar.add(new Variable(parName, parType, operator));

                            if (currentToken.getType() == TokenType.COMMA) {
                                if (match(TokenType.COMMA)) {
                                    if (typeSpecifier()) {
                                        isAccept = formalParameterList(functPar, varOrFunctName, scope);
                                    } else {
                                        isAccept = match(TokenType.ERROR);
                                    }
                                }
                            } else {
                                isAccept = true;
                            }
                        }
                    }
                }
            } 
        } else {
            isAccept = match(TokenType.ERROR);
        }
        

        if (isTest) System.out.println("<<< Exit formal_parameter_list. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean compoundStatement(Scope scope) {
        // '{' { declaration | statement } '}'
        if (isTest) System.out.println(">>> Enter compound_statement");
        boolean isAccept = false;
        boolean isStop = false;
        
        if (currentToken.getType() == TokenType.LEFT_CURLY_BRACE) {
            while (!isStop) {
                if (isPause) {
                    isPause = false;
                } else {
                    currentToken = tokenizer.getNextToken();
                }

                if (currentToken.getType() == TokenType.RIGHT_CURLY_BRACE) {
                    isStop = true;
                    isAccept = true;
                } else if (typeSpecifier()) {
                    if (!declaration(scope)) {
                        isStop = true;
                    }
                } else {
                    if (!statement(scope)) {
                        isStop = true;
                    }
                }
            }
        } else {
            errorMessage.syntacticalError(currentToken);
        }

        if (isTest) System.out.println("<<< Exit compound_statement. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean declaration(Scope scope) {
        // type_specifier Identifier rest_of_declarators
        if (isTest) System.out.println(">>> Enter declaration");
        boolean isAccept = false;
        TokenType varType = currentToken.getType();

        // Don't use type_specifier because typeSpecifier() has already used
        if (match(currentToken.getType())) {
            String varOrFunctName = currentToken.getName();

            if (match(TokenType.IDENTIFIER)) {
                if (restOfDeclarators(varType, varOrFunctName, scope)) {
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit declaration. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean statement(Scope scope) {
        //    : ';'     // the null statement
        // | expression ';'  /* expression here should not be empty */
        // | RETURN [ expression ] ';'
        // | compound_statement
        // | IF '(' expression ')' statement [ ELSE statement ]
        // | WHILE '(' expression ')' statement
        // | DO statement WHILE '(' expression ')' ';'
        if (isTest) System.out.println(">>> Enter statement");
        boolean isAccept = false;
        
        switch (currentToken.getType()) {
            case SEMICOLON:
                // ;
                isAccept = true;
                break;
            
            case RETURN:
                // RETURN [ expression ] ';'
                if (match(TokenType.RETURN)) {
                    if (currentToken.getType() == TokenType.SEMICOLON) {
                            isAccept = true;
                    } else if (expression(scope)) {
                        if (currentToken.getType() == TokenType.SEMICOLON) {
                                isAccept = true;
                        } else {
                            errorMessage.syntacticalError(currentToken);
                        }
                    }
                }
                break;
    
            case LEFT_CURLY_BRACE:
                // compound_statement
                Scope subScope = scope.enterScope();
                isAccept = compoundStatement(subScope);
                subScope.exitScope();

                break;
    
            case IF:
                // IF '(' expression ')' statement [ ELSE statement ]
                if (isTest) System.out.println(">>> >>> Enter IF statement. -----> in statement()");
                Scope subScopeIF = scope.enterScope();

                if (match(TokenType.IF)) {
                    if (match(TokenType.LEFT_PAREN)) {
                        if (expression(subScopeIF)) {
                            if (match(TokenType.RIGHT_PAREN)) {
                                if (statement(subScopeIF)) {
                                    subScopeIF.exitScope();
                                    currentToken = tokenizer.getNextToken();

                                    if (currentToken.getType() == TokenType.ELSE) {
                                        Scope subScopeELSE = scope.enterScope();

                                        if (match(TokenType.ELSE)) {
                                            isAccept = statement(subScopeELSE);
                                            subScopeELSE.exitScope();
                                        }
                                    } else {
                                        isPause = true;
                                        isAccept = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if(isTest) System.out.println("<<< <<< Exit IF statement. -----> in statement()");
                break;
    
            case WHILE:
                // WHILE '(' expression ')' statement
                Scope subScopeWHILE = scope.enterScope();

                if (match(TokenType.WHILE)) {
                    if (match(TokenType.LEFT_PAREN)) {
                        if (expression(subScopeWHILE)) {
                            if (match(TokenType.RIGHT_PAREN)) {
                                isAccept = statement(subScopeWHILE);
                                subScopeWHILE.exitScope();
                            }
                        }
                    }
                }
                break;
    
            case DO:
                // DO statement WHILE '(' expression ')' ';'
                Scope subScopeDO = scope.enterScope();

                if (match(TokenType.DO)) {
                    if (statement(subScopeDO)) {
                        subScopeDO.exitScope();
                        
                        if (match(TokenType.WHILE)) {
                            Scope subScopeDoWHILE = scope.enterScope();

                            if (match(TokenType.LEFT_PAREN)) {
                                if (expression(subScopeDoWHILE)) {
                                    subScopeDoWHILE.exitScope();

                                    if (match(TokenType.RIGHT_PAREN)) {
                                        if (currentToken.getType() == TokenType.SEMICOLON) {
                                            isAccept = true;
                                        } else {
                                            errorMessage.syntacticalError(currentToken);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
    
            default:
                // expression ';'  /* expression here should not be empty */
                if (expression(scope)) {
                    if (currentToken.getType() == TokenType.SEMICOLON) {
                        isAccept = true;
                    } else {
                        errorMessage.syntacticalError(currentToken);
                    }
                }
                break;
        }
    
        if (isTest) System.out.println("<<< Exit statement. isAccept = " + isAccept);
        
        return isAccept;
    }

    private boolean expression(Scope scope) {
        // : basic_expression { ',' basic_expression }
        if (isTest) System.out.println(">>> Enter expression");
        boolean isAccept = false;

        if (basicExpression(scope)) {
            if (currentToken.getType() == TokenType.COMMA) {
                boolean isStop = false;

                while (!isStop) {
                    if (match(TokenType.COMMA)) {
                        if (basicExpression(scope)) {
                            if (!(currentToken.getType() == TokenType.COMMA)) {
                                isStop = true;
                                isAccept = true;
                            }
                        }
                    }
                }
            } else {
                isAccept = true;
            }
        }

        if (isTest) System.out.println("<<< Exit expression. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean basicExpression(Scope scope) {
        // : Identifier rest_of_Identifier_started_basic_exp
        // | ( PP | MM ) Identifier rest_of_PPMM_Identifier_started_basic_exp
        // | sign { sign } signed_unary_exp romce_and_romloe
        // | ( Constant | '(' expression ')' ) romce_and_romloe
        if (isTest) System.out.println(">>> Enter basic_expression");
        boolean isAccept = false;
        Token identifier = new Token();
        
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            identifier = currentToken;

            if (match(TokenType.IDENTIFIER)) {
                isAccept = restzofIdentifierStartedBasicExp(scope, identifier);
            }
        } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
            if (match(currentToken.getType())) {
                if (currentToken.getType() == TokenType.IDENTIFIER) {
                    identifier = currentToken;
                    
                    if (!checkDeclaration(identifier, false, scope)) return false;
                }
                
                if (match(TokenType.IDENTIFIER)) {
                    isAccept = restOfPPMMIdentifierStartedBasicExp(scope);
                }
            }
        } else if (sign()) {
            if (match(currentToken.getType())) {
                boolean isStop = false;

                while (!isStop) {
                    if (sign()) {
                        boolean nop = match(currentToken.getType());
                    } else {
                        isStop = true;
                    }
                }

                if (signedUnaryExp(scope)) {
                    isAccept = romceAndRomloe(scope);
                }
            }
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            if (match(TokenType.LEFT_PAREN)){
                if (expression(scope)) {
                    if (match(TokenType.RIGHT_PAREN)) {
                        isAccept = romceAndRomloe(scope);
                    }
                }
            }
        } else {
            if (constant(scope)) {
                isAccept = romceAndRomloe(scope);
            }
        }

        if (isTest) System.out.println("<<< Exit basic_expression. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restzofIdentifierStartedBasicExp(Scope scope, Token identifier) {
        // : [ '[' expression ']' ]
        //       ( assignment_operator basic_expression             (Must be array)
        //       | 
        //       [ PP | MM ] romce_and_romloe                       (Must be variable)
        //     )
        //   | '(' [ actual_parameter_list ] ')' romce_and_romloe   (Must be function)
        if (isTest) System.out.println(">>> Enter rest_of_Identifier_started_basic_exp");
        boolean isAccept = false;
        
        if (currentToken.getType() == TokenType.LEFT_PAREN) {
            // '(' [ actual_parameter_list ] ')' romce_and_romloe
            if (!checkDeclaration(identifier, true, scope)) return false;

            if (match(TokenType.LEFT_PAREN)) {
                if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                    if (match(TokenType.RIGHT_PAREN)) {
                        if (romceAndRomloe(scope)) {
                            isAccept = true;

                            if (currentToken.getType() == TokenType.SEMICOLON) {
                                if (identifier.getName().equals("ListAllVariables")) {
                                    globalScope.listNamesSortedByName();
                                } else if (identifier.getName().equals("ListAllFunctions")) {
                                    globalScope.listFunctionsSortedByName();
                                } else if (identifier.getName().equals("Done")) {
                                    isDone = true;
                                }
                            }
                        }
                    }
                } else {
                    ArrayList<String> list = new ArrayList<String>();   

                    if (actualParameterList(scope, list)) {
                        if (match(TokenType.RIGHT_PAREN)) {
                            isAccept = romceAndRomloe(scope);

                            if (currentToken.getType() == TokenType.SEMICOLON && list.size() == 1) {
                                if (identifier.getName().equals("ListVariable")) {
                                    if (globalScope.containsVariableName(list.get(0))) {
                                        globalScope.getVariable(list.get(0)).printListVariable();
                                    } else if (globalScope.containsArrayName(list.get(0))) {
                                        globalScope.getArray(list.get(0)).printListVariable();
                                    }
                                } else if (identifier.getName().equals("ListFunction")) {
                                    if (globalScope.containsFunctionName(list.get(0))) {
                                        globalScope.getFunction(list.get(0)).printListFunction();
                                    }
                                } 
                            }
                        }
                    }
                }
            }
        } else {
            if (!checkDeclaration(identifier, false, scope)) return false;

            if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                if (match(TokenType.LEFT_BRACKET)) {
                    if (expression(scope)) {
                        if (match(TokenType.RIGHT_BRACKET)) {
                            if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                                if (match(currentToken.getType())) {
                                    isAccept = romceAndRomloe(scope);
                                }
                            } else {
                                if (assignmentOperator(scope)) {
                                    if (match(currentToken.getType())) {
                                        isAccept = basicExpression(scope);
                                    }
                                } else {
                                    isAccept = romceAndRomloe(scope);
                                }
                            }
                        }
                    }
                }
            } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                if (match(currentToken.getType())) {
                    isAccept = romceAndRomloe(scope);
                }
            } else {
                if (assignmentOperator(scope)) {
                    if (match(currentToken.getType())) {
                        isAccept = basicExpression(scope);
                    }
                } else {
                    isAccept = romceAndRomloe(scope);
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_Identifier_started_basic_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfPPMMIdentifierStartedBasicExp(Scope scope) {
        // : [ '[' expression ']' ] romce_and_romloe 
        if (isTest) System.out.println(">>> Enter rest_of_PPMM_Identifier_started_basic_exp");
        boolean isAccept = false;
        
        if (currentToken.getType() == TokenType.LEFT_BRACKET) {
            if (match(TokenType.LEFT_BRACKET)) {
                if (expression(scope)) {
                    if (match(TokenType.RIGHT_BRACKET)) {
                        isAccept = romceAndRomloe(scope);
                    }
                }
            }
        } else {
            isAccept = romceAndRomloe(scope);
        }

        if (isTest) System.out.println("<<< Exit rest_of_PPMM_Identifier_started_basic_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean sign() {
        // : '+' | '-' | '!'
        if (isTest) System.out.println(">>> Enter sign");
        boolean isAccept = false;

        if (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS ||
            currentToken.getType() == TokenType.NOT) {
            isAccept = true;
        }

        if (isTest) System.out.println("<<< Exit sign. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean actualParameterList (Scope scope, ArrayList<String> list) {
        // : basic_expression { ',' basic_expression }
        if (isTest) System.out.println(">>> Enter actual_parameter_list");
        boolean isAccept = false;

        if (isConstant(scope)) list.add(currentToken.getName());

        if (basicExpression(scope)) {
            if (currentToken.getType() == TokenType.COMMA) {
                boolean isStop = false;

                while (!isStop) {
                    if (match(TokenType.COMMA)) {
                        if (isConstant(scope)) list.add(currentToken.getName());
                        
                        if (basicExpression(scope)) {
                            if (!(currentToken.getType() == TokenType.COMMA)) {
                                isStop = true;
                                isAccept = true;
                            }
                        }
                    }
                }
            } else {
                isAccept = true;
            }
        }

        if (isTest) System.out.println("<<< Exit actual_parameter_list. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean assignmentOperator(Scope scope) {
        if (isTest) System.out.println(">>> Enter assignment_operator");
        // : '=' | TE | DE | RE | PE | ME
        boolean isAccept = false;
        
        if (currentToken.getType() == TokenType.ASSIGNMENT || currentToken.getType() == TokenType.TE ||
            currentToken.getType() == TokenType.DE || currentToken.getType() == TokenType.RE || 
            currentToken.getType() == TokenType.PE || currentToken.getType() == TokenType.ME) {
            isAccept = true;
        }

        if (isTest) System.out.println("<<< Exit assignment_operator. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean romceAndRomloe(Scope scope) { // rest_of_maybe_conditional_exp_and_rest_of_maybe_logical_OR_exp
        // : rest_of_maybe_logical_OR_exp [ '?' basic_expression ':' basic_expression ]
        if (isTest) System.out.println(">>> Enter romce_and_romloe");
        boolean isAccept = false;
        
        if (restOfMaybeLogicalORExp(scope)) {
            if (currentToken.getType() == TokenType.CONDITIONAL) {
                if (match(TokenType.CONDITIONAL)) {
                    if (basicExpression(scope)) {
                        if (match(TokenType.COLON)) {
                            isAccept = basicExpression(scope);
                        }
                    }
                }
            } else {
                isAccept = true;
            }
        }

        if (isTest) System.out.println("<<< Exit romce_and_romloe. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeLogicalORExp(Scope scope) {
        // : rest_of_maybe_logical_AND_exp { OR maybe_logical_AND_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_logical_OR_exp");
        boolean isAccept = false;
        
        if (restOfMaybeLogicalANDExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.OR) {
                    if (match(TokenType.OR)) {
                        if (!maybeLogicalANDExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_logical_OR_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeLogicalANDExp(Scope scope) {
        // : maybe_bit_OR_exp { AND maybe_bit_OR_exp }
        if (isTest) System.out.println(">>> Enter maybe_logical_AND_exp");
        boolean isAccept = false;
        
        if (maybeBitORExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.AND) {
                    if (match(TokenType.AND)) {
                        if (!maybeBitORExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_logical_AND_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeLogicalANDExp(Scope scope) {
        // : rest_of_maybe_bit_OR_exp { AND maybe_bit_OR_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_logical_AND_exp");
        boolean isAccept = false;
        
        if (restOfMaybeBitORExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.AND) {
                    if (match(TokenType.AND)) {
                        if (!maybeBitORExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_logical_AND_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeBitORExp(Scope scope) {
        // : maybe_bit_ex_OR_exp { '|' maybe_bit_ex_OR_exp }
        if (isTest) System.out.println(">>> Enter maybe_bit_OR_exp");
        boolean isAccept = false;
        
        if (maybeBitExORExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_OR) {
                    if (match(TokenType.BITWISE_OR)) {
                        if (!maybeBitExORExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_bit_OR_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeBitORExp(Scope scope) {
        // : rest_of_maybe_bit_ex_OR_exp { '|' maybe_bit_ex_OR_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_bit_OR_exp");
        boolean isAccept = false;
        
        if (restOfMaybeBitExORExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_OR) {
                    if (match(TokenType.BITWISE_OR)) {
                        if (!maybeBitExORExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_bit_OR_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeBitExORExp(Scope scope) {
        // : maybe_bit_AND_exp { '^' maybe_bit_AND_exp }
        if (isTest) System.out.println(">>> Enter maybe_bit_ex_OR_exp");
        boolean isAccept = false;
        
        if (maybeBitANDExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.XOR) {
                    if (match(TokenType.XOR)) {
                        if (!maybeBitANDExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_bit_ex_OR_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeBitExORExp(Scope scope) {
        // : rest_of_maybe_bit_AND_exp { '^' maybe_bit_AND_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_bit_ex_OR_exp");
        boolean isAccept = false;
        
        if (restOfMaybeBitANDExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.XOR) {
                    if (match(TokenType.XOR)) {
                        if (!maybeBitANDExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_bit_ex_OR_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeBitANDExp(Scope scope) {
        // : maybe_equality_exp { '&' maybe_equality_exp }
        if (isTest) System.out.println(">>> Enter maybe_bit_AND_exp");
        boolean isAccept = false;
        
        if (maybeEqualityExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_AND) {
                    if (match(TokenType.RIGHT_PAREN)) {
                        if (!maybeEqualityExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_bit_AND_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeBitANDExp(Scope scope) {
        // : rest_of_maybe_equality_exp { '&' maybe_equality_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_bit_AND_exp");
        boolean isAccept = false;
        
        if (restOfMaybeEqualityExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_AND) {
                    if (match(TokenType.RIGHT_PAREN)) {
                        if (!maybeEqualityExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_bit_AND_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeEqualityExp(Scope scope) {
        // : maybe_relational_exp 
        //   { ( EQ | NEQ ) maybe_relational_exp}
        if (isTest) System.out.println(">>> Enter maybe_equality_exp");
        boolean isAccept = false;
        
        if (maybeRelationalExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.EQ || currentToken.getType() == TokenType.NEQ) {
                    if (match(currentToken.getType())) {
                        if (!maybeRelationalExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_equality_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeEqualityExp(Scope scope) {
        // : rest_of_maybe_relational_exp 
        //   { ( EQ | NEQ ) maybe_relational_exp}
        if (isTest) System.out.println(">>> Enter rest_of_maybe_equality_exp");
        boolean isAccept = false;
        
        if (restOfMaybeRelationalExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.EQ || currentToken.getType() == TokenType.NEQ) {
                    if (match(currentToken.getType())) {
                        if (!maybeRelationalExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_equality_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeRelationalExp(Scope scope) {
        // : maybe_shift_exp 
        //   { ( '<' | '>' | LE | GE ) maybe_shift_exp }
        if (isTest) System.out.println(">>> Enter maybe_relational_exp");
        boolean isAccept = false;
        
        if (maybeShiftExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.LESS_THAN || currentToken.getType() == TokenType.GREATER_THAN ||
                    currentToken.getType() == TokenType.LE || currentToken.getType() == TokenType.GE) {
                    if (match(currentToken.getType())) {
                        if (!maybeShiftExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_relational_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeRelationalExp(Scope scope) {
        // : rest_of_maybe_shift_exp 
        //   { ( '<' | '>' | LE | GE ) maybe_shift_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_relational_exp ");
        boolean isAccept = false;
        
        if (restOfMaybeShiftExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.LESS_THAN || currentToken.getType() == TokenType.GREATER_THAN ||
                    currentToken.getType() == TokenType.LE || currentToken.getType() == TokenType.GE) {
                    if (match(currentToken.getType())) {
                        if (!maybeShiftExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_relational_exp . isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeShiftExp(Scope scope) {
        // : maybe_additive_exp { ( LS | RS ) maybe_additive_exp }
        if (isTest) System.out.println(">>> Enter maybe_shift_exp");
        boolean isAccept = false;
        
        if (maybeAdditiveExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.LS || currentToken.getType() == TokenType.RS) {
                    if (match(currentToken.getType())) {
                        if (!maybeAdditiveExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_shift_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeShiftExp(Scope scope) {
        // : rest_of_maybe_additive_exp { ( LS | RS ) maybe_additive_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_shift_exp");
        boolean isAccept = false;

        if (restOfMaybeAdditiveExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.LS || currentToken.getType() == TokenType.RS) {
                    if (match(currentToken.getType())) {
                        if (!maybeAdditiveExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_shift_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeAdditiveExp(Scope scope) {
        // : maybe_mult_exp { ( '+' | '-' ) maybe_mult_exp }
        if (isTest) System.out.println(">>> Enter maybe_additive_exp");
        boolean isAccept = false;
        
        if (maybeMultExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
                    if (match(currentToken.getType())) {
                        if (!maybeMultExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_additive_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeAdditiveExp(Scope scope) {
        // : rest_of_maybe_mult_exp { ( '+' | '-' ) maybe_mult_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_additive_exp");
        boolean isAccept = false;
        
        if (restOfMaybeMultExp(scope)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
                    if (match(currentToken.getType())) {
                        if (!maybeMultExp(scope)) {
                            isStop = true;
                        }
                    }
                } else {
                    isStop = true;
                    isAccept = true;
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_additive_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean maybeMultExp(Scope scope) {
        // : unary_exp rest_of_maybe_mult_exp
        if (isTest) System.out.println(">>> Enter maybe_mult_exp");
        boolean isAccept = false;
        
        
        if (unaryExp(scope)) {
            isAccept = restOfMaybeMultExp(scope);
        }

        if (isTest) System.out.println("<<< Exit maybe_mult_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeMultExp(Scope scope) {
        // : { ( '*' | '/' | '%' ) unary_exp }  /* could be empty ! */
        if (isTest) System.out.println(">>> Enter rest_of_maybe_mult_exp");
        boolean isAccept = false;
        boolean isStop = false;
        
        while (!isStop) {
            if (currentToken.getType() == TokenType.MULTIPLY || currentToken.getType() == TokenType.DIVIDE ||
                currentToken.getType() == TokenType.MOD) {
                if (match(currentToken.getType())) {
                    if (!unaryExp(scope)) {
                        isStop = true;
                    }
                }
            } else {
                isStop = true;
                isAccept = true;
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_maybe_mult_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean unaryExp(Scope scope) {
        // : sign { sign } signed_unary_exp
        // | unsigned_unary_exp
        // | ( PP | MM ) Identifier [ '[' expression ']' ]
        if (isTest) System.out.println(">>> Enter unary_exp");
        boolean isAccept = false;
        
        if (sign()) {
            if (match(currentToken.getType())) {
                boolean isStop = false;

                while(!isStop) {
                    if (sign()) {
                        boolean nop = match(currentToken.getType());
                    } else {
                        isStop = true;
                    }
                }

                isAccept = signedUnaryExp(scope);
            }
        } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
            if (match(currentToken.getType())) {
                if (currentToken.getType() == TokenType.IDENTIFIER) {
                    Token identifier = currentToken;
                    
                    if (!checkDeclaration(identifier, false, scope)) return false;
                }

                if (match(TokenType.IDENTIFIER)){
                    if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                        if (match(TokenType.LEFT_BRACKET)) {
                            if (expression(scope)) {
                                isAccept = match(TokenType.RIGHT_BRACKET);
                            }
                        }
                    } else {
                        isAccept = true;
                    }
                }
            }
        } else {
            isAccept = unsignedUnaryExp(scope);
        }

        if (isTest) System.out.println("<<< Exit unary_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean signedUnaryExp(Scope scope) {
        // : Identifier [ '(' [ actual_parameter_list ] ')' 
        //                |
        //                '[' expression ']'
        //              ]
        // | Constant 
        // | '(' expression ')'
        if (isTest) System.out.println(">>> Enter signed_unary_exp");
        boolean isAccept = false;
        
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            Token identifier = currentToken;

            if (match(TokenType.IDENTIFIER)) {
                if (currentToken.getType() == TokenType.LEFT_PAREN) {
                    // Function
                    if (!checkDeclaration(identifier, true, scope)) return false;

                    if (match(TokenType.LEFT_PAREN)) {
                        if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                            isAccept = match(TokenType.RIGHT_PAREN);
                        } else {
                            ArrayList<String> list = new ArrayList<String>();  

                            if (actualParameterList(scope, list)) {
                                isAccept = match(TokenType.RIGHT_PAREN);
                            }
                        }
                    }
                } else if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                    // Array
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    if (match(TokenType.LEFT_BRACKET)) {
                        if (expression(scope)) {
                            isAccept = match(TokenType.RIGHT_BRACKET);
                        }
                    }
                } else {
                    // Variable
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    isAccept = true;
                }
            }
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            if (match(TokenType.LEFT_PAREN)) {
                if (expression(scope)) {
                    isAccept = match(TokenType.RIGHT_PAREN);
                }
            }
        } else {
            isAccept = constant(scope);
        }

        if (isTest) System.out.println("<<< Exit signed_unary_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean unsignedUnaryExp(Scope scope) {
        // : Identifier [ '(' [ actual_parameter_list ] ')' 
        //                |
        //                [ '[' expression ']' ] [ ( PP | MM ) ]
        //              ]
        // | Constant 
        // | '(' expression ')'
        if (isTest) System.out.println(">>> Enter unsigned_unary_exp");
        boolean isAccept = false;

        if (currentToken.getType() == TokenType.IDENTIFIER) {
            Token identifier = currentToken;

            if (match(TokenType.IDENTIFIER)) {
                if (currentToken.getType() == TokenType.LEFT_PAREN) {
                    // Function
                    if (!checkDeclaration(identifier, true, scope)) return false;

                    if (match(TokenType.LEFT_PAREN)) {
                        if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                            isAccept = match(TokenType.RIGHT_PAREN);
                        } else {
                            ArrayList<String> list = new ArrayList<String>();   

                            if (actualParameterList(scope, list)) {
                                isAccept = match(TokenType.RIGHT_PAREN);
                            }
                        }
                    }
                } else if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                    // Array
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    if (match(TokenType.LEFT_BRACKET)) {
                        if (expression(scope)) {
                            if(match(TokenType.RIGHT_BRACKET)) {
                                if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                                    isAccept = match(currentToken.getType());
                                } else {
                                    isAccept = true;
                                }
                            }
                        }
                    }
                } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                    // Variable
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    isAccept = match(currentToken.getType());
                } else {
                    // Variable
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    isAccept = true;
                }
            }
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            if (match(TokenType.LEFT_PAREN)) {
                if (expression(scope)) {
                    isAccept = match(TokenType.RIGHT_PAREN);
                }
            }
        } else {
            isAccept = constant(scope);
        }
        
        if (isTest) System.out.println("<<< Exit unsigned_unary_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean constant(Scope scope) {
        if (isTest) System.out.println(">>> Enter Constant");
        boolean isAccept = false;
        TokenType type = currentToken.getType();

        if (type == TokenType.CONSTANT_CHAR || type == TokenType.CONSTANT_FALSE || type == TokenType.CONSTANT_FLOAT ||
            type == TokenType.CONSTANT_INT || type == TokenType.CONSTANT_STRING || type == TokenType.CONSTANT_TRUE) {
            isAccept = match(type);
        } else {
            isAccept = match(TokenType.CONSTANT_CHAR); // Select any above token type is OK.
        }

        if (isTest) System.out.println("<<< Exit Constant. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean isConstant(Scope scope) {
        if (isTest) System.out.println(">>> Enter Constant");
        boolean isAccept = false;
        TokenType type = currentToken.getType();

        if (type == TokenType.CONSTANT_CHAR || type == TokenType.CONSTANT_FALSE || type == TokenType.CONSTANT_FLOAT ||
            type == TokenType.CONSTANT_INT || type == TokenType.CONSTANT_STRING || type == TokenType.CONSTANT_TRUE) {
            isAccept = true;
        }

        if (isTest) System.out.println("<<< Exit Constant. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean sample(Scope scope) {
        if (isTest) System.out.println(">>> Enter sample");
        boolean isAccept = false;
        // TODO handle
        if (isTest) System.out.println("<<< Exit sample. isAccept = " + isAccept);

        return isAccept;
    }
}

public class Main {
    static final boolean TokenizerDEBUG = false;
    static final boolean ParserDEBUG = true;
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
            parser.userInput();
        }
        
    }
}