import fronted.*;
import middle.IRBuilder;
import middle.Module;
import middle.Visitor;
import optimize.Optimizer;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Compiler {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("testfile.txt"));
        Lexer lexer = new Lexer(input);
        ArrayList<Token> tokens = lexer.getTokens();
//        PrintStream origin = System.out;
//        System.setOut(new PrintStream("lexer.txt"));
//        for(Token token : tokens){
//            System.out.println(token);
//        }
//        System.setOut(origin);
        Parser parser = new Parser(tokens);
        CompUnit compUnit = parser.getCompUnit();
//        PrintStream origin = System.out;
//        System.setOut(new PrintStream("parser.txt"));
//        compUnit.print();
//        System.setOut(origin);
        Visitor visitor = new Visitor(compUnit);
        visitor.visit();
        ArrayList<ErrorToken> errorTokens = ErrorHandler.getErrorTokens();
        if (errorTokens.isEmpty()) {
            //ToDo 记得把main的输出去掉
            IRBuilder ir = new IRBuilder(compUnit);
            ir.build();
//            PrintStream origin = System.out;
//            System.setOut(new PrintStream("llvm_ir.txt"));
//            Module.getINSTANCE().print();
//            System.setOut(origin);
            Optimizer optimizer = new Optimizer(Module.getINSTANCE());
            optimizer.optimize();
//            MipsBuilder mipsBuilder = new MipsBuilder(Module.getINSTANCE());
//            mipsBuilder.build();
//            PrintStream origin = System.out;
//            System.setOut(new PrintStream("mips.txt"));
//            MipsFile.getINSTANCE().print();
//            System.setOut(origin);
        } else {
            PrintStream origin = System.out;
            System.setOut(new PrintStream("error.txt"));
            //System.out.print(errorTokens.size());
            //ToDo 这里要对errorTokens排序，行号先，错误码用字典序
            for (ErrorToken errorToken : errorTokens) {
                System.out.println(errorToken);
            }
            System.setOut(origin);
        }
    }
}
