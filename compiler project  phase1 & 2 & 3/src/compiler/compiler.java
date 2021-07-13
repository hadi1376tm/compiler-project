package compiler;
import gen.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.io.IOException;


public class compiler {
    public static void main(String[] args) throws IOException{
        CharStream stream= CharStreams.fromFileName("./test.mla");
        MoolaLexer lexer= new MoolaLexer(stream);
        TokenStream tockens = new CommonTokenStream(lexer);
        MoolaParser parser = new MoolaParser(tockens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();

        CharStream stream2= CharStreams.fromFileName("./error_test.mla");
        MoolaLexer lexer2= new MoolaLexer(stream2);
        TokenStream tockens2 = new CommonTokenStream(lexer2);
        MoolaParser parser2 = new MoolaParser(tockens2);
        parser2.setBuildParseTree(true);
        ParseTree tree2 = parser2.program();
        ParseTreeWalker walker2 = new ParseTreeWalker();




        MoolaListener listener = new ProgramPrinter();//phase1
        //walker.walk(listener , tree);                 //un_comment to run phase1


        MoolaListener listener2 = new SetSymbolTable2(); //phase2
        //walker.walk(listener2 , tree);

        MoolaListener listener3 = new phase3SetSymbolTable(); //phase3
        walker.walk(listener3 , tree2);

        MoolaListener listener4 = new ErrorChatcher(); //phase3
        walker2.walk(listener4 , tree2);


        //MoolaListener listener5 = new SetSymbolTable(); //phase2 fail
        //walker2.walk(listener5 , tree);
    }
}
