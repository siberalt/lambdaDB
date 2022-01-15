package application;

import lambdaDB.syntax.analyzer.Alphabet;
import lambdaDB.syntax.analyzer.SqlSyntaxAnalyzer;
import syntax.scanner.input.FileInput;
import syntax.lang.letter.Terminal;
import syntax.scanner.Scanner;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

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
