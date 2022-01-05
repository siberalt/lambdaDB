import org.junit.Before;
import org.junit.Test;
import sql_syntax_analyzer.analyzer.SqlSyntaxAnalyzer;
import syntax_analyzer.analyzator.exceptions.UnexpectedLexemeException;
import syntax_analyzer.compilator.DummyCompiler;
import syntax_analyzer.input_adapters.FileInput;
import syntax_analyzer.input_adapters.StringInput;
import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.Scanner;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class SyntaxAnalyzerTests {
    protected SqlSyntaxAnalyzer analyzer;

    protected final String SYNTAX_ANALYZER_RESOURCES_DIR = "C:\\projects\\lambdaDB\\src\\test\\resources\\syntaxAnalyzerData\\";

    protected Scanner scanner;

    @Before
    public void init() {
        scanner = new Scanner();
        analyzer = new SqlSyntaxAnalyzer(scanner);
        analyzer.setCompiler(new DummyCompiler());
    }

    @Test
    public void testSimpleQuery() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new StringInput("SELECT * FROM t_table"));

        analyzer.analyze();
    }

    @Test
    public void testSelect1Query() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "select1.dat"));

        analyzer.analyze();
    }

    @Test
    public void testSelect2Query() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "select2.dat"));

        boolean exceptionThrown = false;

        try {
            analyzer.analyze();
        } catch (UnexpectedLexemeException exception) {
            exceptionThrown = true;
            System.out.println(exception.getMessage());
            Terminal actual = exception.getActual();
            assertSame(2, actual.getLine());
            assertSame(7, actual.getColumn());
        }

        assertTrue("Exception has not been thrown", exceptionThrown);
    }
}
