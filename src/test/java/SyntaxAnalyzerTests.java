import org.junit.Before;
import org.junit.Test;
import sql.syntax.analyzer.Alphabet;
import sql.syntax.analyzer.SqlSyntaxAnalyzer;
import syntax.analyzer.exceptions.UnexpectedLexemesException;
import syntax.compilator.DummyCompiler;
import syntax.input_adapters.FileInput;
import syntax.input_adapters.StringInput;
import syntax.scanner.Scanner;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
        } catch (UnexpectedLexemesException exception) {
            exceptionThrown = true;
            System.out.println(exception.getMessage());
            assertSame(2, exception.getLine());
            assertSame(7, exception.getColumn());
        }

        assertTrue("Exception has not been thrown", exceptionThrown);
    }

    @Test
    public void testSelect3Query() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "select3.dat"));

        analyzer.analyze();
    }

    @Test
    public void testSelect4Query() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "select4.dat"));

        boolean exceptionThrown = false;

        try {
            analyzer.analyze();
        } catch (UnexpectedLexemesException exception) {
            exceptionThrown = true;
            System.out.println(exception.getMessage());
            assertSame(4, exception.getLine());
            assertSame(11, exception.getColumn());
        }

        assertTrue("Exception has not been thrown", exceptionThrown);
    }

    @Test
    public void testInsert1Query() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "insert1.dat"));

        analyzer.analyze();
    }

    @Test
    public void testInsert2Query() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "insert2.dat"));

        analyzer.analyze();
    }

    @Test
    public void testInvalidQuery() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "invalid.dat"));

        boolean exceptionThrown = false;

        try {
            analyzer.analyze();
        } catch (UnexpectedLexemesException exception) {
            exceptionThrown = true;
            System.out.println(exception.getMessage());
            LinkedList<Enum<? extends Enum<?>>> expectedLexemes = new LinkedList<>(
                List.of(
                    Alphabet.NT_SELECT,
                    Alphabet.NT_DELETE,
                    Alphabet.NT_UPDATE,
                    Alphabet.NT_INSERT,
                    Alphabet.NT_CREATE_TABLE
                )
            );

            for(List<Enum<? extends Enum<?>>> sequence: exception.getExpectedLexemes()){
                boolean contains = false;

                for(Enum<? extends Enum<?>> expected: expectedLexemes){
                    if(sequence.contains(expected)){
                        contains = true;
                        break;
                    }
                }

                assertTrue("Lexeme sequence" + sequence.toString() + " is not presented in expected", contains);
            }
        }

        assertTrue("Exception has not been thrown", exceptionThrown);
    }

    @Test
    public void testCreateQuery() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(SYNTAX_ANALYZER_RESOURCES_DIR + "create.dat"));

        analyzer.analyze();
    }
}
