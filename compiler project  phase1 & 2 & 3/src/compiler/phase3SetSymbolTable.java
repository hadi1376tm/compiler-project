package compiler;

import phase3.symbolTable.error.Error;
import phase3.symbolTable.symbolTableItem.varItems.*;
import gen.MoolaListener;
import gen.MoolaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import phase3.symbolTable.SymbolTable;
import phase3.symbolTable.symbolTableItem.*;

import java.util.*;

public class phase3SetSymbolTable implements MoolaListener {


    SymbolTable tempsymbolTable;
    SymbolTable currentSymbolTable;
    MethodSymbolTableItem currentMethod;
    boolean entry;
    boolean defined_main= false;
    int blockNumber = 0;
    public static Map<String, MethodSymbolTableItem> methods = new HashMap<>();

    @Override
    public void enterProgram(MoolaParser.ProgramContext ctx) {
        currentSymbolTable = new SymbolTable(ctx.start.getLine(), 0, "program", null); //global scope

        SymbolTable.push(currentSymbolTable);
    }

    @Override
    public void exitProgram(MoolaParser.ProgramContext ctx) {
        SymbolTable.printAll();
    }

    @Override
    public void enterClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        String name = ctx.className.getText();
        int line = ctx.start.getLine();
        int column = ctx.className.getCharPositionInLine();
        if(entry && !defined_main){

            defined_main= true;
            ClassDeclaration mainClassSymbolItem =new MainClassSymbolItem(name); //main class item
            currentSymbolTable.put(mainClassSymbolItem); //save class item in global scope
            tempsymbolTable = new SymbolTable(line, column, name, currentSymbolTable); //new class scope
            mainClassSymbolItem.setSymbolTable(tempsymbolTable);


        }
        else {
            ClassSymbolTableItem classSymbolTableItem = new ClassSymbolTableItem(name);
            if(!currentSymbolTable.put(classSymbolTableItem)){
                tempsymbolTable = new SymbolTable(line, column, name, currentSymbolTable);
                classSymbolTableItem.setSymbolTable(tempsymbolTable);
                if(ctx.classParent != null){
                    classSymbolTableItem.setParent(ctx.classParent.getText());
                }
            }
            else {
                ErrorChatcher.errors.add(new Error(101, line, column, "class [" + name + "] has been defined already"));
                classSymbolTableItem.setName(ctx.className.getText() + "_" + line + "_" + column);
                name= classSymbolTableItem.getName();
                currentSymbolTable.put(classSymbolTableItem);
                tempsymbolTable = new SymbolTable(line, column, name, currentSymbolTable);
                classSymbolTableItem.setSymbolTable(tempsymbolTable);
                if(ctx.classParent != null){
                    classSymbolTableItem.setParent(ctx.classParent.getText());
                }

            }

        }
        SymbolTable.push(tempsymbolTable); //add too tree as class scope symbol table
        currentSymbolTable = tempsymbolTable; //working on class scope
    }

    @Override
    public void exitClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        currentSymbolTable = currentSymbolTable.getPreSymbolTable(); //back to pre scope(global)
        defined_main =false;
    }

    @Override
    public void enterEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {
        entry = true;
    }

    @Override
    public void exitEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {
        entry = false;
    }

    @Override
    public void enterFieldDeclaration(MoolaParser.FieldDeclarationContext ctx) {
        for (int i = 0; i < ctx.ID().size() ; i++) {
            int line = ctx.start.getLine();
            int column = ctx.fieldName.getCharPositionInLine();
            FieldSymbolTableItem fieldSymbolTableItem = new FieldSymbolTableItem(ctx.ID().get(i).getText(), ctx.fieldType.getText()
                    , ctx.fieldAccessModifier.getText() );
            if(currentSymbolTable.put (fieldSymbolTableItem)) {
                ErrorChatcher.errors.add(new Error(104, line, column, "field [" + fieldSymbolTableItem.getName() + "] has been defined already"));
                fieldSymbolTableItem.setName(ctx.fieldName.getText() + "_" + line + "_" + column);
                currentSymbolTable.put(fieldSymbolTableItem);
            }
        }
        //no scope for fields
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
        String name = ctx.methodName.getText();
        SymbolTable methodsymbolTable;
        int line = ctx.start.getLine();
        int column = ctx.start.getCharPositionInLine();

        MethodSymbolTableItem methodSymbolTableItem;
        if(entry){
            methodSymbolTableItem = new MethodSymbolTableItem(name, ctx.t.getText(), "public");
            entry= false;
        }
        else {
            methodSymbolTableItem = new MethodSymbolTableItem(name, ctx.t.getText(), ctx.methodAccessModifier.getText());
        }

        methodSymbolTableItem.setLine_column(line, column);

        if (currentSymbolTable.put(methodSymbolTableItem)) {
            ErrorChatcher.errors.add(new Error(102, line, column, "method [" + name + "] has been defined already"));
            methodSymbolTableItem.setName(name + "_" + line + "_" + column);
            name= methodSymbolTableItem.getName();
            currentSymbolTable.put(methodSymbolTableItem);
            methodsymbolTable = new SymbolTable(line, column, name, currentSymbolTable);
        }
        else {
            methodsymbolTable = new SymbolTable(line, column, name, currentSymbolTable);
            methods.put(name,methodSymbolTableItem);

        }

        SymbolTable.push(methodsymbolTable);
        currentMethod = methodSymbolTableItem;
        currentSymbolTable=methodsymbolTable;


        for (int i = 1; i < ctx.ID().size() ; i++) {
            currentMethod.addParameter(ctx.ID().get(i).getText());
            currentMethod.addParametertype(ctx.moolaType().get(i-1).getText());
            currentSymbolTable.put(new ParameterSymbolTableItem(ctx.ID().get(i).getText(),
                    ctx.moolaType().get(i-1).getText()));
        }


    }



    @Override
    public void exitMethodDeclaration(MoolaParser.MethodDeclarationContext ctx) {
        currentSymbolTable = currentSymbolTable.getPreSymbolTable();
        currentMethod = null;
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
        if(currentMethod != null){
            for (int i = 0; i < ctx.ID().size() ; i++) {
                LocalVariableSymbolTableItem var = new LocalVariableSymbolTableItem(ctx.ID().get(i).getText(), "var");
                int line = ctx.start.getLine();
                int column = ctx.i1.getCharPositionInLine();
                if(currentSymbolTable.put(var)) {
                    ErrorChatcher.errors.add(new Error(103, line, column, "Variable " + var.getName() + " has been defined already"));
                    var.setName(var.getName() + "_" + line + "_" + column);
                    currentSymbolTable.put(var);
                }
            }

        }
        }



    @Override
    public void exitStatementVarDef(MoolaParser.StatementVarDefContext ctx) {

    }

    @Override
    public void enterStatementBlock(MoolaParser.StatementBlockContext ctx) {

        if (!(ctx.statement().get(0).getText().equals("begin"))) {
            blockNumber++;
            int line = ctx.start.getLine();
            int column = ctx.start.getCharPositionInLine();
            currentSymbolTable = new SymbolTable(line, column,"Block"+blockNumber, currentSymbolTable);
            SymbolTable.push(currentSymbolTable);
        }

    }


    @Override
    public void exitStatementBlock(MoolaParser.StatementBlockContext ctx) {
        if (!(ctx.statement().get(0).getText().equals("begin"))) {
            currentSymbolTable = currentSymbolTable.getPreSymbolTable();
        }
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

    }

    @Override
    public void exitExpressionMethods(MoolaParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void enterExpressionMethodsTemp(MoolaParser.ExpressionMethodsTempContext ctx) {

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

}
