package syntax.analyzer;

import syntax.lang.letter.Terminal;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OrRule extends AbstractRule implements RuleInterface {
	protected LinkedList<List<Enum<? extends Enum<?>>>> operands;

	protected boolean throwException = true;

	@SafeVarargs
	public OrRule(Enum<? extends Enum<?>> ruleId, Enum<? extends Enum<?>> ...operands) {
		this.operands = new LinkedList<>();
		
		for(Enum<? extends Enum<?>> operand: operands) {
			LinkedList<Enum<? extends Enum<?>>> list = new LinkedList<>();
			list.add(operand);
			this.operands.add(list);
		}
		
		this.ruleId = ruleId;
	}
	
	@SafeVarargs
	public OrRule(Enum<? extends Enum<?>> ruleId, Enum<? extends Enum<?>>[] ...operands){
		this.operands = new LinkedList<>();
		
		for(Enum<? extends Enum<?>>[] operand: operands) {

			LinkedList<Enum<? extends Enum<?>>> list = new LinkedList<>(Arrays.asList(operand));
			
			this.operands.add(list);
		}
		
		this.ruleId = ruleId;
	}

	@Override
	public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
		int maxIndex = -1, curIndex;
		ArrayList<Terminal> scannedTerminals = new ArrayList<>();
		List<Enum<? extends Enum<?>>> bestMatchOperand = null;
		
		for(List<Enum<? extends Enum<?>>> items: operands) {
			curIndex = 0;
			boolean isMatched = false;
			
			for(Enum<? extends Enum<?>> item: items) {
				if(curIndex > maxIndex) {
					scannedTerminals.add(scanner.scan());
				}
				
				if(!(isMatched = item.equals(scannedTerminals.get(curIndex).getId()))) {
					break;
				}
				
				curIndex++;
			}
			
			if(isMatched && curIndex >= maxIndex) {
				maxIndex = curIndex;
				bestMatchOperand = items;
			}
		}

		if(null == bestMatchOperand && throwException){
			ruleManager.throwUnexpectedLexemes(operands, scannedTerminals);
		}
		
		return bestMatchOperand;
	}

	public OrRule setThrowException(boolean throwException) {
		this.throwException = throwException;

		return this;
	}
}
