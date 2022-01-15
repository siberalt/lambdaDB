import lambdaDB.syntax.analyzer.SqlSyntaxAnalyzer;
import lambdaDB.syntax.compiler.SqlCompiler;
import org.junit.Before;
import org.junit.Test;
import syntax.compilator.CompilerInterface;
import syntax.compilator.input.InputInterface;
import syntax.compilator.output.CharOutput;
import syntax.compilator.output.ConsoleOutput;
import syntax.compilator.output.OutputInterface;
import syntax.compilator.output.command.writer.CommandWriterInterface;
import syntax.compilator.output.command.writer.HumanViewWriter;
import syntax.compilator.output.command.writer.MachineViewWriter;
import syntax.scanner.Scanner;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;
import syntax.scanner.input.FileInput;

import java.io.IOException;
import static org.junit.Assert.*;

public class CompilerTests {
    protected final String COMPILER_DATA_DIR = "C:\\projects\\lambdaDB\\src\\test\\resources\\compilerData\\";

    protected Scanner scanner;

    protected SqlSyntaxAnalyzer analyzer;

    protected CompilerInterface compiler;

    @Before
    public void init() {
        compiler = new SqlCompiler();
        scanner = new Scanner();
        analyzer = new SqlSyntaxAnalyzer(scanner);
        analyzer.setCompiler(compiler);
    }

    @Test
    public void initialTest() throws IOException, GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException {
        scanner.setInput(new FileInput(COMPILER_DATA_DIR + "create.dat"));
        analyzer.analyze();

        OutputInterface output = new ConsoleOutput();
        CommandWriterInterface humanViewWriter = new HumanViewWriter(output).setOutputCommandId(true);
        CommandWriterInterface machineViewWriter = new MachineViewWriter(output);

        compiler.compile(humanViewWriter);
        System.out.println("---------------------------------");
        compiler.compile(machineViewWriter);
    }

    @Test
    public void testCreate() throws GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException, IOException {
        scanner.setInput(new FileInput(COMPILER_DATA_DIR + "create.dat"));
        analyzer.analyze();

        CharOutput output = new CharOutput();
        CommandWriterInterface machineViewWriter = new MachineViewWriter(output);

        compiler.compile(machineViewWriter);

        InputInterface input = output.getInput();

        /*
            0) CREATE_TABLE(0) user
            1) CREATE_TABLE_FIELD(1) name 0
            2) CREATE_TABLE_FIELD_TYPE_SIZE(7) 230
            3) CREATE_TABLE_FIELD(1) surname 0
            4) CREATE_TABLE_FIELD_NOT_NULL(9)
            5) CREATE_TABLE_FIELD(1) age 2
            6) CREATE_TABLE_FIELD_DEFAULT_INT(3) 18
            7) CREATE_TABLE_FIELD(1) id 2
            8) CREATE_TABLE_FIELD_PRIMARY_KEY(5)
            9) CREATE_TABLE_FIELD_AUTOINCREMENT(6)
            10) CREATE_TABLE_FIELD(1) is_male 1
            11) CREATE_TABLE_FIELD_DEFAULT_NULL(4)
            12) CREATE_TABLE_COMMIT(8)
         */

        assertEquals(0, input.readInt());
        assertEquals("user", input.readString());
        assertEquals(1, input.readInt());
        assertEquals("name", input.readString());
        assertEquals(0, input.readInt());
        assertEquals(7, input.readInt());
        assertEquals(230, input.readInt());
        assertEquals(1, input.readInt());
        assertEquals("surname", input.readString());
        assertEquals(0, input.readInt());
        assertEquals(9, input.readInt());
        assertEquals(1, input.readInt());
        assertEquals("age", input.readString());
        assertEquals(2, input.readInt());
        assertEquals(3, input.readInt());
        assertEquals(18, input.readInt());
        assertEquals(1, input.readInt());
    }
}
