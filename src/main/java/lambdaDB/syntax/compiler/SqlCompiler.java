package lambdaDB.syntax.compiler;

import lambdaDB.syntax.analyzer.Alphabet;
import lambdaDB.syntax.compiler.create.table.CreateTableCompiler;
import syntax.compilator.CompilerInterface;
import syntax.compilator.output.command.writer.CommandWriterInterface;
import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

public class SqlCompiler implements CompilerInterface {
    Stack<Terminal> terminals = new Stack<>();
    CompilerInterface queryCompiler;
    LinkedList<CompilerInterface> queryCompilers = new LinkedList<>();

    @Override
    public void init() {

    }

    @Override
    public void processTerminal(Terminal terminal) {
        if(null != queryCompiler){
            queryCompiler.processTerminal(terminal);

            return;
        }

        terminals.push(terminal);
    }

    @Override
    public void processOperating(Operating operating) {
        switch ((Alphabet)operating.getId()){
            case O_CREATE_TABLE_BEGIN:
                queryCompiler = new CreateTableCompiler(terminals.pop().getView());
                queryCompiler.init();

                break;

            case O_CREATE_TABLE_END:
                queryCompilers.push(queryCompiler);

                break;

            default:
                queryCompiler.processOperating(operating);
        }
    }

    @Override
    public void compile(CommandWriterInterface writer) throws IOException {
        for(CompilerInterface queryCompiler: queryCompilers){
            queryCompiler.compile(writer);
        }
    }
}
