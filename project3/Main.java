// javac -encoding utf-8 Main.java
// java Main

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Base class for AST nodes
abstract class ASTNode {
    public abstract void execute();
}

// UserInput class that extends ASTNode but does nothing
class UserInput extends ASTNode {
    private ASTNode associatedNode;

    public UserInput() {
        this.associatedNode = null;
    }

    public ASTNode getAssociatedNode() {
        return associatedNode;
    }

    public void setAssociatedNode(ASTNode associatedNode) {
        this.associatedNode = associatedNode;
    }

    @Override
    public void execute() {
        if (associatedNode != null) associatedNode.execute();
    }
}

// Base class for Expression
abstract class ExpressionNode extends ASTNode {
    public abstract Object getOutput();
}

// Expression class that extends statement
class Expression extends ExpressionNode {
    private List<BasicExpression> basicExpressions;
    private Object output;

    public Expression() {
        this.basicExpressions = null;
        this.output = null;
    }

    public Expression(List<BasicExpression> basicExpressions) {
        this.basicExpressions = basicExpressions;
        this.output = null;
    }

    public List<BasicExpression> getBasicExpressions() {
        return basicExpressions;
    }

    public void setBasicExpressions(List<BasicExpression> basicExpressions) {
        this.basicExpressions = basicExpressions;
    }

    private void calculate() {
        if (basicExpressions != null && !basicExpressions.isEmpty()) {
            // Iterate over the list of basicExpressions
            for (BasicExpression basicExpr : basicExpressions) {
                this.output = basicExpr.getOutput();
            }
        } else {
            System.out.println(">>> You maybe have an error in your AST <expr>. <<<");
        }
    }

    @Override
    public Object getOutput() {
        calculate();
        
        return output;
    }

    public void execute() {
        getOutput();
    }
}

// BasicExpression class that extends expressions
class BasicExpression extends ExpressionNode {
    private ExpressionNode associatedNode;
    private Object output;
    private Scope scope;
    private ExpressionEvaluator evaluator;


    public BasicExpression(Scope scope) {
        this.associatedNode = null;
        this.scope = scope;
        this.evaluator = new ExpressionEvaluator();
    }

    public BasicExpression(Scope scope, ExpressionNode associatedNode) {
        this.associatedNode = associatedNode;
        this.scope = scope;
        this.evaluator = new ExpressionEvaluator();
    }

    public ExpressionNode getAssociatedNode() {
        return associatedNode;
    }

    public void setAssociatedNode(ExpressionNode associatedNode) {
        this.associatedNode = associatedNode;
    }

    private void calculate() {
        if (associatedNode != null) {
            this.output = associatedNode.getOutput();
            
            // deal i++
            for (Map.Entry<String, String> entry : scope.getPostProcessingMapPP().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue(); // array index

                if (key != null && value == null) {
                    // variable
                    Variable variable = scope.getVariable(key);
                    Object temp = evaluator.convertVariable(variable.getValue(), variable.getType());
                    Object originalValue = evaluator.processPPMM(temp, TokenType.PP);
                    
                    variable.setValue(String.valueOf(originalValue));
                    scope.addOrUpdateVariable(variable);
                } else if (key != null && value != null) {
                    // array
                    Array array = scope.getArray(key);
                    Object temp = evaluator.convertVariable(array.getValueAtIndex(value), array.getType());
                    Object originalValue = evaluator.processPPMM(temp, TokenType.PP);
                    
                    array.addValue(String.valueOf(originalValue), value);
                    scope.addOrUpdateArray(array);
                } else {
                    System.out.println("May be have an error( i-- ) in <BasicExpression>");
                }
            }

            scope.clearPostProcessingMapPP();

            // deal i--
            for (Map.Entry<String, String> entry : scope.getPostProcessingMapMM().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue(); // array index
                
                if (key != null && value == null) {
                    // variable
                    Variable variable = scope.getVariable(key);
                    Object temp = evaluator.convertVariable(variable.getValue(), variable.getType());
                    Object originalValue = evaluator.processPPMM(temp, TokenType.MM);
                    
                    variable.setValue(String.valueOf(originalValue));
                    scope.addOrUpdateVariable(variable);
                } else if (key != null && value != null) {
                    // array
                    Array array = scope.getArray(key);
                    Object temp = evaluator.convertVariable(array.getValueAtIndex(value), array.getType());
                    Object originalValue = evaluator.processPPMM(temp, TokenType.MM);
                    
                    array.addValue(String.valueOf(originalValue), value);
                    scope.addOrUpdateArray(array);
                } else {
                    System.out.println("May be have an error( i-- ) in <BasicExpression>");
                }
            }

            scope.clearPostProcessingMapMM();
        } else {
            // System.out.println(">>> You maybe have an error in your AST <basic_expr>. <<<");
        }
    }

    @Override
    public Object getOutput() {
        calculate();
        
        return output;
    }

    public void execute() {
        // Do nothing
    }
}

// TernaryExpression handles ternary expressions (e.g., a ? b : c)
class TernaryExpression extends ExpressionNode {
    private ExpressionNode condition;
    private BasicExpression trueExpression;
    private BasicExpression falseExpression;
    private Object output;

    public TernaryExpression() {
        this.condition = null;
        this.trueExpression = null;
        this.falseExpression = null;
    }

    public TernaryExpression(ExpressionNode condition, BasicExpression trueExpression, BasicExpression falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    // Getter for condition
    public ExpressionNode getCondition() {
        return condition;
    }

    // Setter for condition
    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    // Getter for trueExpression
    public BasicExpression getTrueExpression() {
        return trueExpression;
    }

    // Setter for trueExpression
    public void setTrueExpression(BasicExpression trueExpression) {
        this.trueExpression = trueExpression;
    }

    // Getter for falseExpression
    public BasicExpression getFalseExpression() {
        return falseExpression;
    }

    // Setter for falseExpression
    public void setFalseExpression(BasicExpression falseExpression) {
        this.falseExpression = falseExpression;
    }

    private void calculate() {
        if ((boolean) condition.getOutput()) {
            output = trueExpression.getOutput();
        } else {
            output = falseExpression.getOutput();
        }
    }

    @Override
    public Object getOutput() {
        calculate();
        
        return output;
    }

    public void execute() {
        
    }
}

// BinaryExpression handles expressions with operators (e.g., a + b)
class BinaryExpression extends ExpressionNode {
    private ExpressionNode left;
    private ExpressionNode right;
    private TokenType operator;
    private Object output;
    private ExpressionEvaluator evaluator;

    public BinaryExpression() {
        this.left = null;
        this.right = null;
        this.operator = null;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    public BinaryExpression(ExpressionNode left, ExpressionNode right, TokenType operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    // Getter for left operand
    public ExpressionNode getLeft() {
        return left;
    }

    // Setter for left operand
    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    // Getter for right operand
    public ExpressionNode getRight() {
        return right;
    }

    // Setter for right operand
    public void setRight(ExpressionNode right) {
        this.right = right;
    }

    // Getter for operator
    public TokenType getOperator() {
        return operator;
    }

    // Setter for operator
    public void setOperator(TokenType operator) {
        this.operator = operator;
    }

    private void calculate() {
        Object leftValue = left.getOutput();
        Object rightValue = right.getOutput();
    
        if (leftValue == null || rightValue == null) {
            throw new IllegalArgumentException("Operands cannot be null.");
        }

        output = evaluator.handleOperation(operator, leftValue, rightValue);
    }    

    // Getter for output
    public Object getOutput() {
        calculate();
        
        return output;
    }

    @Override
    public void execute() {
        // Implementation for execution logic goes here
    }
}

// VariableExpression handles variable expressions and array accesses
class TerminalNode extends ExpressionNode {
    private ExpressionNode associatedNode;
    private Object output;

    public TerminalNode() {
        this.associatedNode = null;
        this.output = null;
    }

    public ExpressionNode getAssociatedNode() {
        return associatedNode;
    }

    public void setAssociatedNode(ExpressionNode associatedNode) {
        this.associatedNode = associatedNode;
    }

    private void calculate() {
        if (associatedNode != null) {
            this.output = associatedNode.getOutput();
        } else {
            System.out.println(">>> You maybe have an error in your AST <term>. <<<");
        }
    }

    @Override    
    public Object getOutput() {
        calculate();
        
        return output;
    }

    public void execute() {
        
    }
}

// LiteralExpression for constant values
class LiteralExpression extends ExpressionNode {
    private Object value;
    private Object output;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    public void calculate() {
        this.output = this.value;
    }

    public Object getOutput() {
        calculate();
        
        return output;
    }

    @Override
    public void execute() {
        
    }
}

// VariableExpression handles variable expressions and array accesses
class VariableExpression extends ExpressionNode {
    private Variable variable;
    private Object output;
    private ExpressionEvaluator evaluator;

    public VariableExpression(Scope scope, String variableName) {
        this.variable = scope.getVariable(variableName);
        this.evaluator = new ExpressionEvaluator();
    }

    public Variable getVariable() {
        return variable;
    }

    private void calculate() {
        this.output = evaluator.convertVariable(variable.getValue(), variable.getType());
    }

    public Object getOutput() {
        calculate();
        
        return output;
    }

    @Override
    public void execute() {
        
    }
}

// ArrayExpression handles array expressions and array accesses
class ArrayExpression extends ExpressionNode {
    private Array array;
    private Expression expression;
    private ExpressionEvaluator evaluator;
    private Object output;

    public ArrayExpression(Scope scope, String arrayName, Expression expression) {
        this.array = scope.getArray(arrayName);
        this.expression = expression;
        this.evaluator = new ExpressionEvaluator();
    }

    public Array getArray() {
        return array;
    }

    private void calculate() {
        Object index = expression.getOutput();
        String value = array.getValueAtIndex(String.valueOf(index));
        output = evaluator.convertVariable(value, array.getType());
    }

    public Object getOutput() {
        calculate();

        return output;
    }

    @Override
    public void execute() {
        
    }
}

// UnaryExpression handles expressions with a single operand (e.g., -a, +a)
class UnaryExpression extends ExpressionNode {
    private TokenType operator;
    private ExpressionNode operand;
    private Object output;
    private ExpressionEvaluator evaluator;

    public UnaryExpression() {
        this.operator = null;
        this.operand = null;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    public UnaryExpression(TokenType operator, ExpressionNode operand) {
        this.operator = operator;
        this.operand = operand;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    public void setOperator(TokenType operator) {
        this.operator = operator;
    }

    public void setOperand(ExpressionNode operand) {
        this.operand = operand;
    }

    private void calculate() {
        if (operator == TokenType.PLUS) {
            output = operand.getOutput();
        } else {
            output = evaluator.reverse(operand.getOutput());
        }
    }

    @Override
    public Object getOutput() {
        calculate();

        return output;
    }

    public void execute() {
        
    }
}
// ProcessorPPMM handles expressions with a single operand (e.g., --a, a++)
class PPMM_Expression extends Expression {
    private Variable variable;
    private Array array;
    private Expression expression;
    private TokenType operatorFront;
    private TokenType operatorBehind;
    private Scope scope;
    private Object output;
    private ExpressionEvaluator evaluator;

    public PPMM_Expression(Scope scope) {
        this.variable = null;
        this.array = null;
        this.expression = null;
        this.operatorFront = null;
        this.operatorBehind = null;
        this.scope = scope;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    // Variable
    public PPMM_Expression(Scope scope, Variable variable, TokenType operatorBehind) { // i++
        this.variable = variable;
        this.array = null;
        this.expression = null;
        this.operatorFront = null;
        this.operatorBehind = operatorBehind;
        this.scope = scope;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    public PPMM_Expression(Scope scope, TokenType operatorFront, Variable variable) { // ++i
        this.variable = variable;
        this.array = null;
        this.expression = null;
        this.operatorFront = operatorFront;
        this.operatorBehind = null;
        this.scope = scope;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    // Array
    public PPMM_Expression(Scope scope, Array array, Expression expression, TokenType operatorBehind) { // i++
        this.variable = null;
        this.array = array;
        this.expression = expression;
        this.operatorFront = null;
        this.operatorBehind = operatorBehind;
        this.scope = scope;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    public PPMM_Expression(Scope scope, TokenType operatorFront, Array array, Expression expression) { // ++i
        this.variable = null;
        this.array = array;
        this.expression = expression;
        this.operatorFront = operatorFront;
        this.operatorBehind = null;
        this.scope = scope;
        this.output = null;
        this.evaluator = new ExpressionEvaluator();
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
    
    public void setArray(Array array) {
        this.array = array;
    }
    
    public void setExpression(Expression expression) {
        this.expression = expression;
    }    

    public void setOperatorBehind(TokenType operatorBehind) {
        this.operatorBehind = operatorBehind;
    }

    public void setOperatorFront(TokenType operatorFront) {
        this.operatorFront = operatorFront;
    }

    private void calculate() {
        if (operatorFront != null) { // ++i
            if (variable != null) {
                Object temp = evaluator.convertVariable(variable.getValue(), variable.getType());
                output = evaluator.processPPMM(temp, operatorFront);

                variable.setValue(String.valueOf(output));
                scope.addOrUpdateVariable(variable);
            } else if (array != null) {
                String index = String.valueOf(expression.getOutput());
                Object temp = evaluator.convertVariable(array.getValueAtIndex(index), array.getType());
                output = evaluator.processPPMM(temp, operatorFront);

                array.addValue(String.valueOf(output), index);
                scope.addOrUpdateArray(array);
            } else {
                System.out.println("You must have an error (no operand) in the PPMM_Expression");
            }
        } else if (operatorBehind != null) { // i++
            if (variable != null) {
                output = evaluator.convertVariable(variable.getValue(), variable.getType());

                if (operatorBehind == TokenType.PP) {
                    scope.addToPostProcessingMapPP(variable.getName(), null);
                } else {
                    scope.addToPostProcessingMapMM(variable.getName(), null);
                }
            } else if (array != null) {
                String index = String.valueOf(expression.getOutput());
                output = evaluator.convertVariable(array.getValueAtIndex(index), array.getType());

                if (operatorBehind == TokenType.PP) {
                    scope.addToPostProcessingMapPP(array.getName(), index);
                } else {
                    scope.addToPostProcessingMapMM(array.getName(), index);
                }
            } else {
                System.out.println("You must have an error (no operand) in the PPMM_Expression");
            }
        } else {
            System.out.println("You must have an error (no operator) in the PPMM_Expression");
        }
    }

    @Override
    public Object getOutput() {
        calculate();
        
        return output;
    }

    public void execute() {
        
    }
}

// AssignmentExpression for assignment operations (e.g., a = b)
class AssignmentExpression extends ExpressionNode {
    private Variable variable;
    private Array array;
    private Expression expression;
    private BasicExpression value;
    private TokenType operator;
    private Object output;
    private Scope scope;
    private ExpressionEvaluator evaluator;

    public AssignmentExpression(Scope scope) {
        this.variable = null;
        this.array = null;
        this.value = null;
        this.operator = null;
        this.scope = scope;
        this.evaluator = new ExpressionEvaluator();
    }

    public AssignmentExpression(Variable variable, BasicExpression value, TokenType operator, Scope scope) {
        this.variable = variable;
        this.array = null;
        this.expression = null;
        this.value = value;
        this.operator = operator;
        this.scope = scope;
        this.evaluator = new ExpressionEvaluator();
    }

    public AssignmentExpression(Array array, Expression expression, BasicExpression value, TokenType operator, Scope scope) {
        this.variable = null;
        this.array = array;
        this.expression = expression;
        this.value = value;
        this.operator = operator;
        this.scope = scope;
        this.evaluator = new ExpressionEvaluator();
    }

    private void calculate() {
        Object leftValue;
        Object rightValue = value.getOutput();
        TokenType leftValueType = TokenType.NOP;
        String index = "";

        if (expression == null) {
            // variable
            leftValue = evaluator.convertVariable(variable.getValue(), variable.getType());
            leftValueType = variable.getType();
        } else {
            // array
            index = String.valueOf(expression.getOutput());
            String value = array.getValueAtIndex(String.valueOf(index));
            leftValue = evaluator.convertVariable(value, array.getType());
            leftValueType = array.getType();
        }

        switch (operator) {
            case ASSIGNMENT: // =
                output = rightValue;
                break;
        
            case PE: // +=
                output = evaluator.handleOperation(TokenType.PLUS, leftValue, rightValue);
                break;

            case ME: // -=
                output = evaluator.handleOperation(TokenType.MINUS, leftValue, rightValue);
                break;

            case TE: // *=
                output = evaluator.handleOperation(TokenType.MULTIPLY, leftValue, rightValue);
                break;

            case DE: // /=
                output = evaluator.handleOperation(TokenType.DIVIDE, leftValue, rightValue);
                break;

            case RE: // %=
                output = evaluator.handleOperation(TokenType.MOD, leftValue, rightValue);
                break;

            default:
                System.out.println("You may have an operator error in AssignmentExpression");
                break;
        }

        // cast type to left value type
        output = evaluator.typeCast(output, leftValueType);

        if (expression == null) {
            // variable store
            variable.setValue(String.valueOf(output));
            scope.addOrUpdateVariable(variable);
        } else {
            // array store
            array.addValue(String.valueOf(output), index);
            scope.addOrUpdateArray(array);
        }

    }

    @Override
    public Object getOutput() {
        calculate();

        return output;
    }
    
    public void execute() {
        
    }
}

// New class for handling `cout <<` expressions
class CoutExpression extends ExpressionNode {
    private List<BasicExpression> basicExprList;
    private Object output;

    public CoutExpression(List<BasicExpression> basicExprList) {
        this.basicExprList = basicExprList;
        this.output = null;
    }

    private void print() {
        if (output instanceof Float) {
            // Format the float to 3 decimal places
            String str = String.format("%.3f", output);
            System.out.print(str);
        } else if (output instanceof String) {
            // Handle escape sequences in the string
            String str = (String) output;
    
            // Replace the escape sequences with their corresponding characters
            str = str.replace("\\n", "\n")
                     .replace("\\t", "\t")
                     .replace("\\r", "\r")
                     .replace("\\f", "\f")
                     .replace("\\b", "\b")
                     .replace("\\\"", "\"")
                     .replace("\\'", "'")
                     .replace("\\\\", "\\");
    
            System.out.print(str);
        } else {
            // For other types, just print the output directly
            System.out.print(output);
        }
    }

    private void calculate() {
        for (BasicExpression basicExpr : basicExprList) {
            this.output = basicExpr.getOutput();
            print();
        }
    }

    @Override
    public Object getOutput() {
        calculate();

        return output;
    }

    public void execute() {
        
    }
}

/* 
 ! project 3 don't need to complete the function call .
 ! Just pass.
// FunctionCall class that extends expressions
class FunctionCall extends ExpressionNode {
    private ExpressionNode associatedNode;
    private ActualParameterList parameterList;
    private Object output;
    private Scope scope;
    private boolean isAccept;
    // Maybe I will do this ?

    public FunctionCall(Scope scope) {
        this.associatedNode = null;
        this.parameterList = null;
        this.scope = scope;
        this.isAccept = false;
    }

    public ExpressionNode getAssociatedNode() {
        return associatedNode;
    }

    public void setAssociatedNode(ExpressionNode associatedNode) {
        this.associatedNode = associatedNode;
    }

    public boolean getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(boolean isAccept) {
        this.isAccept = isAccept;
    }

    @Override
    public Object getOutput() {
        return output;
    }

    public void execute() {
        // Do nothing
    }
}

// ActualParameterList class that extends ExpressionNode
class ActualParameterList extends ExpressionNode {
    private List<BasicExpression> basicExpressions;
    private Object output;
    // Maybe I will do this ?

    public ActualParameterList(List<BasicExpression> basicExpressions) {
        this.basicExpressions = basicExpressions;
    }

    public List<BasicExpression> getBasicExpressions() {
        return basicExpressions;
    }


    @Override
    public Object getOutput() {
        return output;
    }

    public void execute() {
        // Do nothing
    }
}
  */

// Base class for Statements
abstract class StatementNode extends ASTNode {
    // Use scope for statement-related operations
}

// Statement class that extends StatementNode
class Statement extends StatementNode {
    private ASTNode associatedNode;

    public Statement() {
        this.associatedNode = null;
    }

    public ASTNode getAssociatedNode() {
        return associatedNode;
    }

    public void setAssociatedNode(ASTNode associatedNode) {
        this.associatedNode = associatedNode;
    }


    @Override
    public void execute() {
        if (associatedNode != null) associatedNode.execute();
    }
}

// Class for if statements
class IfStatement extends StatementNode {
    private Expression condition;
    private Statement thenBranch;
    private Statement elseBranch;

    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getThenBranch() {
        return thenBranch;
    }

    public Statement getElseBranch() {
        return elseBranch;
    }

    @Override
    public void execute() {
        if ((boolean) condition.getOutput()) {
            if (thenBranch != null) thenBranch.execute();
        } else {
            if (elseBranch != null) elseBranch.execute();
        }
    }
}

// Class for while statements
class WhileStatement extends StatementNode {
    private Expression condition;
    private Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public void execute() {
        while ((boolean) condition.getOutput()) {
            if (body != null) body.execute();
        }
    }
}

// Class for do while statements
class DoWhileStatement extends StatementNode {
    private Expression condition;
    private Statement body;

    public DoWhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public void execute() {
        do {
            if (body != null) body.execute();
        } while ((boolean) condition.getOutput());
    }
}

// Class for return statements
class ReturnStatement extends StatementNode {
    private Expression returnValue;
    private Object output;

    public ReturnStatement(Expression returnValue) {
        this.returnValue = returnValue;
    }

    public Expression getReturnValue() {
        return returnValue;
    }

    public Object getOutput() {
        return output;
    }

    @Override
    public void execute() {
        returnValue.getOutput();
    }
}

// Class for compound statements
class CompoundStatement extends StatementNode {
    private List<StatementNode> statements;
    private boolean isAccept;

    public CompoundStatement() {
        this.statements = null;
    }

    public CompoundStatement(List<StatementNode> statements) {
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public void setStatements(List<StatementNode> statements) {
        this.statements = statements;
    }

    public boolean getIsAccept() {
        return isAccept;
    }

    @Override
    public void execute() {
        for (StatementNode stmt : statements) {
            if (stmt != null) stmt.execute();
        }
    }
}

/* 
 ! project 3 don't need to complete the function call .
 ! Just pass.
// Class for function declarations
class FunctionDeclaration extends StatementNode {
    private String returnType;
    private String functionName;
    private List<Parameter> parameters;
    private CompoundStatement body;
    // Maybe I will do this ?

    public FunctionDeclaration(String returnType, String functionName, List<Parameter> parameters, CompoundStatement body) {
        this.returnType = returnType;
        this.functionName = functionName;
        this.parameters = parameters;
        this.body = body;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public CompoundStatement getBody() {
        return body;
    }

    @Override
    public void execute() {
        
    }
}

// Class for parameters
class Parameter extends ASTNode {
    private String type;
    private String name;

    // Maybe I will do this ?

    public Parameter(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public void execute() {
        
    }
}
  */
class ExpressionEvaluator {

    public Object handleOperation(TokenType operator, Object leftValue, Object rightValue) {
        switch (operator) {
            case PLUS:
                return add(leftValue, rightValue);
            case MINUS:
                return subtract(leftValue, rightValue);
            case MULTIPLY:
                return multiply(leftValue, rightValue);
            case DIVIDE:
                return divide(leftValue, rightValue);
            case MOD:
                return mod(leftValue, rightValue);
            case EQ:
                return equals(leftValue, rightValue);
            case NEQ:
                return notEquals(leftValue, rightValue);
            case GREATER_THAN:
                return greaterThan(leftValue, rightValue);
            case LESS_THAN:
                return lessThan(leftValue, rightValue);
            case GE:
                return greaterOrEqual(leftValue, rightValue);
            case LE:
                return lessOrEqual(leftValue, rightValue);
            case AND:
                return and(leftValue, rightValue);
            case OR:
                return or(leftValue, rightValue);
            case LS:
                return leftShift(leftValue, rightValue);
            case RS:
                return rightShift(leftValue, rightValue);
            default:
                throw new UnsupportedOperationException("Unsupported operator: " + operator);
        }
    }

    // Method to convert variable based on TokenType
    // * Only can use when you must know what type of variable in the string.
    // * tokenType is value's type.
    public Object convertVariable(String value, TokenType tokenType) { // TODO
        switch (tokenType) {
            case BOOL:
                return Boolean.parseBoolean(value);
            case INT:
                return Integer.parseInt(value);
            case FLOAT:
                return Float.parseFloat(value);
            case CHAR:
                if (value == null || value.isEmpty()) {
                    return "";
                }

                return parseChar(value);
            default:
                return value;
        }
    }

    // tokenType is what value needs to be cast to
    public Object typeCast(Object value, TokenType tokenType) {
        switch (tokenType) {
            case BOOL:
                if (value instanceof Boolean) {
                    return (Boolean) value;
                } else {
                    throw new IllegalArgumentException("Bool can not cast: " + value);
                }

            case INT:
                if (value instanceof Integer) {
                    return (Integer) value;
                } else if (value instanceof Float) {
                    return (Integer) ((Float) value).intValue();
                } else if (value instanceof Character) {
                    return (int) (char) value;
                } else {
                    throw new IllegalArgumentException("Int can not cast: " + value);
                }

            case FLOAT:
                if (value instanceof Float) {
                    return (Float) value;
                } else if (value instanceof Integer) {
                    return (Float) ((Integer) value).floatValue();
                } else if (value instanceof Character) {
                    return (float) (char) value;
                } else {
                    throw new IllegalArgumentException("Float can not cast: " + value);
                }

            case CHAR:
                if (value instanceof Character) {
                    return (Character) value;
                } else {
                    throw new IllegalArgumentException("Char can not cast: " + value);
                }

            case STRING:
                if (value instanceof String) {
                    return (String) value;
                } else if ((value instanceof Character)) {
                    return String.valueOf(value);
                } else {
                    throw new IllegalArgumentException("String can not cast: " + value);
                }

            default:
                throw new UnsupportedOperationException("Unsupported tokenType: " + tokenType);
        }        
    }

    public TokenType determineTokenType(Object value) {
        if (value instanceof Boolean) {
            return TokenType.BOOL;
        } else if (value instanceof Integer) {
            return TokenType.INT;
        } else if (value instanceof Float) {
            return TokenType.FLOAT;
        } else if (value instanceof Character) {
            return TokenType.CHAR;
        } else if (value instanceof String) {
            return TokenType.STRING;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + value.getClass().getSimpleName());
        }
    }

    public Object reverse(Object value) {
        if (value instanceof Boolean) {
            return !(Boolean) value;
        } else if (value instanceof Integer) {
            return -(Integer) value;
        } else if (value instanceof Float) {
            return -(Float) value;
        } else {
            throw new IllegalArgumentException("Type can not be negated: " + value.getClass().getSimpleName());
        }
    }    

    public Object processPPMM(Object value, TokenType operator) {
        if (value instanceof Integer) {
            if (operator == TokenType.PP) {
                return (Integer) value + 1;
            } else if (operator == TokenType.MM) {
                return (Integer) value - 1;
            } else {
                throw new IllegalArgumentException("Invalid operator");
            }
        } else if (value instanceof Float) {
            if (operator == TokenType.PP) {
                return (Float) value + 1;
            } else if (operator == TokenType.MM) {
                return (Float) value - 1;
            } else {
                throw new IllegalArgumentException("Invalid operator");
            }
        } else {
            throw new IllegalArgumentException("Type is not Integer or Float");
        }
    }    

    // %, &, | , ^, <<, >> must be integer
    private Object add(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            if (leftValue instanceof Float || rightValue instanceof Float) {
                return ((Number) leftValue).floatValue() + ((Number) rightValue).floatValue();
            } else {
                return ((Number) leftValue).intValue() + ((Number) rightValue).intValue();
            }
        } else if (leftValue instanceof String || rightValue instanceof String) {
            return leftValue.toString() + rightValue.toString();
        } else {
            throw new IllegalArgumentException("Cannot perform addition on two Boolean values, two Char values, etc.");
        }
    }

    private Object subtract(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            if (leftValue instanceof Float || rightValue instanceof Float) {
                return ((Number) leftValue).floatValue() - ((Number) rightValue).floatValue();
            } else {
                return ((Number) leftValue).intValue() - ((Number) rightValue).intValue();
            }
        } else if (leftValue instanceof String || rightValue instanceof String) {
            return leftValue.toString() + rightValue.toString();
        } else {
            throw new IllegalArgumentException("Cannot perform subtract on two Boolean values, two Char values, etc.");
        }
    }

    private Object multiply(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            if (leftValue instanceof Float || rightValue instanceof Float) {
                return ((Number) leftValue).floatValue() * ((Number) rightValue).floatValue();
            } else {
                return ((Number) leftValue).intValue() * ((Number) rightValue).intValue();
            }
        } else if (leftValue instanceof String || rightValue instanceof String) {
            return leftValue.toString() + rightValue.toString();
        } else {
            throw new IllegalArgumentException("Cannot perform subtract on two Boolean values, two Char values, etc.");
        }
    }

    private Object divide(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            if (leftValue instanceof Float || rightValue instanceof Float) {
                return ((Number) leftValue).floatValue() / ((Number) rightValue).floatValue();
            } else {
                return ((Number) leftValue).intValue() / ((Number) rightValue).intValue();
            }
        } else if (leftValue instanceof String || rightValue instanceof String) {
            return leftValue.toString() + rightValue.toString();
        } else {
            throw new IllegalArgumentException("Cannot perform subtract on two Boolean values, two Char values, etc.");
        }
    }

    private Object mod(Object leftValue, Object rightValue) {
        if (leftValue instanceof Integer && rightValue instanceof Integer) {
            int result = ((Integer) leftValue).intValue() % ((Integer) rightValue).intValue();
            return result;
        } else {
            throw new IllegalArgumentException("Unsupported types for MOD operator.");
        }
    }

    private Object equals(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            // cast to double is more precise
            return ((Number) leftValue).doubleValue() == ((Number) rightValue).doubleValue();
        } else if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            return leftValue == rightValue;
        } else {
            throw new IllegalArgumentException("Unsupported types for EQ operator.");
        }
    }

    private Object notEquals(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            // cast to double is more precise
            return ((Number) leftValue).doubleValue() != ((Number) rightValue).doubleValue();
        } else if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            return leftValue != rightValue;
        } else {
            throw new IllegalArgumentException("Unsupported types for NEQ operator.");
        }
    }

    private Object greaterThan(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            // cast to double is more precise
            return ((Number) leftValue).doubleValue() > ((Number) rightValue).doubleValue();
        } else {
            throw new IllegalArgumentException("Unsupported types for GREATER_THAN operator.");
        }
    }

    private Object lessThan(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            // cast to double is more precise
            return ((Number) leftValue).doubleValue() < ((Number) rightValue).doubleValue();
        } else {
            throw new IllegalArgumentException("Unsupported types for LESS_THAN operator.");
        }
    }

    private Object greaterOrEqual(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            // cast to double is more precise
            return ((Number) leftValue).doubleValue() >= ((Number) rightValue).doubleValue();
        } else {
            throw new IllegalArgumentException("Unsupported types for GE operator.");
        }
    }

    private Object lessOrEqual(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            // cast to double is more precise
            return ((Number) leftValue).doubleValue() >= ((Number) rightValue).doubleValue();
        } else {
            throw new IllegalArgumentException("Unsupported types for LE operator.");
        }
    }

    private Object and(Object leftValue, Object rightValue) {
        if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            return (Boolean) leftValue && (Boolean) rightValue;
        } else {
            throw new IllegalArgumentException("Unsupported types for AND operator.");
        }
    }

    private Object or(Object leftValue, Object rightValue) {
        if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            return (Boolean) leftValue || (Boolean) rightValue;
        } else {
            throw new IllegalArgumentException("Unsupported types for OR operator.");
        }
    }

    private Object leftShift(Object leftValue, Object rightValue) {
        if (leftValue instanceof Integer && rightValue instanceof Integer) {
            return (Integer) leftValue << (Integer) rightValue;
        } else {
            throw new IllegalArgumentException("Unsupported types for LS operator.");
        }
    }

    private Object rightShift(Object leftValue, Object rightValue) {
        if (leftValue instanceof Integer && rightValue instanceof Integer) {
            return (Integer) leftValue >> (Integer) rightValue;
        } else {
            throw new IllegalArgumentException("Unsupported types for RS operator.");
        }
    }

    // Helper method to handle CHAR conversion, including special characters
    private Character parseChar(String value) {
        // Handle special characters
        switch (value) {
            case "\\n":
                return '\n';
            case "\\t":
                return '\t';
            case "\\r":
                return '\r';
            case "\\b":
                return '\b';
            case "\\f":
                return '\f';
            case "\\\"":
                return '\"';
            case "\\\\":
                return '\\';
            default:
                if (value.length() == 1) {
                    return value.charAt(0);
                } else {
                    throw new IllegalArgumentException("Invalid character literal: " + value);
                }
        }
    }
}

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
    OBJECT,            // any type
    NOP                // do nothing
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
    private String operator;   // *(Pointer Operator) or &(Address Operator) ; can be empty

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

    // Method to retrieve a value based on the index
    public String getValueAtIndex(String index) {
        if (elements.containsKey(index)) {
            return elements.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds or does not exist in the array.");
        }
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
    private Map<String, String> postProcessingMapPP;
    private Map<String, String> postProcessingMapMM;
    private Scope parentScope;

    public Scope(Scope parentScope) {
        this.variables = new HashMap<String, Variable>();
        this.arrays = new HashMap<String, Array>();
        this.functions = new HashMap<String, Function>();
        this.predefineVariable = new HashMap<String, Variable>();
        this.predefineFunctions = new HashMap<String, Function>();
        this.postProcessingMapPP = new HashMap<String, String>();
        this.postProcessingMapMM = new HashMap<String, String>();
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

        if (variable == null) variable = predefineVariable.get(name);
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

    // Getter for postProcessingMapPP
    public Map<String, String> getPostProcessingMapPP() {
        return postProcessingMapPP;
    }

    // Setter for postProcessingMapPP
    public void setPostProcessingMapPP(Map<String, String> postProcessingMapPP) {
        this.postProcessingMapPP = postProcessingMapPP;
    }

    // Getter for postProcessingMapMM
    public Map<String, String> getPostProcessingMapMM() {
        return postProcessingMapMM;
    }

    // Setter for postProcessingMapMM
    public void setPostProcessingMapMM(Map<String, String> postProcessingMapMM) {
        this.postProcessingMapMM = postProcessingMapMM;
    }

    // Method to clear postProcessingMapPP
    public void clearPostProcessingMapPP() {
        postProcessingMapPP.clear();
    }

    // Method to clear postProcessingMapMM
    public void clearPostProcessingMapMM() {
        postProcessingMapMM.clear();
    }

    // Method to add content to postProcessingMapPP
    public void addToPostProcessingMapPP(String key, String value) {
        postProcessingMapPP.put(key, value);
    }

    // Method to add content to postProcessingMapMM
    public void addToPostProcessingMapMM(String key, String value) {
        postProcessingMapMM.put(key, value);
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
        if (variables.containsKey(name) || predefineVariable.containsKey(name)) {
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

    // Recursively deep copy the parentScope chain
    private Scope deepCopyParentScope(Scope sourceParent) {
        if (sourceParent == null) {
            return null;
        }

        Scope newParent = new Scope(null);
        newParent.variables = new HashMap<>(sourceParent.variables);
        newParent.arrays = new HashMap<>(sourceParent.arrays);
        newParent.functions = new HashMap<>(sourceParent.functions);
        newParent.predefineVariable = new HashMap<>(sourceParent.predefineVariable);
        newParent.predefineFunctions = new HashMap<>(sourceParent.predefineFunctions);

        // Recursively copy the parent scope
        newParent.parentScope = deepCopyParentScope(sourceParent.parentScope);
        return newParent;
    }

    // Deep copy the entire Scope object, including its parentScope chain
    public Scope deepCopy() {
        Scope newScope = new Scope(null);
        newScope.variables = new HashMap<>(this.variables);
        newScope.arrays = new HashMap<>(this.arrays);
        newScope.functions = new HashMap<>(this.functions);
        newScope.predefineVariable = new HashMap<>(this.predefineVariable);
        newScope.predefineFunctions = new HashMap<>(this.predefineFunctions);

        // Deep copy the parentScope chain
        newScope.parentScope = deepCopyParentScope(this.parentScope);
        return newScope;
    }

    // Update the target scope with the content of the sourceCopy scope
    private void updateScopeWithCopy(Scope target, Scope sourceCopy) {
        if (sourceCopy == null) {
            return;
        }

        // Update the current scope's content
        target.variables = sourceCopy.variables;
        target.arrays = sourceCopy.arrays;
        target.functions = sourceCopy.functions;
        target.predefineVariable = sourceCopy.predefineVariable;
        target.predefineFunctions = sourceCopy.predefineFunctions;

        // Recursively update the parentScope
        if (target.parentScope != null && sourceCopy.parentScope != null) {
            updateScopeWithCopy(target.parentScope, sourceCopy.parentScope);
        }
    }

    // Merge the content from subscopeCopy back into the original scope
    public void mergeBackFromCopy(Scope subscopeCopy) {
        updateScopeWithCopy(this, subscopeCopy);
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
    private boolean isCout;
    private Tokenizer tokenizer;
    private ErrorMessage errorMessage;
    private Token currentToken;
    private Token priorToken;
    private Scope globalScope;
    private UserInput root;
    

    public Parser() {
        this.isTest = false;                               // TODO: --TestControl -TC
        this.isDone = false;
        this.isPause = false;                              // Avoid undefined function error removal token (used for if statement)
        this.isCout = false;
        this.tokenizer = new Tokenizer();
        this.errorMessage = new ErrorMessage();
        this.currentToken = new Token();
        this.priorToken = new Token();
        this.globalScope = new Scope(null);
        this.root = new UserInput();
    }

    private boolean match(TokenType expectedType) {
        // match() don't consider undefine. If match() deal undefined symbols, logic will be error.
        if (currentToken.getType() == TokenType.ERROR || expectedType == TokenType.ERROR) {
            if (expectedType == TokenType.ERROR) errorMessage.syntacticalError(currentToken);
            resetAll();

            return false;
        } else if (currentToken.getType() == expectedType) {
            priorToken = currentToken;
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

            if (globalScope.containsArrayName(name) || globalScope.containsName(name) || 
                globalScope.containsPredefineVariableName(name)) {
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
            
            if (globalScope.containsArrayName(name) || globalScope.containsName(name) || 
                globalScope.containsPredefineVariableName(name)) {
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
            root = new UserInput();

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
                Statement stmt = new Statement();
                root.setAssociatedNode(stmt);

                if (statement(globalScope, stmt)) {
                    root.execute();
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
                        CompoundStatement compoundStmt = new CompoundStatement();

                        if (compoundStatement(functionScope, compoundStmt)) {
                            defineToken(new Function(varOrFunctName, varType, tokenizer.getTokenList(), functPar), scope);
                            tokenizer.resetTokenList();
                            isAccept = true;
                        }
                    }
                } 
            } else if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                if (match(TokenType.RIGHT_PAREN)) {
                    CompoundStatement compoundStmt = new CompoundStatement();

                    if (compoundStatement(functionScope, compoundStmt)) {
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
                            CompoundStatement compoundStmt = new CompoundStatement();
                            
                            if (compoundStatement(functionScope, compoundStmt)) {
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

    private boolean compoundStatement(Scope scope, CompoundStatement compoundStmt) {
        // '{' { declaration | statement } '}'
        if (isTest) System.out.println(">>> Enter compound_statement");
        boolean isAccept = false;
        boolean isStop = false;
        List<StatementNode> stmtList = new ArrayList<StatementNode>();
        
        if (currentToken.getType() == TokenType.LEFT_CURLY_BRACE) {
            while (!isStop) {
                if (isPause) {
                    isPause = false;
                } else {
                    currentToken = tokenizer.getNextToken();
                }

                if (currentToken.getType() == TokenType.RIGHT_CURLY_BRACE) {
                    compoundStmt.setStatements(stmtList);
                    isStop = true;
                    isAccept = true;
                } else if (typeSpecifier()) {
                    if (!declaration(scope)) {
                        isStop = true;
                    }
                } else {
                    Statement stmt = new Statement();

                    if (!statement(scope, stmt)) {
                        isStop = true;
                    }

                    stmtList.add(stmt);
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

    private boolean statement(Scope scope, Statement stmt) {
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
                    Expression expr = new Expression();

                    if (currentToken.getType() == TokenType.SEMICOLON) {
                            isAccept = true;
                    } else if (expression(scope, expr)) {
                        if (currentToken.getType() == TokenType.SEMICOLON) {
                                ReturnStatement returnStmt = new ReturnStatement(expr);
                                isAccept = true;

                                if (isAccept) stmt.setAssociatedNode(returnStmt);
                        } else {
                            errorMessage.syntacticalError(currentToken);
                        }
                    }
                }
                break;
    
            case LEFT_CURLY_BRACE:
                // compound_statement
                Scope subScope = scope.enterScope();
                CompoundStatement compoundStmt = new CompoundStatement();
                isAccept = compoundStatement(subScope, compoundStmt);
                
                if (isAccept) stmt.setAssociatedNode(compoundStmt);
                subScope.exitScope();

                break;
    
            case IF:
                // IF '(' expression ')' statement [ ELSE statement ]
                if (isTest) System.out.println(">>> >>> Enter IF statement. -----> in statement()");
                Scope subScopeIF = scope.enterScope();

                if (match(TokenType.IF)) {
                    if (match(TokenType.LEFT_PAREN)) {
                        Expression expr = new Expression();

                        if (expression(subScopeIF, expr)) {
                            if (match(TokenType.RIGHT_PAREN)) {
                                Statement stmtIF = new Statement();

                                if (statement(subScopeIF, stmtIF)) {
                                    subScopeIF.exitScope();
                                    currentToken = tokenizer.getNextToken();

                                    if (currentToken.getType() == TokenType.ELSE) {
                                        Scope subScopeELSE = scope.enterScope();

                                        if (match(TokenType.ELSE)) {
                                            Statement stmtELSE = new Statement();
                                            isAccept = statement(subScopeELSE, stmtELSE);
                                            IfStatement elseBranchStmt = new IfStatement(expr, stmtIF, stmtELSE);

                                            if (isAccept) stmt.setAssociatedNode(elseBranchStmt);
                                            subScopeELSE.exitScope();
                                        }
                                    } else {
                                        IfStatement thenBranchStmt = new IfStatement(expr, stmtIF, null);
                                        isPause = true;
                                        isAccept = true;

                                        if (isAccept) stmt.setAssociatedNode(thenBranchStmt);
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
                        Expression expr = new Expression();

                        if (expression(subScopeWHILE, expr)) {
                            if (match(TokenType.RIGHT_PAREN)) {
                                Statement stmtWHILE = new Statement();
                                isAccept = statement(subScopeWHILE, stmtWHILE);
                                WhileStatement whileStmt = new WhileStatement(expr, stmtWHILE);

                                if (isAccept) stmt.setAssociatedNode(whileStmt);
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
                    Statement stmtDO = new Statement();

                    if (statement(subScopeDO, stmtDO)) {
                        subScopeDO.exitScope();
                        
                        if (match(TokenType.WHILE)) {
                            Scope subScopeDoWHILE = scope.enterScope();

                            if (match(TokenType.LEFT_PAREN)) {
                                Expression expr = new Expression();

                                if (expression(subScopeDoWHILE, expr)) {
                                    subScopeDoWHILE.exitScope();

                                    if (match(TokenType.RIGHT_PAREN)) {
                                        if (currentToken.getType() == TokenType.SEMICOLON) {
                                            DoWhileStatement doWhileStmt = new DoWhileStatement(expr, stmtDO);
                                            isAccept = true;

                                            if (isAccept) stmt.setAssociatedNode(doWhileStmt);
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
                Expression expr = new Expression();

                if (expression(scope, expr)) {
                    if (currentToken.getType() == TokenType.SEMICOLON) {
                        isAccept = true;

                        if (isAccept) stmt.setAssociatedNode(expr);
                    } else {
                        errorMessage.syntacticalError(currentToken);
                    }
                }
                break;
        }
    
        if (isTest) System.out.println("<<< Exit statement. isAccept = " + isAccept);
        
        return isAccept;
    }

    private boolean expression(Scope scope, Expression expr) {
        // : basic_expression { ',' basic_expression }
        if (isTest) System.out.println(">>> Enter expression");
        boolean isAccept = false;
        List<BasicExpression> basicExprList = new ArrayList<BasicExpression>();
        BasicExpression basicExpr = new BasicExpression(scope);

        if (basicExpression(scope, basicExpr)) {
            basicExprList.add(basicExpr);

            if (currentToken.getType() == TokenType.COMMA) {
                boolean isStop = false;

                while (!isStop) {
                    if (match(TokenType.COMMA)) {
                        BasicExpression basicExprSub = new BasicExpression(scope);

                        if (basicExpression(scope, basicExprSub)) {
                            basicExprList.add(basicExprSub);

                            if (!(currentToken.getType() == TokenType.COMMA)) {
                                expr.setBasicExpressions(basicExprList);
                                isStop = true;
                                isAccept = true;
                            }
                        }
                    }
                }
            } else {
                expr.setBasicExpressions(basicExprList);
                isAccept = true;
            }
        }

        if (isTest) System.out.println("<<< Exit expression. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean basicExpression(Scope scope, BasicExpression basicExpr) {
        // : Identifier rest_of_Identifier_started_basic_exp
        // | ( PP | MM ) Identifier rest_of_PPMM_Identifier_started_basic_exp
        // | sign { sign } signed_unary_exp romce_and_romloe
        // | ( Constant | '(' expression ')' ) romce_and_romloe
        if (isTest) System.out.println(">>> Enter basic_expression");
        boolean isAccept = false;
        Token identifier = new Token();
        PPMM_Expression ppmm = new PPMM_Expression(scope);
        BinaryExpression binaryExpr = new BinaryExpression();
        TernaryExpression ternaryExpr = new TernaryExpression();
        
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            identifier = currentToken;

            if (match(TokenType.IDENTIFIER)) {
                isAccept = restzofIdentifierStartedBasicExp(scope, identifier, basicExpr);
            }
        } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
            ppmm.setOperatorFront(currentToken.getType());

            if (match(currentToken.getType())) {
                if (currentToken.getType() == TokenType.IDENTIFIER) {
                    identifier = currentToken;
                    
                    if (!checkDeclaration(identifier, false, scope)) return false;
                }
                
                if (match(TokenType.IDENTIFIER)) {
                    isAccept = restOfPPMMIdentifierStartedBasicExp(scope, identifier, ppmm, basicExpr);
                }
            }
        } else if (sign()) {
            ArrayList<TokenType> signs = new ArrayList<TokenType>();
            signs.add(currentToken.getType());

            if (match(currentToken.getType())) {
                boolean isStop = false;

                while (!isStop) {
                    if (sign()) {
                        signs.add(currentToken.getType());
                        boolean nop = match(currentToken.getType());
                    } else {
                        isStop = true;
                    }
                }

                TokenType operator = simplifySigns(signs);
                TerminalNode signedUnaryExpTerm = new TerminalNode();

                if (signedUnaryExp(scope, signedUnaryExpTerm)) {
                    UnaryExpression unaryExpr = new UnaryExpression(operator, signedUnaryExpTerm);
                    binaryExpr.setLeft(unaryExpr);
                    isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                    if (isAccept) {
                        if (ternaryExpr.getCondition() == null) {
                            if (binaryExpr.getRight() == null) {
                                basicExpr.setAssociatedNode(unaryExpr);
                            } else {
                                basicExpr.setAssociatedNode(binaryExpr);
                            }  
                        } else {
                            basicExpr.setAssociatedNode(ternaryExpr);
                        }
                    }
                }
            }
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            boolean isCoutMode = isCout;

            if (isCoutMode) isCout = false;
            if (match(TokenType.LEFT_PAREN)){
                Expression expr = new Expression();

                if (expression(scope, expr)) {
                    if (match(TokenType.RIGHT_PAREN)) {
                        if (isCoutMode) isCout = true;

                        binaryExpr.setLeft(expr);
                        isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                        if (isAccept) {
                            if (ternaryExpr.getCondition() == null) {
                                if (binaryExpr.getRight() == null) {
                                    basicExpr.setAssociatedNode(expr);
                                } else {
                                    basicExpr.setAssociatedNode(binaryExpr);
                                }  
                            } else {
                                basicExpr.setAssociatedNode(ternaryExpr);
                            }
                        }
                    }
                }
            }
        } else {
            Object value = tokenChangeToValue(currentToken);

            if (constant(scope)) {
                LiteralExpression literal = new LiteralExpression(value);
                binaryExpr.setLeft(literal);
                isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                if (isAccept) {
                    if (ternaryExpr.getCondition() == null) {
                        if (binaryExpr.getRight() == null) {
                            basicExpr.setAssociatedNode(literal);
                        } else {
                            basicExpr.setAssociatedNode(binaryExpr);
                        }    
                    } else {
                        basicExpr.setAssociatedNode(ternaryExpr);
                    }
                }
            }
        }

        if (isTest) System.out.println("<<< Exit basic_expression. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restzofIdentifierStartedBasicExp(Scope scope, Token identifier, BasicExpression basicExpr) {
        // : [ '[' expression ']' ]
        //       ( assignment_operator basic_expression             (Must be array)
        //       | 
        //       [ PP | MM ] romce_and_romloe                       (Must be variable)
        //     )
        //   | '(' [ actual_parameter_list ] ')' romce_and_romloe   (Must be function)
        if (isTest) System.out.println(">>> Enter rest_of_Identifier_started_basic_exp");
        boolean isAccept = false;
        BinaryExpression binaryExpr = new BinaryExpression();
        TernaryExpression ternaryExpr = new TernaryExpression();
        
        if (currentToken.getType() == TokenType.LEFT_PAREN) {
            // '(' [ actual_parameter_list ] ')' romce_and_romloe
            if (!checkDeclaration(identifier, true, scope)) return false;

            if (match(TokenType.LEFT_PAREN)) { // TODO: if do the function call need to fix from this line...
                // Function call
                if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                    if (match(TokenType.RIGHT_PAREN)) {
                        if (romceAndRomloe(scope, binaryExpr, ternaryExpr)) {
                            isAccept = true;

                            if (isAccept) {
                                if (ternaryExpr.getCondition() == null) {
                                    // basicExpr.setAssociatedNode(binaryExpr);
                                } else {
                                    // basicExpr.setAssociatedNode(ternaryExpr);
                                }
                            }

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
                            isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                            if (isAccept) {
                                if (ternaryExpr.getCondition() == null) {
                                    // basicExpr.setAssociatedNode(binaryExpr);
                                } else {
                                    // basicExpr.setAssociatedNode(ternaryExpr);
                                }
                            }

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
            }                                                // ...to this line
        } else if (identifier.getName().equals("cout") && currentToken.getType() == TokenType.LS) { // Added by me, maybe error.  
            List<BasicExpression> basicExprList = new ArrayList<BasicExpression>();                          // This grammar is for cout syntax.
            boolean isStop = false;

            while (!isStop) {
                isCout = true;
                
                if (currentToken.getType() == TokenType.LS) {
                    if (match(TokenType.LS)) {
                        BasicExpression basicExprSub = new BasicExpression(scope);

                        if (!basicExpression(scope, basicExprSub)) {
                            isStop = true;
                            isCout = false;
                        }

                        basicExprList.add(basicExprSub);

                        if (currentToken.getType() == TokenType.NOP) {
                            currentToken.setType(TokenType.LS);
                        }
                    } else {
                        isCout = false;
                        isStop = true;
                    }
                } else {
                    isCout = false;
                    isStop = true;
                    isAccept = true;
                }
            }

            if (isAccept) {
                CoutExpression coutExpr = new CoutExpression(basicExprList);
                basicExpr.setAssociatedNode(coutExpr);
            }
        } else {
            if (!checkDeclaration(identifier, false, scope)) return false;

            if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                // Array
                if (match(TokenType.LEFT_BRACKET)) {
                    Expression expr = new Expression();

                    if (expression(scope, expr)) {
                        if (match(TokenType.RIGHT_BRACKET)) {
                            ArrayExpression arrayExp = new ArrayExpression(scope, identifier.getName(), expr);

                            if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                                TokenType operator = currentToken.getType();
                                PPMM_Expression ppmm = new PPMM_Expression(scope, scope.getArray(identifier.getName()), expr, operator);

                                if (match(currentToken.getType())) {
                                    binaryExpr.setLeft(ppmm);
                                    isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                                    if (isAccept) {
                                        if (ternaryExpr.getCondition() == null) {
                                            if (binaryExpr.getRight() == null) {
                                                basicExpr.setAssociatedNode(ppmm);
                                            } else {
                                                basicExpr.setAssociatedNode(binaryExpr);
                                            } 
                                        } else {
                                            basicExpr.setAssociatedNode(ternaryExpr);
                                        }
                                    }
                                }
                            } else {
                                if (assignmentOperator()) {
                                    TokenType operator = currentToken.getType();

                                    if (match(currentToken.getType())) {
                                        BasicExpression basicExprSub = new BasicExpression(scope);
                                        isAccept = basicExpression(scope, basicExprSub);
                                        AssignmentExpression assignment = new AssignmentExpression(scope.getArray(identifier.getName()), expr, basicExprSub, operator, scope);
                                        basicExpr.setAssociatedNode(assignment);
                                    }
                                } else {
                                    binaryExpr.setLeft(arrayExp);
                                    isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                                    if (isAccept) {
                                        if (ternaryExpr.getCondition() == null) {
                                            if (binaryExpr.getRight() == null) {
                                                basicExpr.setAssociatedNode(arrayExp);
                                            } else {
                                                basicExpr.setAssociatedNode(binaryExpr);
                                            }  
                                        } else {
                                            basicExpr.setAssociatedNode(ternaryExpr);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                TokenType operator = currentToken.getType();
                PPMM_Expression ppmm = new PPMM_Expression(scope, scope.getVariable(identifier.getName()), operator);

                if (match(currentToken.getType())) {
                    binaryExpr.setLeft(ppmm);
                    isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                    if (isAccept) {
                        if (ternaryExpr.getCondition() == null) {
                            if (binaryExpr.getRight() == null) {
                                basicExpr.setAssociatedNode(ppmm);
                            } else {
                                basicExpr.setAssociatedNode(binaryExpr);
                            }  
                        } else {
                            basicExpr.setAssociatedNode(ternaryExpr);
                        }
                    }
                }
            } else {
                VariableExpression varExp = new VariableExpression(scope, identifier.getName());

                if (assignmentOperator()) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        BasicExpression basicExprSub = new BasicExpression(scope);
                        isAccept = basicExpression(scope, basicExprSub);
                        AssignmentExpression assignment = new AssignmentExpression(scope.getVariable(identifier.getName()), basicExprSub, operator, scope);
                        basicExpr.setAssociatedNode(assignment);
                    }
                } else {
                    binaryExpr.setLeft(varExp);
                    isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);

                    if (isAccept) {
                        if (ternaryExpr.getCondition() == null) {
                            if (binaryExpr.getRight() == null) {
                                basicExpr.setAssociatedNode(varExp);
                            } else {
                                basicExpr.setAssociatedNode(binaryExpr);
                            }  
                        } else {
                            basicExpr.setAssociatedNode(ternaryExpr);
                        }
                    }
                }
            }
        }

        if (isTest) System.out.println("<<< Exit rest_of_Identifier_started_basic_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfPPMMIdentifierStartedBasicExp(Scope scope, Token identifier, PPMM_Expression ppmm, BasicExpression basicExpr) {
        // : [ '[' expression ']' ] romce_and_romloe 
        if (isTest) System.out.println(">>> Enter rest_of_PPMM_Identifier_started_basic_exp");
        boolean isAccept = false;
        BinaryExpression binaryExpr = new BinaryExpression();
        TernaryExpression ternaryExpr = new TernaryExpression();
        
        if (currentToken.getType() == TokenType.LEFT_BRACKET) { // Array
            if (match(TokenType.LEFT_BRACKET)) {
                Expression expr = new Expression();

                if (expression(scope, expr)) {
                    if (match(TokenType.RIGHT_BRACKET)) {
                        ppmm.setArray(scope.getArray(identifier.getName()));
                        ppmm.setExpression(expr);
                        binaryExpr.setLeft(ppmm);
                        isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);
                    }
                }
            }
        } else { // Variable
            ppmm.setVariable(scope.getVariable(identifier.getName()));
            binaryExpr.setLeft(ppmm);
            isAccept = romceAndRomloe(scope, binaryExpr, ternaryExpr);
        }

        if (isAccept) {
            if (ternaryExpr.getCondition() == null) {
                if (binaryExpr.getRight() == null) {
                    basicExpr.setAssociatedNode(ppmm);
                } else {
                    basicExpr.setAssociatedNode(binaryExpr);
                } 
            } else {
                basicExpr.setAssociatedNode(ternaryExpr);
            }
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

    private boolean actualParameterList (Scope scope, ArrayList<String> list) { // TODO check this
        // : basic_expression { ',' basic_expression }
        if (isTest) System.out.println(">>> Enter actual_parameter_list");
        boolean isAccept = false;
        BasicExpression basicExpr = new BasicExpression(scope);

        if (isConstant(scope)) list.add(currentToken.getName());

        if (basicExpression(scope, basicExpr)) {
            if (currentToken.getType() == TokenType.COMMA) {
                boolean isStop = false;

                while (!isStop) {
                    if (match(TokenType.COMMA)) {
                        if (isConstant(scope)) list.add(currentToken.getName());

                        basicExpr = new BasicExpression(scope);

                        if (basicExpression(scope, basicExpr)) {
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

    private boolean assignmentOperator() {
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

    private boolean romceAndRomloe(Scope scope, BinaryExpression binaryExpr, TernaryExpression ternaryExpr) { // rest_of_maybe_conditional_exp_and_rest_of_maybe_logical_OR_exp
        // : rest_of_maybe_logical_OR_exp [ '?' basic_expression ':' basic_expression ]
        if (isTest) System.out.println(">>> Enter romce_and_romloe");
        boolean isAccept = false;
        
        if (restOfMaybeLogicalORExp(scope, binaryExpr)) {
            if (currentToken.getType() == TokenType.CONDITIONAL) {
                if (match(TokenType.CONDITIONAL)) {
                    BasicExpression basicExprTrue = new BasicExpression(scope);

                    if (basicExpression(scope, basicExprTrue)) {
                        if (match(TokenType.COLON)) {
                            BasicExpression basicExprFalse = new BasicExpression(scope);

                            isAccept = basicExpression(scope, basicExprFalse);

                            if (isAccept) {
                                ternaryExpr.setCondition(binaryExpr);
                                ternaryExpr.setTrueExpression(basicExprTrue);
                                ternaryExpr.setFalseExpression(basicExprFalse);
                            }
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

    private boolean restOfMaybeLogicalORExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_logical_AND_exp { OR maybe_logical_AND_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_logical_OR_exp");
        boolean isAccept = false;
        
        if (restOfMaybeLogicalANDExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.OR) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.OR)) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeLogicalANDExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeLogicalANDExp(Scope scope, TerminalNode term) {
        // : maybe_bit_OR_exp { AND maybe_bit_OR_exp }
        if (isTest) System.out.println(">>> Enter maybe_logical_AND_exp");
        boolean isAccept = false;
        
        if (maybeBitORExp(scope, term)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.AND) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.AND)) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeBitORExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeLogicalANDExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_bit_OR_exp { AND maybe_bit_OR_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_logical_AND_exp");
        boolean isAccept = false;
        
        if (restOfMaybeBitORExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.AND) {
                    TokenType operator = currentToken.getType();
                    
                    if (match(TokenType.AND)) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeBitORExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeBitORExp(Scope scope, TerminalNode term) {
        // : maybe_bit_ex_OR_exp { '|' maybe_bit_ex_OR_exp }
        if (isTest) System.out.println(">>> Enter maybe_bit_OR_exp");
        boolean isAccept = false;
        
        if (maybeBitExORExp(scope, term)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_OR) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.BITWISE_OR)) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeBitExORExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeBitORExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_bit_ex_OR_exp { '|' maybe_bit_ex_OR_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_bit_OR_exp");
        boolean isAccept = false;
        
        if (restOfMaybeBitExORExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_OR) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.BITWISE_OR)) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeBitExORExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeBitExORExp(Scope scope, TerminalNode term) {
        // : maybe_bit_AND_exp { '^' maybe_bit_AND_exp }
        if (isTest) System.out.println(">>> Enter maybe_bit_ex_OR_exp");
        boolean isAccept = false;
        
        if (maybeBitANDExp(scope, term)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.XOR) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.XOR)) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeBitANDExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeBitExORExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_bit_AND_exp { '^' maybe_bit_AND_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_bit_ex_OR_exp");
        boolean isAccept = false;
        
        if (restOfMaybeBitANDExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.XOR) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.XOR)) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeBitANDExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeBitANDExp(Scope scope, TerminalNode term) {
        // : maybe_equality_exp { '&' maybe_equality_exp }
        if (isTest) System.out.println(">>> Enter maybe_bit_AND_exp");
        boolean isAccept = false;
        
        if (maybeEqualityExp(scope, term)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_AND) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.RIGHT_PAREN)) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeEqualityExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeBitANDExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_equality_exp { '&' maybe_equality_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_bit_AND_exp");
        boolean isAccept = false;
        
        if (restOfMaybeEqualityExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.BITWISE_AND) {
                    TokenType operator = currentToken.getType();

                    if (match(TokenType.RIGHT_PAREN)) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeEqualityExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeEqualityExp(Scope scope, TerminalNode term) {
        // : maybe_relational_exp 
        //   { ( EQ | NEQ ) maybe_relational_exp}
        if (isTest) System.out.println(">>> Enter maybe_equality_exp");
        boolean isAccept = false;
        
        if (maybeRelationalExp(scope, term)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.EQ || currentToken.getType() == TokenType.NEQ) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeRelationalExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeEqualityExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_relational_exp 
        //   { ( EQ | NEQ ) maybe_relational_exp}
        if (isTest) System.out.println(">>> Enter rest_of_maybe_equality_exp");
        boolean isAccept = false;
        
        if (restOfMaybeRelationalExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.EQ || currentToken.getType() == TokenType.NEQ) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeRelationalExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeRelationalExp(Scope scope, TerminalNode term) {
        // : maybe_shift_exp 
        //   { ( '<' | '>' | LE | GE ) maybe_shift_exp }
        if (isTest) System.out.println(">>> Enter maybe_relational_exp");
        boolean isAccept = false;
        
        if (maybeShiftExp(scope, term)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.LESS_THAN || currentToken.getType() == TokenType.GREATER_THAN ||
                    currentToken.getType() == TokenType.LE || currentToken.getType() == TokenType.GE) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeShiftExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeRelationalExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_shift_exp 
        //   { ( '<' | '>' | LE | GE ) maybe_shift_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_relational_exp ");
        boolean isAccept = false;
        
        if (restOfMaybeShiftExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.LESS_THAN || currentToken.getType() == TokenType.GREATER_THAN ||
                    currentToken.getType() == TokenType.LE || currentToken.getType() == TokenType.GE) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeShiftExp(scope, term)) {
                            isStop = true;
                        }

                        // TODO:

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeShiftExp(Scope scope, TerminalNode term) {
        // : maybe_additive_exp { ( LS | RS ) maybe_additive_exp }
        if (isTest) System.out.println(">>> Enter maybe_shift_exp");
        boolean isAccept = false;
        
        if (maybeAdditiveExp(scope, term)) {
            boolean isStop = false;

            if (currentToken.getType() == TokenType.LS && isCout) currentToken.setType(TokenType.NOP);

            while (!isStop) {
                if (currentToken.getType() == TokenType.LS || currentToken.getType() == TokenType.RS) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeAdditiveExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeShiftExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_additive_exp { ( LS | RS ) maybe_additive_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_shift_exp");
        boolean isAccept = false;

        if (restOfMaybeAdditiveExp(scope, binaryExpr)) {
            boolean isStop = false;

            if (currentToken.getType() == TokenType.LS && isCout) currentToken.setType(TokenType.NOP);

            while (!isStop) {
                if (currentToken.getType() == TokenType.LS || currentToken.getType() == TokenType.RS) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeAdditiveExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeAdditiveExp(Scope scope, TerminalNode term) {
        // : maybe_mult_exp { ( '+' | '-' ) maybe_mult_exp }
        if (isTest) System.out.println(">>> Enter maybe_additive_exp");
        boolean isAccept = false;
        
        if (maybeMultExp(scope, term)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode termCopy = term.getAssociatedNode();
                        TerminalNode subTerm = new TerminalNode();

                        if (!maybeMultExp(scope, subTerm)) {
                            isStop = true;
                        }

                        BinaryExpression binaryExpr = new BinaryExpression(termCopy, subTerm, operator);
                        term.setAssociatedNode(binaryExpr);
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

    private boolean restOfMaybeAdditiveExp(Scope scope, BinaryExpression binaryExpr) {
        // : rest_of_maybe_mult_exp { ( '+' | '-' ) maybe_mult_exp }
        if (isTest) System.out.println(">>> Enter rest_of_maybe_additive_exp");
        boolean isAccept = false;
        
        if (restOfMaybeMultExp(scope, binaryExpr)) {
            boolean isStop = false;

            while (!isStop) {
                if (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
                    TokenType operator = currentToken.getType();

                    if (match(currentToken.getType())) {
                        ExpressionNode binaryExprRight = binaryExpr.getRight();
                        TerminalNode term = new TerminalNode();

                        if (!maybeMultExp(scope, term)) {
                            isStop = true;
                        }

                        if (binaryExpr.getOperator() == null) {
                            binaryExpr.setOperator(operator);
                            binaryExpr.setRight(term);
                        } else {
                            if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                                // less or equal, binaryExpr root move to rightTree's left tree
                                BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                                binaryExpr.setLeft(binaryExprCopy);
                                binaryExpr.setRight(term);
                                binaryExpr.setOperator(operator);
                            } else {
                                BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                                binaryExpr.setRight(rightTree);
                            }
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

    private boolean maybeMultExp(Scope scope, TerminalNode term) {
        // : unary_exp rest_of_maybe_mult_exp
        if (isTest) System.out.println(">>> Enter maybe_mult_exp");
        boolean isAccept = false;
        TerminalNode subTerm = new TerminalNode();

        if (unaryExp(scope, subTerm)) {
            BinaryExpression multExpr = new BinaryExpression();
            multExpr.setLeft(subTerm);
            isAccept = restOfMaybeMultExp(scope, multExpr);

            if (isAccept) {
                if (multExpr.getRight() == null) {
                    term.setAssociatedNode(subTerm);
                } else {
                    term.setAssociatedNode(multExpr);
                }
            }
        }

        if (isTest) System.out.println("<<< Exit maybe_mult_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean restOfMaybeMultExp(Scope scope, BinaryExpression binaryExpr) {
        // : { ( '*' | '/' | '%' ) unary_exp }  /* could be empty ! */
        if (isTest) System.out.println(">>> Enter rest_of_maybe_mult_exp");
        boolean isAccept = false;
        boolean isStop = false;
        
        while (!isStop) {
            if (currentToken.getType() == TokenType.MULTIPLY || currentToken.getType() == TokenType.DIVIDE ||
                currentToken.getType() == TokenType.MOD) {
                TokenType operator = currentToken.getType();

                if (match(currentToken.getType())) {
                    ExpressionNode binaryExprRight = binaryExpr.getRight();
                    TerminalNode term = new TerminalNode();

                    if (!unaryExp(scope, term)) {
                        isStop = true;
                    }

                    if (binaryExpr.getOperator() == null) {
                        binaryExpr.setOperator(operator);
                        binaryExpr.setRight(term);
                    } else {
                        if (comparePrecedence(binaryExpr.getOperator(), operator)) {
                            // less or equal, binaryExpr root move to rightTree's left tree
                            BinaryExpression binaryExprCopy = new BinaryExpression(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
                            binaryExpr.setLeft(binaryExprCopy);
                            binaryExpr.setRight(term);
                            binaryExpr.setOperator(operator);
                        } else {
                            BinaryExpression rightTree = new BinaryExpression(binaryExprRight, term, operator);
                            binaryExpr.setRight(rightTree);
                        }
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

    private boolean unaryExp(Scope scope, TerminalNode term) {
        // : sign { sign } signed_unary_exp
        // | unsigned_unary_exp
        // | ( PP | MM ) Identifier [ '[' expression ']' ]
        if (isTest) System.out.println(">>> Enter unary_exp");
        boolean isAccept = false;
        
        if (sign()) {
            ArrayList<TokenType> signs = new ArrayList<TokenType>();
            signs.add(currentToken.getType());

            if (match(currentToken.getType())) {
                boolean isStop = false;

                while(!isStop) {
                    if (sign()) {
                        signs.add(currentToken.getType());
                        boolean nop = match(currentToken.getType());
                    } else {
                        isStop = true;
                    }
                }

                TokenType operator = simplifySigns(signs);
                TerminalNode signedUnaryExpTerm = new TerminalNode();
                isAccept = signedUnaryExp(scope, signedUnaryExpTerm);

                if (isAccept) {
                    UnaryExpression unaryExpr = new UnaryExpression(operator, signedUnaryExpTerm);
                    term.setAssociatedNode(unaryExpr);
                }
            }
        } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
            TokenType operator = currentToken.getType();

            if (match(currentToken.getType())) {
                Token identifier = currentToken;

                if (currentToken.getType() == TokenType.IDENTIFIER) {
                    if (!checkDeclaration(identifier, false, scope)) return false;
                }

                if (match(TokenType.IDENTIFIER)){
                    if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                        // Array
                        if (match(TokenType.LEFT_BRACKET)) {
                            Expression expr = new Expression();

                            if (expression(scope, expr)) {
                                isAccept = match(TokenType.RIGHT_BRACKET);

                                if (isAccept) {
                                    PPMM_Expression ppmm = new PPMM_Expression(scope, operator, scope.getArray(identifier.getName()), expr);
                                    term.setAssociatedNode(ppmm);
                                }
                            }
                        }
                    } else {
                        // Variable
                        isAccept = true;

                        if (isAccept) {
                            PPMM_Expression ppmm = new PPMM_Expression(scope, operator, scope.getVariable(identifier.getName()));
                            term.setAssociatedNode(ppmm);
                        }
                    }
                }
            }
        } else {
            isAccept = unsignedUnaryExp(scope, term);
        }

        if (isTest) System.out.println("<<< Exit unary_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean signedUnaryExp(Scope scope, TerminalNode term) {
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
                if (currentToken.getType() == TokenType.LEFT_PAREN) { // TODO: if do the function call need to fix from this line...
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
                    }                                                // ...to this line
                } else if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                    // Array
                    Expression expr = new Expression();

                    if (!checkDeclaration(identifier, false, scope)) return false;

                    if (match(TokenType.LEFT_BRACKET)) {
                        if (expression(scope, expr)) {
                            isAccept = match(TokenType.RIGHT_BRACKET);

                            if (isAccept) {
                                ArrayExpression array = new ArrayExpression(scope, identifier.getName(), expr);
                                term.setAssociatedNode(array);
                            }
                        }
                    }
                } else {
                    // Variable
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    VariableExpression variable = new VariableExpression(scope, identifier.getName());
                    isAccept = true;

                    if (isAccept) term.setAssociatedNode(variable);
                }
            }
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            if (match(TokenType.LEFT_PAREN)) {
                Expression expr = new Expression();
                boolean isCoutMode = isCout;

                if (isCoutMode) isCout = false;

                if (expression(scope, expr)) {
                    isAccept = match(TokenType.RIGHT_PAREN);

                    if (isAccept) term.setAssociatedNode(expr);
                }

                if (isCoutMode) isCout = true;
            }
        } else {
            Object value = tokenChangeToValue(currentToken);
            isAccept = constant(scope);
            LiteralExpression literal = new LiteralExpression(value);
            
            if (isAccept) term.setAssociatedNode(literal);
        }

        if (isTest) System.out.println("<<< Exit signed_unary_exp. isAccept = " + isAccept);

        return isAccept;
    }

    private boolean unsignedUnaryExp(Scope scope, TerminalNode term) {
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
                if (currentToken.getType() == TokenType.LEFT_PAREN) { // TODO: if do the function call need to fix from this line...
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
                    }                                                // ...to this line
                } else if (currentToken.getType() == TokenType.LEFT_BRACKET) {
                    // Array
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    if (match(TokenType.LEFT_BRACKET)) {
                        Expression expr = new Expression();

                        if (expression(scope, expr)) {
                            if(match(TokenType.RIGHT_BRACKET)) {
                                ArrayExpression array = new ArrayExpression(scope, identifier.getName(), expr);

                                if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                                    TokenType operator = currentToken.getType();
                                    PPMM_Expression ppmm = new PPMM_Expression(scope, scope.getArray(identifier.getName()), expr, operator);
                                    isAccept = match(currentToken.getType());

                                    if (isAccept) term.setAssociatedNode(ppmm);
                                } else {
                                    isAccept = true;

                                    if (isAccept) term.setAssociatedNode(array);
                                }
                            }
                        }
                    }
                } else if (currentToken.getType() == TokenType.PP || currentToken.getType() == TokenType.MM) {
                    // Variable
                    if (!checkDeclaration(identifier, false, scope)) return false;

                    TokenType operator = currentToken.getType();
                    PPMM_Expression ppmm = new PPMM_Expression(scope, scope.getVariable(identifier.getName()), operator);
                    isAccept = match(currentToken.getType());

                    if (isAccept) term.setAssociatedNode(ppmm);
                } else {
                    // Variable
                    if (!checkDeclaration(identifier, false, scope)) return false;
                    
                    VariableExpression variable = new VariableExpression(scope, identifier.getName());
                    isAccept = true;
                    if (isAccept) term.setAssociatedNode(variable);
                }
            }
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            if (match(TokenType.LEFT_PAREN)) {
                Expression expr = new Expression();
                boolean isCoutMode = isCout;

                if (isCoutMode) isCout = false;

                if (expression(scope, expr)) {
                    isAccept = match(TokenType.RIGHT_PAREN);

                    if (isAccept) term.setAssociatedNode(expr);
                }

                if (isCoutMode) isCout = true;
            }
        } else {
            Object value = tokenChangeToValue(currentToken);
            isAccept = constant(scope);
            LiteralExpression literal = new LiteralExpression(value);
            
            if (isAccept) term.setAssociatedNode(literal);
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

    private Object tokenChangeToValue(Token token) {
        String tokenName = token.getName();
        TokenType tokenType = token.getType();

        switch (tokenType) {
            case CONSTANT_FALSE:
            case CONSTANT_TRUE:
                boolean boolValue = Boolean.parseBoolean(tokenName);
                return boolValue;

            case CONSTANT_INT:
                int intValue = Integer.parseInt(tokenName);
                return intValue;

            case CONSTANT_FLOAT:
                float floatValue = Float.parseFloat(tokenName);
                return floatValue;

            case CONSTANT_CHAR:
                char charValue = tokenName.charAt(0);
                return charValue;
        
            default:
                return tokenName;
        }
    }

    private TokenType simplifySigns(ArrayList<TokenType> signs) {
        int sign = 1;  // Start with positive (1)

        for (TokenType type : signs) {
            if (type == TokenType.PLUS) {
                // '+' doesn't change the sign
                continue;
            } else if (type == TokenType.MINUS) {
                // '-' flips the sign
                sign *= -1;
            } else if (type == TokenType.NOT) {
                // '!' flips the sign
                sign *= -1;
            }
        }

        // Return the final simplified sign
        return sign == 1 ? TokenType.PLUS : TokenType.MINUS;
    }

    public boolean comparePrecedence(TokenType rootOp, TokenType newOp) {
        int rootOpPrecedence = getPrecedence(rootOp);
        int newOpPrecedence = getPrecedence(newOp);
    
        return rootOpPrecedence >= newOpPrecedence;
    }
    
    private int getPrecedence(TokenType tokenType) {
        switch (tokenType) {
            case OR:
                return 1;

            case AND:
                return 2;

            case BITWISE_OR:
                return 3;

            case XOR:
                return 4;

            case BITWISE_AND:
                return 5;

            case EQ: 
            case NEQ:
                return 6;

            case LESS_THAN: 
            case GREATER_THAN: 
            case GE: 
            case LE:
                return 7;

            case PLUS: case MINUS:
                return 8;

            case MULTIPLY: 
            case DIVIDE: 
            case MOD:
                return 9;

            default:
                return -1;
        }
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