package compiler;

import compiler.symbolTable.symbolTableItem.varItems.*;

import gen.MoolaListener;
import gen.MoolaParser;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import compiler.symbolTable.SymbolTable;
import compiler.symbolTable.symbolTableItem.*;

import java.util.*;

public class SetSymbolTable2 implements MoolaListener {
    SymbolTable tempsymbolTable;
    SymbolTable currentSymbolTable;
    MethodSymbolTableItem currentMethod;
    int blockNumber = 0;
    boolean entry;

    @Override
    public void enterProgram(MoolaParser.ProgramContext ctx) {
        currentSymbolTable = new SymbolTable("program", null); //global scope
        SymbolTable.push(currentSymbolTable);
    }

    @Override
    public void exitProgram(MoolaParser.ProgramContext ctx) {
        SymbolTable.printAll();
    }

    @Override
    public void enterClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        String name = ctx.className.getText();
        if(entry){
            ClassDeclaration mainClassSymbolItem =new MainClassSymbolItem(name); //main class item
            currentSymbolTable.put(mainClassSymbolItem); //save class item in global scope
            tempsymbolTable = new SymbolTable(name, currentSymbolTable); //new class scope
            mainClassSymbolItem.setSymbolTable(tempsymbolTable);
            SymbolTable.push(tempsymbolTable); //add too tree as class scope symbol table
            currentSymbolTable = tempsymbolTable; //working on class scope

        }
        else {
            ClassSymbolTableItem classSymbolTableItem = new ClassSymbolTableItem(name);
            currentSymbolTable.put(classSymbolTableItem);
            tempsymbolTable = new SymbolTable(name, currentSymbolTable);
            classSymbolTableItem.setSymbolTable(tempsymbolTable);
            SymbolTable.push(tempsymbolTable);
            currentSymbolTable = tempsymbolTable;
            if(ctx.classParent != null){
                classSymbolTableItem.setParent(ctx.classParent.getText());
            }
        }


    }

    @Override
    public void exitClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        currentSymbolTable = currentSymbolTable.getPreSymbolTable(); //back to pre scope(global)
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
            currentSymbolTable.put (new FieldSymbolTableItem(ctx.ID().get(i).getText(), ctx.fieldType.getText()
                    , ctx.fieldAccessModifier.getText() ));
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

        MethodSymbolTableItem methodSymbolTableItem;
        if(entry){
            methodSymbolTableItem = new MethodSymbolTableItem(name, ctx.t.getText(), "public");

        }
        else {
            methodSymbolTableItem = new MethodSymbolTableItem(name, ctx.t.getText(), ctx.methodAccessModifier.getText());
        }
        currentSymbolTable.put(methodSymbolTableItem);
        methodsymbolTable = new SymbolTable(name, currentSymbolTable);
        currentMethod = methodSymbolTableItem;
        SymbolTable.push(methodsymbolTable);
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
        if (entry)
            entry=false;
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
                currentSymbolTable.put(new LocalVariableSymbolTableItem(ctx.ID().get(i).getText(), "var"));
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
            currentSymbolTable = new SymbolTable("Block"+blockNumber, currentSymbolTable);
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
