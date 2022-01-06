package syntax.scanner.helpers;

public interface CharHelperInterface {
    default boolean isLetter(char letter){
        return  CharHelper.isLetter(letter);
    }

    default boolean isSmallLetter(char letter) {
        return CharHelper.isSmallLetter(letter);
    }

    default boolean isBigLetter(char letter) {
        return CharHelper.isBigLetter(letter);
    }

    default boolean isDigit(char letter) {
        return CharHelper.isDigit(letter);
    }

    default boolean isUnderline(char letter){
        return CharHelper.isUnderline(letter);
    }

    default boolean isSign(char letter) {
        return CharHelper.isSign(letter);
    }

    default boolean inArray(char letter, String array) {
        return CharHelper.inArray(letter, array);
    }

    default char toLowerCase(char ch) {
        return CharHelper.toLowerCase(ch);
    }
}
