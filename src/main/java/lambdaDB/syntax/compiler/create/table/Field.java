package lambdaDB.syntax.compiler.create.table;

import lambdaDB.query.executer.Commands;
import lambdaDB.syntax.analyzer.Alphabet;
import syntax.compilator.output.command.Command;
import syntax.compilator.output.command.writer.CommandWriterInterface;
import syntax.lang.letter.Terminal;

import java.io.IOException;

public class Field {
    protected String name;

    protected FieldType type;

    protected Terminal defaultValue = null;

    protected boolean notNull = false;

    protected boolean autoIncrement = false;

    protected boolean primaryKey = false;

    protected Integer typeSize = null;

    public Field(String name) {
        this.name = name;
    }

    public Field setName(String name) {
        this.name = name;

        return this;
    }

    public Field setType(FieldType type) {
        this.type = type;

        return this;
    }

    public void setDefaultValue(Terminal terminal) {
        setDefaultValue(terminal, true);
    }

    public Field setDefaultValue(Terminal terminal, boolean throwException) {
        if (null != defaultValue && throwException) {
            // TODO throw exception
        }

        defaultValue = terminal;

        return this;
    }

    public Field notNull() {
        notNull = true;

        return this;
    }

    public Field autoIncrement() {
        autoIncrement = true;

        return this;
    }

    public Field setTypeSize(int typeSize) {
        this.typeSize = typeSize;

        return this;
    }

    public Field primaryKey() {
        this.primaryKey = true;

        return this;
    }

    public void compile(CommandWriterInterface writer) throws IOException {
        writer.write(
            new Command(Commands.CREATE_TABLE_FIELD)
                .append(name)
                .append(type.ordinal())
        );

        if (null != typeSize) {
            writer.write(new Command(Commands.CREATE_TABLE_FIELD_TYPE_SIZE).append(typeSize));
        }

        if (primaryKey) {
            writer.write(new Command(Commands.CREATE_TABLE_FIELD_PRIMARY_KEY));
        }

        if (autoIncrement) {
            writer.write(new Command(Commands.CREATE_TABLE_FIELD_AUTOINCREMENT));
        }

        if (notNull) {
            writer.write(new Command(Commands.CREATE_TABLE_FIELD_NOT_NULL));
        }

        if (null != defaultValue) {
            switch ((Alphabet) defaultValue.getId()) {
                case T_CONST_INT:
                    writer.write(
                        new Command(Commands.CREATE_TABLE_FIELD_DEFAULT_INT)
                            .append(Integer.parseInt(defaultValue.getView()))
                    );
                    break;

                case T_CONST_STRING:
                    writer.write(
                        new Command(Commands.CREATE_TABLE_FIELD_DEFAULT_STRING)
                            .append(defaultValue.getView().replaceAll("^\"|\"$", "")));
                    break;

                case T_NULL:
                    writer.write(new Command(Commands.CREATE_TABLE_FIELD_DEFAULT_NULL));
                    break;
            }
        }

    }
}
