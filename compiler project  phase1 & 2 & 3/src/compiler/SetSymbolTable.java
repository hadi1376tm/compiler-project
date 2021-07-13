package compiler;
import java.util.*;

import gen.MoolaListener;
import gen.MoolaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;


public class SetSymbolTable implements MoolaListener {
    boolean entry;
    int Indentation = 0;
    int ClassAns = 0;
    int MethodAns = 0;
    boolean InClass = false;
    boolean InBlock = false;
    boolean InMethod = false;


    Hashtable GlobalScope = new Hashtable(); // classes are in this
    Hashtable ClassScope[] = new Hashtable [50]; //method and vars are in this
    Hashtable MethodScope[] = new Hashtable [50];

    @Override
    public void enterProgram(MoolaParser.ProgramContext ctx) {
        for (int i = 0; i < Indentation ; i++) {
            System.out.print("\t");

        }

        System.out.println("program start{");
        Indentation++;


    }



    @Override
    public void exitProgram(MoolaParser.ProgramContext ctx) {
        for (int i = 0; i < Indentation-1 ; i++) {
            System.out.print("\t");

        }
        System.out.println("}");
        Indentation--;
        System.out.println(GlobalScope.toString());
        System.out.println(ClassScope[2].toString());
    }

    @Override
    public void enterClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        InClass=true;
        ClassAns++; //to recognize method and vars are for which class

        if(!entry) {
            for (int i = 0; i < Indentation; i++) {
                System.out.print("\t");

            }
        }

        if(ctx.classParent == null){
            System.out.println( "class: "+ctx.className.getText()+"{");
        }
        else{
            System.out.println( "class: "+ctx.className.getText()+"/ class parent: ("+ctx.classParent.getText()+")"+"{");
        }
        Indentation++;
        GlobalScope.put(ctx.className.getText() , "Class");

        ClassScope[ClassAns]= new Hashtable(); //phase 2

    }


    @Override
    public void exitClassDeclaration(MoolaParser.ClassDeclarationContext ctx) {
        InClass=false;
        for (int i = 0; i < Indentation-1 ; i++) {
            System.out.print("\t");

        }
        System.out.println("}");
        Indentation--;


    }

    @Override
    public void enterEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {
        for (int i = 0; i < Indentation ; i++) {
            System.out.print("\t");

        }
        System.out.print("main ");
        entry = true;

    }

    @Override
    public void exitEntryClassDeclaration(MoolaParser.EntryClassDeclarationContext ctx) {

        entry=false;
    }

    @Override
    public void enterFieldDeclaration(MoolaParser.FieldDeclarationContext ctx) {
        for (int i = 0; i < Indentation ; i++) {
            System.out.print("\t");

        }
        System.out.println("field: "+ ctx.fieldName.getText()+"/ type="+ctx.fieldType.getText()+"/ access modifier="+ctx.fieldAccessModifier.getText());

        List<String> list = new ArrayList<>();
        list.add("field");
        list.add(ctx.fieldType.getText());
        //list.add(properties)?


        ClassScope[ClassAns].put(ctx.fieldName.getText() , list.toString());
        list.clear();
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
        InMethod= true;
        MethodAns++;
        MethodScope[MethodAns]= new Hashtable(); //phase 2

        for (int i = 0; i < Indentation ; i++) {
            System.out.print("\t");

        }
        if(entry){
            System.out.println("class method: "+ctx.methodName.getText()+"/ return type="+ctx.t.getText()+"/ access modifier= public"
                    +"{");
            Indentation++;}
        if(!entry){
            System.out.println("class method: "+ctx.methodName.getText()+"/ return type="+ctx.t.getText()+"/ access modifier= "
                    +ctx.methodAccessModifier.getText()+ "{");
            Indentation++;
        }


        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        list.add("method");
        if(ctx.typeP1==null) {
            list.add("->" + ctx.t.getText());
        }
        if(ctx.typeP1 != null && ctx.typeP2 == null) {
            list.add(ctx.typeP1.getText()+"->" + ctx.t.getText());
            list2.add("var");
            list2.add(ctx.typeP1.getText());
            MethodScope[MethodAns].put(ctx.param1.getText() , list2.toString());
            list2.clear();
        }
        if (ctx.typeP1 != null && ctx.typeP2 != null){
            list.add(ctx.typeP1.getText()+","+ctx.typeP2.getText()+"->" + ctx.t.getText());
            list2.add("var");
            list2.add(ctx.typeP1.getText());
            MethodScope[MethodAns].put(ctx.param1.getText() , list2.toString());
            list2.clear();
            list2.add("var");
            list2.add(ctx.typeP2.getText());
            MethodScope[MethodAns].put(ctx.param2.getText() , list2.toString());
            list2.clear();

        }


        //add properties

        ClassScope[ClassAns].put(ctx.methodName.getText() , list.toString());
        list.clear();



    }

    @Override
    public void exitMethodDeclaration(MoolaParser.MethodDeclarationContext ctx) {
        InMethod = false;
        for (int i = 0; i < Indentation-1 ; i++) {
            System.out.print("\t");

        }
        System.out.println("}");
        Indentation--;


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
        for (int i = 0; i < Indentation ; i++) {
            System.out.print("\t");

        }
        System.out.println("var: "+ctx.i1.getText());

        List<String> list = new ArrayList<>();
        list.add("var");
        list.add(ctx.e1.getText());

        MethodScope[MethodAns].put(ctx.i1.getText() , list.toString());
    }

    @Override
    public void exitStatementVarDef(MoolaParser.StatementVarDefContext ctx) {
    }

    @Override
    public void enterStatementBlock(MoolaParser.StatementBlockContext ctx) {

        InBlock=true;

    }

    @Override
    public void exitStatementBlock(MoolaParser.StatementBlockContext ctx) {

        InBlock=false;
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
