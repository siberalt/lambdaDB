package application;

import sql_syntax_analyzer.analyzer.Alphabet;
import sql_syntax_analyzer.analyzer.SqlSyntaxAnalyzer;
import syntax_analyzer.input_adapters.FileInput;
import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.Scanner;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.ArrayList;

public class Application {

    public static void main(String[] args) throws
            UnexpectedEndOfInputException,
            UnknownLexemException,
            IOException, GeneralScannerException {
        FileInput input = new FileInput("C:\\Users\\1\\eclipse-workspace\\BananaDB\\src\\application\\input.dat");

        Scanner scanner = new Scanner(input);
        scanner.setIgnoredLexemeTypes(new Alphabet[]{
                Alphabet.T_NEUTRAL,
                Alphabet.T_COMMENT
        });
        scanner.setLexemeRecognizers(SqlSyntaxAnalyzer.lexemeRecognizers);
        ArrayList<Terminal> lexemes = scanner.retrieveTerminals();

        for (Terminal lexeme : lexemes) {
            System.out.print(lexeme.toString());
            System.out.print('\n');
        }
    }

}
