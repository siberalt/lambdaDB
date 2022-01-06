package syntax.analyzer.exceptions;

public class SyntaxAnalyzerException extends RuntimeException{

    public SyntaxAnalyzerException(String message){
        super("Syntax analyzer has thrown exception: " + message);
    }
}
