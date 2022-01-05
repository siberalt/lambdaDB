package syntax_analyzer.analyzator.exceptions;

public class UndefinedRuleException extends SyntaxAnalyzerException {

    public UndefinedRuleException(String ruleId){
        super("Rule with id " + ruleId + " is not defined");
    }
}
