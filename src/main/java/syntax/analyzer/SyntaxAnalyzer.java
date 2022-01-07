package syntax.analyzer;

import sql.syntax.analyzer.Alphabet;
import syntax.analyzer.exceptions.InvalidConfigException;
import syntax.analyzer.exceptions.UnexpectedLexemeException;
import syntax.compilator.CompilerInterface;
import syntax.lang.*;
import syntax.lang.letter.LetterInterface;
import syntax.lang.letter.LetterType;
import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;
import syntax.scanner.Scanner;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SyntaxAnalyzer {
	protected Scanner scanner = null;
	
	protected CompilerInterface compiler = null;
	
	protected LinkedList<LetterInterface> lettersStack = null;
	
	protected LetterManagerInterface langManager = null;

	protected RuleManager ruleManager;
	
	public SyntaxAnalyzer(Scanner scanner, CompilerInterface compiler) {
		this.scanner = scanner;
		this.compiler = compiler;
	}

	public SyntaxAnalyzer() {}

	public SyntaxAnalyzer setCompiler(CompilerInterface compiler) {
		this.compiler = compiler;

		return this;
	}

	public SyntaxAnalyzer setScanner(Scanner scanner) {
		this.scanner = scanner;

		return this;
	}

	public SyntaxAnalyzer setLangManager(LetterManagerInterface generator) {
		langManager = generator;
		
		return this;
	}
	
	public SyntaxAnalyzer setRuleManager(RuleManager ruleManager) {
		this.ruleManager = ruleManager;
		
		return this;
	}
	
	public SyntaxAnalyzer setStartNotTerminal(Alphabet notTerminal) {
		lettersStack = new LinkedList<>();
		lettersStack.add(langManager.generate(notTerminal));
		
		return this;
	}
	
	public void analyze() throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
		if(null == scanner) {
			throw new InvalidConfigException("Scanner is not configured");
		}
		
		if(null == compiler) {
			throw new InvalidConfigException("Compiler is not configured");
		}
		
		if(null == lettersStack) {
			throw new InvalidConfigException("Start not-terminal is not set");
		}
		
		if(null == ruleManager) {
			throw new InvalidConfigException("Rule manager is not set");
		}
		
		if(null == langManager) {
			throw new InvalidConfigException("Language manager is not configured");
		}

		compiler.init();
		Terminal curTerminal = scanner.scan();
		
		while(null != curTerminal) {
			LetterInterface topObject = lettersStack.getLast();
			
			if(LetterType.TERMINAL == topObject.getType()) {
				if(curTerminal.getId() == topObject.getId()) {
					compiler.processTerminal(curTerminal);
					lettersStack.removeLast();
					curTerminal = scanner.scan();
				}else {
					throw new UnexpectedLexemeException((Terminal) topObject, curTerminal);
				}
				
			}else if(LetterType.NOTERMINAL == topObject.getType()) {
				ruleManager.getTerminalScanner().setFirstScanTerminal(curTerminal);
				List<LetterInterface> ruleLangObjects = ruleManager.generateRule(topObject.getId());
				lettersStack.removeLast();
				
				if(null != ruleLangObjects) {
					lettersStack.addAll(ruleLangObjects);
				}
				
			}else if(LetterType.OPERATING == topObject.getType()) {
				compiler.processOperating((Operating) topObject);
				lettersStack.removeLast();
			}
		}
	}
}
