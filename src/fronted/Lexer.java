package fronted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Lexer {
    private static final Map<String,TokenType> reserveMap = new HashMap<>();
    private static final Map<String,TokenType> operaterMap = new HashMap<>();

    static {
        reserveMap.put("const",TokenType.CONSTTK);
        reserveMap.put("int",TokenType.INTTK);
        reserveMap.put("static",TokenType.STATICTK);
        reserveMap.put("break",TokenType.BREAKTK);
        reserveMap.put("continue",TokenType.CONTINUETK);
        reserveMap.put("if",TokenType.IFTK);
        reserveMap.put("main",TokenType.MAINTK);
        reserveMap.put("else",TokenType.ELSETK);
        reserveMap.put("for",TokenType.FORTK);
        reserveMap.put("return",TokenType.RETURNTK);
        reserveMap.put("void",TokenType.VOIDTK);
        reserveMap.put("printf",TokenType.PRINTFTK);

        operaterMap.put("+",TokenType.PLUS);
        operaterMap.put("-",TokenType.MINU);
        operaterMap.put("*",TokenType.MULT);
        operaterMap.put("%",TokenType.MOD);
        operaterMap.put(";",TokenType.SEMICN);
        operaterMap.put(",",TokenType.COMMA);
        operaterMap.put("(",TokenType.LPARENT);
        operaterMap.put(")",TokenType.RPARENT);
        operaterMap.put("[",TokenType.LBRACK);
        operaterMap.put("]",TokenType.RBRACK);
        operaterMap.put("{",TokenType.LBRACE);
        operaterMap.put("}",TokenType.RBRACE);


    }

    private String inputString;
    //private int errorNum = 0;
    private ArrayList<Token> tokens = new ArrayList<>();
    //private ArrayList<ErrorToken> errorTokens = new ArrayList<>();
    private TokenType type;
    private int line = 1;
    private StringBuilder content = new StringBuilder();
    private int pos = 0;


    public Lexer(String input){
        this.inputString = input;
        while(hastoken()){
            tokens.add(new Token(type,content.toString(),line));
        }
        for(Token token:tokens){
            System.out.println(token);
        }
    }

//    public int getErrorNum(){
//        return errorNum;
//    }

    public ArrayList<fronted.Token> getTokens(){
        return tokens;
    }

//    public ArrayList<ErrorToken> getErrorTokens(){
//        return errorTokens;
//    }

    public boolean hastoken(){
        while(pos < inputString.length() && isBlank()){
            pos++;
        }
        if(pos >= inputString.length()){
            return false;
        }
        content.setLength(0);
        if(Character.isLetter(inputString.charAt(pos)) || inputString.charAt(pos) == '_'){
            while(pos < inputString.length() && (Character.isLetter(inputString.charAt(pos)) || Character.isDigit(inputString.charAt(pos))|| inputString.charAt(pos) == '_')){
                content.append(inputString.charAt(pos));
                pos++;
            }
            type = reserveMap.getOrDefault(content.toString(), TokenType.IDENFR);
        } else if(Character.isDigit(inputString.charAt(pos))){
            while(pos < inputString.length() && Character.isDigit(inputString.charAt(pos))){
                content.append(inputString.charAt(pos));
                pos++;
            }
            type = TokenType.INTCON;
        } else if(inputString.charAt(pos) == '&'|| inputString.charAt(pos) == '|'){
            getAndOrOr();
        }else if(inputString.charAt(pos) == '\"'){
            getStringConst();
        } else if(inputString.charAt(pos) == '/'){
            return getDivOrCmt();
        }else if(inputString.charAt(pos) == '>' ||inputString.charAt(pos) == '<' ||inputString.charAt(pos) == '=' ||inputString.charAt(pos) == '!'){
            getAgnOrCmp();
        }else if(operaterMap.containsKey(Character.toString(inputString.charAt(pos)))){
            content.append(inputString.charAt(pos));
            type = operaterMap.get(Character.toString(inputString.charAt(pos)));
            pos++;
        }
        return true;
    }

    private boolean isBlank(){
        if(inputString.charAt(pos) == ' '||inputString.charAt(pos) == '\t' || inputString.charAt(pos) == '\r'){
            return true;
        }else if(inputString.charAt(pos) == '\n'){
            line++;
            return true;
        }
        return false;
    }

    private void getAndOrOr(){
        if(inputString.charAt(pos) == '&'){
            content.append(inputString.charAt(pos));
            pos++;
            type = TokenType.AND;
            if(pos < inputString.length() && inputString.charAt(pos) == '&'){
                content.append(inputString.charAt(pos));
                pos++;
            }else{
                ErrorHandler.addError(new ErrorToken(line,"a"));
            }
        }else{
            content.append(inputString.charAt(pos));
            pos++;
            type = TokenType.OR;
            if(pos < inputString.length() && inputString.charAt(pos) == '|'){
                content.append(inputString.charAt(pos));
                pos++;
            }else{
                ErrorHandler.addError(new ErrorToken(line,"a"));
            }
        }
    }

    private void getStringConst(){
        type = TokenType.STRCON;
        content.append(inputString.charAt(pos));
        pos++;
        while(pos < inputString.length() && inputString.charAt(pos) != '\"'){
            if(inputString.charAt(pos) == '\\'){
                content.append(inputString.charAt(pos));
                pos++;
            }
            if(pos < inputString.length()){
                content.append(inputString.charAt(pos));
                pos++;
            }
        }
        if(pos < inputString.length()){
            content.append(inputString.charAt(pos));
            pos++;
        }
    }

    private boolean getDivOrCmt(){
        content.append(inputString.charAt(pos));
        pos++;
        if(pos < inputString.length()){
            if(inputString.charAt(pos) == '/'){
                pos++;
                while(pos < inputString.length() && inputString.charAt(pos) != '\n'){
                    pos++;
                }
                return hastoken();
            }else if(inputString.charAt(pos) == '*'){
                pos++;
                while(pos < inputString.length()){
                    if(inputString.charAt(pos) == '*'){
                        pos++;
                        if(pos >= inputString.length()){
                            break;
                        }
                        if(inputString.charAt(pos) == '/'){
                            pos++;
                            break;
                        }
                        continue;
                    } else if(inputString.charAt(pos) == '\n'){
                        line++;
                    }
                    pos++;
                }
                return hastoken();
            }else{
                type = TokenType.DIV;
                return true;
            }
        }else{
            type = TokenType.DIV;
            return true;
        }
    }

    private void getAgnOrCmp(){
        if(pos + 1 < inputString.length() && inputString.charAt(pos + 1) == '='){
            char ch = inputString.charAt(pos);
            content.append(ch);
            pos++;
            content.append(inputString.charAt(pos));
            pos++;
            switch (ch){
                case '!' -> type = TokenType.NEQ;
                case '=' -> type = TokenType.EQL;
                case '<' -> type = TokenType.LEQ;
                case '>' -> type = TokenType.GEQ;
            }
        }else{
            char ch = inputString.charAt(pos);
            content.append(ch);
            pos++;
            switch (ch){
                case '!' -> type = TokenType.NOT;
                case '=' -> type = TokenType.ASSIGN;
                case '<' -> type = TokenType.LSS;
                case '>' -> type = TokenType.GRE;
            }
        }
    }

}
