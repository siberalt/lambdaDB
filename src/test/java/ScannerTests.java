import org.junit.Before;
import org.junit.Test;
import lambdaDB.syntax.analyzer.Alphabet;
import lambdaDB.syntax.analyzer.SqlSyntaxAnalyzer;
import syntax.scanner.input.FileInput;
import syntax.scanner.input.InputInterface;
import syntax.scanner.input.StringInput;
import syntax.lang.letter.Terminal;
import syntax.scanner.Scanner;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

public class ScannerTests {
    private Scanner scanner;

    protected final String SCANNER_DATA_DIR = "C:\\projects\\lambdaDB\\src\\test\\resources\\scannerData\\";

    @Before
    public void init() {
        scanner = new Scanner();
        scanner.setIgnoredLexemeTypes(new Alphabet[]{
            Alphabet.T_NEUTRAL,
            Alphabet.T_COMMENT
        });
        scanner.setLexemeRecognizers(SqlSyntaxAnalyzer.lexemeRecognizers);
    }

    public void test(List<Terminal> actualTerminals, List<Alphabet> expectedTypes) {
        Iterator<Alphabet> iterator = expectedTypes.iterator();

        for (Terminal terminal : actualTerminals) {
            System.out.print(terminal.toString());
            System.out.print('\n');
            assertSame(iterator.next(), terminal.getId());
        }

        System.out.println();
    }

    public void testQuery(InputInterface input, Alphabet[] expectedLexemes) throws GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException, IOException {
        scanner.setInput(input);
        test(scanner.retrieveTerminals(), Arrays.asList(expectedLexemes));
    }

    @Test
    public void testSimpleQuery() throws GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException, IOException {
        String data = "SELECT * FROM t_table";
        testQuery(
            new StringInput(data),
            new Alphabet[]{
                Alphabet.T_SELECT,
                Alphabet.T_MULT,
                Alphabet.T_FROM,
                Alphabet.T_ID
            }
        );
    }

    @Test
    public void testComplexQuery() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        testQuery(
            new FileInput(SCANNER_DATA_DIR + "input.dat"),
            new Alphabet[]{
                Alphabet.T_SELECT,
                Alphabet.T_ID,
                Alphabet.T_COMMA,
                Alphabet.T_ID,
                Alphabet.T_COMMA,
                Alphabet.T_FROM,
                Alphabet.T_ID,
                Alphabet.T_WHERE,
                Alphabet.T_ID,
                Alphabet.T_EQUAL,
                Alphabet.T_CONST_INT,
                Alphabet.T_SEMICOLON,
            });
    }

    @Test
    public void testInvalidStartLexeme() throws IOException, GeneralScannerException, UnexpectedEndOfInputException {
        try {
            testQuery(new FileInput(SCANNER_DATA_DIR + "input2.dat"), new Alphabet[]{});
        } catch (UnknownLexemException exception) {
            System.out.println(exception.getMessage());
            assertSame(1, exception.getLine());
            assertSame(1, exception.getPosition());
            assertEquals("@", exception.getUnknownLexeme());
        }
    }

    @Test
    public void testInvalidEndLexeme() throws IOException, GeneralScannerException, UnexpectedEndOfInputException {
        try {
            testQuery(new FileInput(SCANNER_DATA_DIR + "input1.dat"), new Alphabet[]{});
        } catch (UnknownLexemException exception) {
            System.out.println(exception.getMessage());
            assertSame(6, exception.getLine());
            assertSame(14, exception.getPosition());
            assertEquals("@", exception.getUnknownLexeme());
        }
    }

    @Test
    public void testInvalidMiddleLexeme() throws IOException, GeneralScannerException, UnexpectedEndOfInputException {
        String data = "" +
            "SELECT * \n" +
            "FROM% t_table";
        try {
            testQuery(new StringInput(data), new Alphabet[]{});
        } catch (UnknownLexemException exception) {
            System.out.println(exception.getMessage());
            assertSame(2, exception.getLine());
            assertSame(5, exception.getPosition());
            assertEquals("%", exception.getUnknownLexeme());
        }
    }

    @Test
    public void testInvalidLexemeInCreateQuery() throws IOException, GeneralScannerException, UnexpectedEndOfInputException {
        try {
            testQuery(new FileInput(SCANNER_DATA_DIR + "create1.dat"), new Alphabet[]{});
        } catch (UnknownLexemException exception) {
            System.out.println(exception.getMessage());
            assertSame(5, exception.getLine());
            assertSame(5, exception.getPosition());
            assertEquals("^", exception.getUnknownLexeme());
        }
    }

    @Test
    public void testSelect1() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        testQuery(
            new FileInput("C:\\projects\\lambdaDB\\src\\test\\resources\\syntaxAnalyzerData\\select1.dat"),
            new Alphabet[]{
                Alphabet.T_SELECT,
                Alphabet.T_ID,
                Alphabet.T_COMMA,
                Alphabet.T_ID,
                Alphabet.T_ADD,
                Alphabet.T_CONST_INT,
                Alphabet.T_COMMA,
                Alphabet.T_ID,
                Alphabet.T_DOT,
                Alphabet.T_ID,
                Alphabet.T_SUB,
                Alphabet.T_CONST_INT,
                Alphabet.T_FROM,
                Alphabet.T_ID
            }
        );
    }

    @Test
    public void testSimpleScannerBuffer() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        LinkedList<Terminal> actualTerminals = new LinkedList<>();
        List<Alphabet> expectedTerminals = Arrays.asList(
            Alphabet.T_SELECT,
            Alphabet.T_MULT,
            Alphabet.T_FROM,
            Alphabet.T_MULT,
            Alphabet.T_FROM
        );

        scanner.setInput(new StringInput("SELECT * FROM t_table"));
        actualTerminals.add(scanner.scan());
        scanner.savePosition();
        actualTerminals.add(scanner.scan());
        actualTerminals.add(scanner.scan());
        scanner.restoreSavedPosition();
        actualTerminals.add(scanner.scan());
        actualTerminals.add(scanner.scan());

        Iterator<Alphabet> iterator = expectedTerminals.iterator();

        for (Terminal terminal : actualTerminals) {
            System.out.print(terminal.toString());
            System.out.print('\n');
            assertSame(iterator.next(), terminal.getId());
        }

        System.out.println();
    }

    @Test
    public void testMediumScannerBuffer() throws GeneralScannerException, UnexpectedEndOfInputException, IOException, UnknownLexemException {
        LinkedList<Terminal> actualTerminals = new LinkedList<>();

        scanner.setInput(new StringInput("SELECT id , * FROM t_table"));

        actualTerminals.add(scanner.scan());
        scanner.savePosition();

        actualTerminals.add(scanner.scan());

        scanner.restoreSavedPosition();

        actualTerminals.add(scanner.scan());
        actualTerminals.add(scanner.scan());
        actualTerminals.add(scanner.scan());

        test(actualTerminals, Arrays.asList(
            Alphabet.T_SELECT,
            Alphabet.T_ID,
            Alphabet.T_ID,
            Alphabet.T_COMMA,
            Alphabet.T_MULT
        ));
    }

    @Test
    public void testComplexScannerBuffer() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        LinkedList<Terminal> actualTerminals = new LinkedList<>();
        List<Alphabet> expectedTerminals = Arrays.asList(
            Alphabet.T_SELECT,
            Alphabet.T_ID,
            Alphabet.T_COMMA,
            Alphabet.T_COMMA,
            Alphabet.T_ID
        );

        scanner.setInput(new StringInput("SELECT id , id2 FROM t_table"));
        actualTerminals.add(scanner.scan());
        scanner.savePosition();

        actualTerminals.add(scanner.scan());
        scanner.savePosition();

        actualTerminals.add(scanner.scan());
        scanner.restoreSavedPosition();

        actualTerminals.add(scanner.scan());
        scanner.restoreSavedPosition();

        actualTerminals.add(scanner.scan());

        Iterator<Alphabet> iterator = expectedTerminals.iterator();

        for (Terminal terminal : actualTerminals) {
            System.out.print(terminal.toString());
            System.out.print('\n');
            assertSame(iterator.next(), terminal.getId());
        }

        System.out.println();
    }
}
