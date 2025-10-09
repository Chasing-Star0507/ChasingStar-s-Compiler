import fronted.*;
import error.*;
import fronted.CompUnit;
import fronted.ErrorHandler;
import fronted.ErrorToken;
import fronted.Token;
import fronted.syntax.*;


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
        Parser parser = new Parser(tokens);
        CompUnit compUnit =parser.getCompUnit();
        ArrayList<ErrorToken> errorTokens = ErrorHandler.getErrorTokens();
        //compUnit.print();
        if(errorTokens.isEmpty()){
            PrintStream origin = System.out;
            System.setOut(new PrintStream("parser.txt"));
            compUnit.print();
            System.setOut(origin);
        }else{
            PrintStream origin = System.out;
            System.setOut(new PrintStream("error.txt"));
            //System.out.print(errorTokens.size());
            //ToDo 这里要对errorTokens排序，行号先，错误码用字典序
            for (ErrorToken errorToken:errorTokens) {
                System.out.println(errorToken);
            }
            System.setOut(origin);
        }
    }
}
