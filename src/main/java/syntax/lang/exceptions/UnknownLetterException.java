package syntax.lang.exceptions;

public class UnknownLetterException extends RuntimeException{
    public UnknownLetterException(Enum<? extends Enum<?>> letter){
        super("Lang exception: type of " + letter.name() + " is no defined");
    }
}
