package syntax_analyzer.analyzator.exceptions;

import environment.Environment;
import syntax_analyzer.lang_objects.Terminal;

public class UnexpectedLexemeException extends SyntaxAnalyzerException {
    protected Terminal actual;

    protected Terminal expected;

    public UnexpectedLexemeException(Terminal expected, Terminal actual) {
        super(Environment.isTest()
            ?   String.format(
                    "Unexpected lexeme. Expected: '%s', got '%s' at line %s in column %s",
                    expected.getId().name(),
                    actual.getId().name(),
                    actual.getLine(),
                    actual.getColumn()
                )
            :   String.format("Unexpected lexeme. Expected: '%s', got '%s' at line %s in column %s",
                    expected.getView(),
                    actual.getView(),
                    actual.getLine(),
                    actual.getColumn()
                )
        );
        this.expected = expected;
        this.actual = actual;
    }

    public Terminal getActual() {
        return actual;
    }

    public Terminal getExpected() {
        return expected;
    }
}
