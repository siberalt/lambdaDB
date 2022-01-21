package lambdaDB.syntax.compiler.create.table;

import lambdaDB.core.metadata.FieldType;
import lambdaDB.query.executer.Commands;
import lambdaDB.syntax.analyzer.Alphabet;
import syntax.compilator.CompilerInterface;
import syntax.compilator.output.command.Command;
import syntax.compilator.output.command.writer.CommandWriterInterface;
import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CreateTableCompiler implements CompilerInterface {
    protected String tableName;

    protected Stack<Terminal> terminals = new Stack<>();

    protected List<Field> fields = new LinkedList<>();

    protected Field newField;

    public CreateTableCompiler(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void init() {

    }

    @Override
    public void processTerminal(Terminal terminal) {
        terminals.push(terminal);
    }

    @Override
    public void processOperating(Operating operating) {
        switch ((Alphabet) operating.getId()) {
            case O_CREATE_TABLE_FIELD:
                newField = new Field(terminals.pop().getView());
                fields.add(newField);

                break;
            case O_CREATE_TABLE_FIELD_TYPE:
                FieldType type;

                switch ((Alphabet) terminals.pop().getId()) {
                    case T_TYPE_INT:
                        type = FieldType.INT;
                        break;
                    case T_TYPE_TINY_INT:
                        type = FieldType.TINYINT;
                        break;
                    case T_TYPE_VARCHAR:
                        type = FieldType.VARCHAR;
                        break;
                    // TODO throw exception
                    default:
                        type = null;
                }

                newField.setType(type);

                break;

            case O_CREATE_TABLE_FIELD_TYPE_SIZE:
                newField.setTypeSize(Integer.parseInt(terminals.pop().getView()));

                break;

            case O_CREATE_TABLE_DEFAULT_VALUE:
                newField.setDefaultValue(terminals.pop());

                break;

            case O_CREATE_TABLE_AUTO_INCREMENT:
                newField.autoIncrement();

                break;

            case O_CREATE_TABLE_PRIMARY_KEY:
                newField.primaryKey();

                break;

            case O_CREATE_TABLE_NOT_NULL:
                newField.notNull();

                break;
        }
    }

    @Override
    public void compile(CommandWriterInterface writer) throws IOException {
        writer.write(new Command(Commands.CREATE_TABLE).append(tableName));

        for (Field field: fields){
            field.compile(writer);
        }

        writer.write(new Command(Commands.CREATE_TABLE_COMMIT));
    }
}
