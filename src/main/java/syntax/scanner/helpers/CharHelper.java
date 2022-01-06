package syntax.scanner.helpers;

public class CharHelper {
	
	public static boolean isSmallLetter(char letter) {
		return 'a' <= letter && letter <= 'z';
	}
	
	public static boolean isBigLetter(char letter) {
		return 'A' <= letter && letter <= 'Z';
	}
	
	public static boolean isDigit(char letter) {
		return '9' >= letter && letter >= '0';
	}
	
	public static boolean isLetter(char letter) {
		return isSmallLetter(letter) || isBigLetter(letter);
	}
	
	public static boolean isSign(char letter) {
		return letter == '+' || letter == '-';
	}
	
	public static boolean inArray(char letter, String array) {
		
		for(char item: array.toCharArray()) {
			if(letter == item) {
				return true;
			}
		}
		
		return false;
	}
	
	public static char toLowerCase(char ch) {
		return Character.toLowerCase(ch);
	}

    public static boolean isUnderline(char letter) {
		return '_' == letter;
    }
}
