package compiler;
import phase3.symbolTable.SymbolTable;
import gen.MoolaListener;
import gen.MoolaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import phase3.symbolTable.error.Error;

import java.util.LinkedList;
import java.util.Queue;

public class ErrorChatcher implements MoolaListener {
    SymbolTable currentMethod;
    SymbolTable currentClass;
    SymbolTable currentProgram;
    String currentMethod_returnType;
    String tmp = null;
    public static Queue<Error> errors = new LinkedList<>();

    @Override
    public void enterProgram(MoolaParser.ProgramContext ctx) {
        currentProgram = SymbolTable.getSymbolTableByKey("program");
        //System.out.println(currentSymbolTable);

    }

    @Override
    public void exitProgram(MoolaParser.ProgramContext ctx) {
      currentProgram = null;
        System.out.println("\nERRORS:");
        System.out.println("***********************");
        printErrors(errors);
        System.out.println("***********************");
    }

    @Override
    public void enterClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        currentClass = SymbolTable.getSymbolTableByKey(ctx.className.getText());
    }

    @Override
    public void exitClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
       // currentClass = currentClass.getPreSymbolTable();
    }

    @Override
    public void enterEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {

    }

    @Override
    public void exitEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {

    }

    @Override
    public void enterFieldDeclaration(MoolaParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void exitFieldDeclaration(MoolaParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void enterAccess_modifier(MoolaParser.Access_modifierContext ctx) {

    }

    @Override
    public void exitAccess_modifier(MoolaParser.Access_modifierContext ctx) {

    }

    @Override
    public void enterMethodDeclaration(MoolaParser.MethodDeclarationContext ctx) {
        currentMethod = SymbolTable.getSymbolTableByKey(ctx.methodName.getText());
        currentMethod_returnType= ctx.t.getText();
    }

    @Override
    public void exitMethodDeclaration(MoolaParser.MethodDeclarationContext ctx) {
      //  currentMethod = currentMethod.getPreSymbolTable();
    }

    @Override
    public void enterClosedStatement(MoolaParser.ClosedStatementContext ctx) {

    }

    @Override
    public void exitClosedStatement(MoolaParser.ClosedStatementContext ctx) {

    }

    @Override
    public void enterClosedConditional(MoolaParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void exitClosedConditional(MoolaParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void enterOpenConditional(MoolaParser.OpenConditionalContext ctx) {

    }

    @Override
    public void exitOpenConditional(MoolaParser.OpenConditionalContext ctx) {

    }

    @Override
    public void enterOpenStatement(MoolaParser.OpenStatementContext ctx) {

    }

    @Override
    public void exitOpenStatement(MoolaParser.OpenStatementContext ctx) {

    }

    @Override
    public void enterStatement(MoolaParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(MoolaParser.StatementContext ctx) {

    }

    @Override
    public void enterStatementVarDef(MoolaParser.StatementVarDefContext ctx) {
    }

    @Override
    public void exitStatementVarDef(MoolaParser.StatementVarDefContext ctx) {

    }

    @Override
    public void enterStatementBlock(MoolaParser.StatementBlockContext ctx) {


    }

    @Override
    public void exitStatementBlock(MoolaParser.StatementBlockContext ctx) {

    }

    @Override
    public void enterStatementContinue(MoolaParser.StatementContinueContext ctx) {

    }

    @Override
    public void exitStatementContinue(MoolaParser.StatementContinueContext ctx) {

    }

    @Override
    public void enterStatementBreak(MoolaParser.StatementBreakContext ctx) {

    }

    @Override
    public void exitStatementBreak(MoolaParser.StatementBreakContext ctx) {

    }

    @Override
    public void enterStatementReturn(MoolaParser.StatementReturnContext ctx) {
    String r =ctx.e.getText();
    if(!currentMethod_returnType.equals(r)){
        int line = ctx.start.getLine();
        int column = ctx.e.start.getCharPositionInLine();
        errors.add(new Error(210, line, column, "ReturnType of this method must be : "+currentMethod_returnType));

    }
    }

    @Override
    public void exitStatementReturn(MoolaParser.StatementReturnContext ctx) {

    }

    @Override
    public void enterStatementClosedLoop(MoolaParser.StatementClosedLoopContext ctx) {

    }

    @Override
    public void exitStatementClosedLoop(MoolaParser.StatementClosedLoopContext ctx) {

    }

    @Override
    public void enterStatementOpenLoop(MoolaParser.StatementOpenLoopContext ctx) {

    }

    @Override
    public void exitStatementOpenLoop(MoolaParser.StatementOpenLoopContext ctx) {

    }

    @Override
    public void enterStatementWrite(MoolaParser.StatementWriteContext ctx) {

    }

    @Override
    public void exitStatementWrite(MoolaParser.StatementWriteContext ctx) {

    }

    @Override
    public void enterStatementAssignment(MoolaParser.StatementAssignmentContext ctx) {
      int line = ctx.start.getLine();
      int column = ctx.left.start.getCharPositionInLine();
      String statement = ctx.left.getText();
        // System.out.println("********"+ statement);
        //System.out.println(currentMethod.name);
        // System.out.println(currentMethod.get("statement"));
    if(currentClass.get(statement) == null && currentMethod.get(statement) == null){
      errors.add(new Error(106, line, column, "Can not find Variable[ "+statement+"]"));
      }
    }

    @Override
    public void exitStatementAssignment(MoolaParser.StatementAssignmentContext ctx) {

    }

    @Override
    public void enterStatementInc(MoolaParser.StatementIncContext ctx) {

    }

    @Override
    public void exitStatementInc(MoolaParser.StatementIncContext ctx) {

    }

    @Override
    public void enterStatementDec(MoolaParser.StatementDecContext ctx) {

    }

    @Override
    public void exitStatementDec(MoolaParser.StatementDecContext ctx) {

    }

    @Override
    public void enterExpression(MoolaParser.ExpressionContext ctx) {

    }

    @Override
    public void exitExpression(MoolaParser.ExpressionContext ctx) {

    }

    @Override
    public void enterExpressionOr(MoolaParser.ExpressionOrContext ctx) {

    }

    @Override
    public void exitExpressionOr(MoolaParser.ExpressionOrContext ctx) {

    }

    @Override
    public void enterExpressionOrTemp(MoolaParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void exitExpressionOrTemp(MoolaParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void enterExpressionAnd(MoolaParser.ExpressionAndContext ctx) {

    }

    @Override
    public void exitExpressionAnd(MoolaParser.ExpressionAndContext ctx) {

    }

    @Override
    public void enterExpressionAndTemp(MoolaParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void exitExpressionAndTemp(MoolaParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void enterExpressionEq(MoolaParser.ExpressionEqContext ctx) {

    }

    @Override
    public void exitExpressionEq(MoolaParser.ExpressionEqContext ctx) {

    }

    @Override
    public void enterExpressionEqTemp(MoolaParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void exitExpressionEqTemp(MoolaParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void enterExpressionCmp(MoolaParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void exitExpressionCmp(MoolaParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void enterExpressionCmpTemp(MoolaParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void exitExpressionCmpTemp(MoolaParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void enterExpressionAdd(MoolaParser.ExpressionAddContext ctx) {

    }

    @Override
    public void exitExpressionAdd(MoolaParser.ExpressionAddContext ctx) {

    }

    @Override
    public void enterExpressionAddTemp(MoolaParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void exitExpressionAddTemp(MoolaParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void enterExpressionMultMod(MoolaParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void exitExpressionMultMod(MoolaParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void enterExpressionMultModTemp(MoolaParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void exitExpressionMultModTemp(MoolaParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void enterExpressionUnary(MoolaParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void exitExpressionUnary(MoolaParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void enterExpressionMethods(MoolaParser.ExpressionMethodsContext ctx) {
        String o;
        boolean dot = false;
        if (ctx.o.i1 != null) {
            o = ctx.o.i1.getText();
        }
        if (ctx.mt.dotOp!= null)
        {
            dot = true;
        }
        if (dot==true && ctx.o.i1 != null){
            o = ctx.o.i1.getText();
            int line = ctx.start.getLine();
            int column = ctx.o.i1.getLine();

                if (!foundMethod(o) && currentProgram.get(o)==null) {
                      errors.add(new Error(105, line, column, "Can not find class [" + o + "]"));

                }

        }

    }


    @Override
    public void exitExpressionMethods(MoolaParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void enterExpressionMethodsTemp(MoolaParser.ExpressionMethodsTempContext ctx) {
        /*
        if(ctx.dotOp.getText().equals(".")){
        if(tmp!=null){
            String expressionName=tmp;
            int line = ctx.start.getLine();
            int column = ctx.start.getLine();
            if(!Character.isUpperCase(expressionName.charAt(0))) {
                if (!foundMethod(expressionName)) {
                    //  errors.add(new Error(111, line, column, "Can not find method[" + expressionName + "]"));

                }
            }
            if(Character.isUpperCase(expressionName.charAt(0))){
                if (currentProgram.get(expressionName)==null){
                    errors.add(new Error(105, line, column, "Can not find class[" + expressionName + "]"));
                }

            }
        }
        }*/
    }

    @Override
    public void exitExpressionMethodsTemp(MoolaParser.ExpressionMethodsTempContext ctx) {

    }

    @Override
    public void enterExpressionOther(MoolaParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void exitExpressionOther(MoolaParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void enterMoolaType(MoolaParser.MoolaTypeContext ctx) {

    }

    @Override
    public void exitMoolaType(MoolaParser.MoolaTypeContext ctx) {

    }

    @Override
    public void enterSingleType(MoolaParser.SingleTypeContext ctx) {

    }

    @Override
    public void exitSingleType(MoolaParser.SingleTypeContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
    public static void printErrors(Queue<Error> errs) {
        for (Error err : errs) {
            System.out.println(err.toString());
        }
    }
    public static boolean foundMethod(String e) {
        if(phase3SetSymbolTable.methods.get(e)== null){
            return false;}
            else{
        boolean found_in_public_class= (phase3SetSymbolTable.methods.get(e).getAccessModifier().equals("public"));
        return found_in_public_class;
            }
    }


}
