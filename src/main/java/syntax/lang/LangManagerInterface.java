package syntax.lang;

import syntax.lang.letter.LetterInterface;
import syntax.lang.letter.Terminal;

public interface LangManagerInterface {

	LetterInterface generate(Enum<? extends Enum<?>> description);

	Terminal generateTerminal(Enum<? extends Enum<?>> description);
	
	boolean isTerminalType(Enum<? extends Enum<?>> type);
	
	boolean isNotTerminalType(Enum<? extends Enum<?>> type);
	
	boolean isOperatingType(Enum<? extends Enum<?>> type);
}
