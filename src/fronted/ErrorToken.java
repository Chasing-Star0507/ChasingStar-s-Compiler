package fronted;

public class ErrorToken {
    private int lineNum;
    private String type;

    public ErrorToken(int lineNum,String type){
        this.lineNum = lineNum;
        this.type = type;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String toString(){
        return lineNum + " " + type;
    }
}
