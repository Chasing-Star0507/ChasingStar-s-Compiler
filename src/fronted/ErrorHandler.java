package fronted;

import java.util.ArrayList;
import java.util.Comparator;

public class ErrorHandler {
    private static ArrayList<ErrorToken> errorTokens = new ArrayList<>();

    public static void addError(ErrorToken errorToken) {
        errorTokens.add(errorToken);
    }

    public static ArrayList<ErrorToken> getErrorTokens() {
        errorTokens.sort(Comparator.comparingInt(ErrorToken::getLineNum));
        return errorTokens;
    }
}
