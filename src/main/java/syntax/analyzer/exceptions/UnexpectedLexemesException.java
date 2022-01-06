package syntax.analyzer.exceptions;

import syntax.lang.letter.Terminal;

import java.util.List;

public class UnexpectedLexemesException extends RuntimeException {

    List<Terminal> actualTerminals;

    List<List<Enum<? extends Enum<?>>>> expectedLexemes;

    public UnexpectedLexemesException(
        String message,
        List<List<Enum<? extends Enum<?>>>> expectedLexemes,
        List<Terminal> actualTerminals){
        super(message);
        this.expectedLexemes = expectedLexemes;
        this.actualTerminals = actualTerminals;
    }

    public List<Terminal> getActualTerminals() {
        return actualTerminals;
    }

    public List<List<Enum<? extends Enum<?>>>> getExpectedLexemes() {
        return expectedLexemes;
    }

    public int getLine(){
        return actualTerminals.iterator().next().getLine();
    }

    public int getColumn(){
        return actualTerminals.iterator().next().getColumn();
    }
}
