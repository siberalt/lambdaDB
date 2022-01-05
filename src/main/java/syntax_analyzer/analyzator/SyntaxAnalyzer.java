package syntax_analyzer.analyzator;

import sql_syntax_analyzer.analyzer.Alphabet;
import syntax_analyzer.analyzator.exceptions.InvalidConfigException;
import syntax_analyzer.analyzator.exceptions.UnexpectedLexemeException;
import syntax_analyzer.compilator.CompilerInterface;
import syntax_analyzer.lang_objects.*;
import syntax_analyzer.scanner.Scanner;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SyntaxAnalyzer {
	protected Scanner scanner = null;
	
	protected CompilerInterface compiler = null;
	
	protected LinkedList<LangObjectInterface> langObjectStack = null;
	
	protected LangObjectGeneratorInterface langObjectGenerator = null;

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

	public SyntaxAnalyzer setLangObjectGenerator(LangObjectGeneratorInterface generator) {
		langObjectGenerator = generator;
		
		return this;
	}
	
	public SyntaxAnalyzer setRuleManager(RuleManager ruleManager) {
		this.ruleManager = ruleManager;
		
		return this;
	}
	
	public SyntaxAnalyzer setStartNotTerminal(Alphabet notTerminal) {
		langObjectStack = new LinkedList<>();
		langObjectStack.add(langObjectGenerator.generate(notTerminal));
		
		return this;
	}
	
	public void analyze() throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
		if(null == scanner) {
			throw new InvalidConfigException("Scanner is not configured");
		}
		
		if(null == compiler) {
			throw new InvalidConfigException("Compiler is not configured");
		}
		
		if(null == langObjectStack) {
			throw new InvalidConfigException("Start not-terminal is not set");
		}
		
		if(null == ruleManager) {
			throw new InvalidConfigException("Rule manager is not set");
		}
		
		if(null == langObjectGenerator) {
			throw new InvalidConfigException("Lang object generator is not configured");
		}

		compiler.init();
		Terminal curTerminal = scanner.scan();
		
		while(null != curTerminal) {
			LangObjectInterface topObject = langObjectStack.getLast();
			
			if(LangObjectType.TERMINAL == topObject.getType()) {
				if(curTerminal.getId() == topObject.getId()) {
					compiler.processTerminal(curTerminal);
					langObjectStack.removeLast();
					curTerminal = scanner.scan();
				}else {
					throw new UnexpectedLexemeException((Terminal) topObject, curTerminal);
				}
				
			}else if(LangObjectType.NOTERMINAL == topObject.getType()) {
				ruleManager.getTerminalScanner().setFirstScanTerminal(curTerminal);
				List<LangObjectInterface> ruleLangObjects = ruleManager.generateRule(topObject.getId());
				langObjectStack.removeLast();
				
				if(null != ruleLangObjects) {
					langObjectStack.addAll(ruleLangObjects);
				}
				
			}else if(LangObjectType.OPERATING == topObject.getType()) {
				compiler.processOperating((Operating) topObject);
				langObjectStack.removeLast();
			}
		}
	}
}
