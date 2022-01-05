package syntax_analyzer.scanner.helpers;

public interface CharHelperInterface {
    public default boolean isLetter(char letter){
        return  CharHelper.isLetter(letter);
    }

    public default boolean isSmallLetter(char letter) {
        return CharHelper.isSmallLetter(letter);
    }

    public default boolean isBigLetter(char letter) {
        return CharHelper.isBigLetter(letter);
    }

    public default boolean isDigit(char letter) {
        return CharHelper.isDigit(letter);
    }

    public default boolean isUnderline(char letter){
        return CharHelper.isUnderline(letter);
    }

    public default boolean isSign(char letter) {
        return CharHelper.isSign(letter);
    }

    public default boolean inArray(char letter, String array) {
        return CharHelper.inArray(letter, array);
    }

    public default char toLowerCase(char ch) {
        return CharHelper.toLowerCase(ch);
    }
}
