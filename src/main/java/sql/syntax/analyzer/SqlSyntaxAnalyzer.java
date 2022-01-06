package sql.syntax.analyzer;


import syntax.analyzer.*;
import syntax.compilator.CompilerInterface;
import global.helpers.MapWrapper;
import syntax.input_adapters.InputInterface;
import syntax.lang.LanguageManager;
import syntax.scanner.Scanner;
import syntax.scanner.lexem.recognizer.*;

import java.util.List;

public class SqlSyntaxAnalyzer extends SyntaxAnalyzer {

    public static LexemeRecognizerInterface[] lexemeRecognizers = {
        //Keywords
        new KeywordRecognizer(new MapWrapper<Enum<? extends Enum<?>>, String>()
            .add(Alphabet.T_SELECT, "SELECT")
            .add(Alphabet.T_DELETE, "DELETE")
            .add(Alphabet.T_INSERT, "INSERT INTO")
            .add(Alphabet.T_UPDATE, "UPDATE")
            .add(Alphabet.T_AND, "AND")
            .add(Alphabet.T_GROUP_BY, "GROUP BY")
            .add(Alphabet.T_FROM, "FROM")
            .add(Alphabet.T_CREATE, "CREATE")
            .add(Alphabet.T_TABLE, "TABLE")
            .add(Alphabet.T_PRIMARY, "PRIMARY")
            .add(Alphabet.T_WHERE, "WHERE")
            .add(Alphabet.T_OR, "OR")
            .add(Alphabet.T_VALUES, "VALUES")
            .add(Alphabet.T_NOT, "NOT")
            .add(Alphabet.T_DEFAULT, "DEFAULT")
            .add(Alphabet.T_NULL, "NULL")
            .add(Alphabet.T_JOIN, "JOIN")
            .add(Alphabet.T_INNER, "INNER")
            .add(Alphabet.T_LEFT, "LEFT")
            .add(Alphabet.T_CROSS, "CROSS")
            .add(Alphabet.T_RIGHT, "RIGHT")
            .add(Alphabet.T_OUTER, "OUTER")
            .add(Alphabet.T_ON, "ON")
            .add(Alphabet.T_ORDER_BY, "ORDER BY")
            .add(Alphabet.T_LIMIT, "LIMIT")
            .add(Alphabet.T_OFFSET, "OFFSET")
            .add(Alphabet.T_TYPE_INT, "INT")
            .add(Alphabet.T_TYPE_VARCHAR, "VARCHAR")
            .add(Alphabet.T_DISTINCT, "DISTINCT")
            .add(Alphabet.T_AS, "AS")
        ),

        //Punctuation marks
        new StringRecognizer('\"', Alphabet.T_CONST_STRING),
        new StringRecognizer('`', Alphabet.T_DB_OBJECT_ID),
        new BaseRecognizer(new MapWrapper<Enum<? extends Enum<?>>, String>()
            .add(Alphabet.T_DOT, ".")
            .add(Alphabet.T_SEMICOLON, ";")
            .add(Alphabet.T_ASTERISK, "*")
            .add(Alphabet.T_COMMA, ",")
        ),

        //Special lexemes
        new IDRecognizer(Alphabet.T_ID),
        new IntegerRecognizer(Alphabet.T_CONST_INT),
        new CommentRecognizer(Alphabet.T_COMMENT),
        new NeutralRecognizer(Alphabet.T_NEUTRAL),

        //Arithmetics
        new BaseRecognizer(new MapWrapper<Enum<? extends Enum<?>>, String>()
            .add(Alphabet.T_EQUAL, "=")
            .add(Alphabet.T_LEFT_PARENTHESIS, "(")
            .add(Alphabet.T_RIGHT_PARENTHESIS, ")")
            .add(Alphabet.T_ADD, "+")
            .add(Alphabet.T_SUB, "-")
            .add(Alphabet.T_MULT, "*")
            .add(Alphabet.T_DIV, "/")
        )
    };

    public void init() {
        LanguageManager<Alphabet> langManager = new LanguageManager<>(Alphabet.class);
        this.scanner.setLexemeRecognizers(SqlSyntaxAnalyzer.lexemeRecognizers);
        this.scanner.setIgnoredLexemeTypes(new Alphabet[]{
            Alphabet.T_NEUTRAL,
            Alphabet.T_COMMENT
        });
        ListRule.setDefaultDelimiter(Alphabet.T_COMMA);
        this.setRuleManager(new RuleManager(this.scanner, SqlSyntaxAnalyzer.rules()).setLangManager(langManager));

        langManager
            .registerTerminalTypesByPrefix("T_")
            .registerNotTerminalTypesByPrefix("NT_")
            .registerOperatingTypesByPrefix("O_")
            .setLexemeRecognizers(List.of(SqlSyntaxAnalyzer.lexemeRecognizers));

        setLangManager(langManager);
        this.setStartNotTerminal(Alphabet.NT_QUERY);
    }

    public SqlSyntaxAnalyzer(InputInterface input, CompilerInterface compiler) {
        super(new Scanner(input), compiler);
        init();
    }

    public SqlSyntaxAnalyzer() {
        this(new Scanner());
    }

    public SqlSyntaxAnalyzer(Scanner scanner) {
        super();
        this.scanner = scanner;
        init();
    }

    public static RuleInterface[] rules(){
        return new RuleInterface[]{
            // NT_QUERIES -> NT_QUERY NT_QUERY_LIST
            new Rule(
                Alphabet.NT_QUERIES,
                new Alphabet[]{
                    Alphabet.NT_QUERY,
                    Alphabet.NT_QUERY_LIST
                }
            ),

            // NT_QUERY_LIST -> ;NT_QUERY NT_QUERY_LIST | e
            new ListRule(
                Alphabet.T_SEMICOLON,
                new Alphabet[]{
                    Alphabet.NT_QUERY
                },
                Alphabet.NT_QUERY_LIST
            ),

            // NT_QUERY -> NT_INSERT | NT_UPDATE | NT_SELECT | NT_DELETE
            new PrefixRule(
                Alphabet.NT_QUERY,
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>>()
                    .add(new Alphabet[]{Alphabet.T_INSERT}, Alphabet.NT_INSERT)
                    .add(new Alphabet[]{Alphabet.T_SELECT}, Alphabet.NT_SELECT)
                    .add(new Alphabet[]{Alphabet.T_UPDATE}, Alphabet.NT_UPDATE)
                    .add(new Alphabet[]{Alphabet.T_DELETE}, Alphabet.NT_DELETE)
            ),

            // NT_INSERT -> insert into NT_TABLE_ID (NT_INSERT_COLUMNS
            new Rule(
                Alphabet.NT_INSERT,
                new Alphabet[]{
                    Alphabet.T_INSERT,
                    Alphabet.NT_TABLE_ID,
                    Alphabet.T_LEFT_PARENTHESIS,
                    Alphabet.NT_INSERT_COLUMNS,
                }
            ),

            // NT_INSERT_COLUMNS -> NT_COLUMN_ID NT_INSERT_COLUMNS_LIST)
            new Rule(
                Alphabet.NT_INSERT_COLUMNS,
                new Alphabet[]{
                    Alphabet.NT_INSERT_COLUMN_ID,
                    Alphabet.NT_INSERT_COLUMNS_LIST,
                    Alphabet.T_RIGHT_PARENTHESIS
                }
            ),

            // NT_INSERT_COLUMNS_LIST -> ,NT_COLUMN_ID NT_INSERT_COLUMNS_LIST| e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_INSERT_COLUMN_ID
                },
                Alphabet.NT_INSERT_COLUMNS_LIST
            ),

            // NT_INSERT_VALUES_START -> values (NT_INSERT_VALUES
            new Rule(Alphabet.NT_INSERT_VALUES_START,
                new Alphabet[]{
                    Alphabet.T_VALUES,
                    Alphabet.T_LEFT_PARENTHESIS,
                    Alphabet.NT_INSERT_VALUES
                }
            ),

            // NT_INSERT_VALUES -> NT_INSERT_VALUE NT_INSERT_VALUES_LIST)
            new Rule(Alphabet.NT_INSERT_VALUES,
                new Alphabet[]{
                    Alphabet.NT_INSERT_VALUE,
                    Alphabet.NT_INSERT_VALUES_LIST,

                }),

            // NT_INSERT_VALUES_LIST -> ,NT_INSERT_VALUE NT_INSERT_VALUES_LIST| e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_INSERT_VALUE
                },
                Alphabet.NT_INSERT_VALUES_LIST
            ),

            // NT_COLUMN_ID -> NT_COLUMN_ID_ITEM NT_COLUMN_ID_ITEM_LIST
            new Rule(
                Alphabet.NT_COLUMN_ID,
                new Alphabet[]{
                    Alphabet.NT_COLUMN_ID_ITEM,
                    Alphabet.NT_COLUMN_ID_ITEM_LIST,
                }
            ),

            // NT_COLUMN_ID_ITEM_LIST -> ,NT_COLUMN_ID_ITEM NT_COLUMN_ID_ITEM_LIST | e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_COLUMN_ID_ITEM
                },
                Alphabet.NT_COLUMN_ID_ITEM_LIST
            ),

            // NT_COLUMN_ID_ITEM -> "<string>" | <id> | '<string>'
            new OrRule(
                Alphabet.NT_COLUMN_ID_ITEM,
                Alphabet.T_CONST_STRING,
                Alphabet.T_DB_OBJECT_ID,
                Alphabet.T_ID
            ),
            /////////////////////////////////////////////////////////////////////////

            // ---------------------------- NT_CREATE_TABLE ----------------------------
            // NT_CREATE_TABLE -> create table <id> (NT_CREATE_TABLE_FIELDS
            new Rule(
                Alphabet.NT_CREATE_TABLE,
                new Alphabet[]{
                    Alphabet.T_CREATE,
                    Alphabet.T_TABLE,
                    Alphabet.T_ID,
                    Alphabet.T_LEFT_PARENTHESIS,
                    Alphabet.NT_CREATE_TABLE_FIELDS,

                }
            ),

            // NT_CREATE_TABLE_FIELDS -> NT_CREATE_TABLE_FIELD NT_CREATE_TABLE_FIELD_LIST)
            new Rule(
                Alphabet.NT_CREATE_TABLE_FIELDS,
                new Alphabet[]{
                    Alphabet.NT_CREATE_TABLE_FIELD,
                    Alphabet.NT_CREATE_TABLE_FIELD_LIST,
                    Alphabet.T_RIGHT_PARENTHESIS,
                }
            ),

            // NT_CREATE_TABLE_FIELD_LIST -> ,NT_CREATE_TABLE_FIELD NT_CREATE_TABLE_FIELD_LIST | e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_CREATE_TABLE_FIELD
                },
                Alphabet.NT_CREATE_TABLE_FIELD_LIST
            ),

            // NT_CREATE_TABLE_FIELD -> <id> NT_CREATE_TABLE_FIELD_TYPE NT_CREATE_TABLE_FIELD_CONFIG
            new Rule(
                Alphabet.NT_CREATE_TABLE_FIELD,
                new Alphabet[]{
                    Alphabet.T_ID,
                    Alphabet.NT_CREATE_TABLE_FIELD_TYPE,
                    Alphabet.NT_CREATE_TABLE_FIELD_CONFIG,
                }
            ),

            // NT_CREATE_TABLE_FIELD_TYPE -> int | varchar | varchar(<int>)
            new OrRule(
                Alphabet.NT_CREATE_TABLE_FIELD_TYPE,
                new Alphabet[]{
                    Alphabet.T_TYPE_INT
                },
                new Alphabet[]{
                    Alphabet.T_TYPE_VARCHAR
                },
                new Alphabet[]{
                    Alphabet.T_TYPE_VARCHAR,
                    Alphabet.T_LEFT_PARENTHESIS,
                    Alphabet.T_CONST_INT,
                    Alphabet.T_RIGHT_PARENTHESIS,
                }
            ),

            // NT_CREATE_TABLE_FIELD_CONFIG -> NT_CREATE_TABLE_FIELD_CONFIG_ITEM NT_CREATE_TABLE_FIELD_CONFIG_LIST
            new Rule(
                Alphabet.NT_CREATE_TABLE_FIELD_CONFIG,
                new Alphabet[]{
                    Alphabet.NT_CREATE_TABLE_FIELD_CONFIG_ITEM,
                    Alphabet.NT_CREATE_TABLE_FIELD_CONFIG_LIST
                }
            ),

            // NT_CREATE_TABLE_FIELD_CONFIG_LIST -> NT_CREATE_TABLE_FIELD_CONFIG_ITEM NT_CREATE_TABLE_FIELD_CONFIG_LIST| e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_CREATE_TABLE_FIELD_CONFIG_ITEM
                },
                Alphabet.NT_CREATE_TABLE_FIELD_CONFIG_LIST
            ),

            // NT_CREATE_TABLE_FIELD_CONFIG_ITEM -> not null | default <string> | default <int> | default null
            new OrRule(
                Alphabet.NT_CREATE_TABLE_FIELD_CONFIG_ITEM,
                new Alphabet[]{
                    Alphabet.T_NOT,
                    Alphabet.T_NULL
                },
                new Alphabet[]{
                    Alphabet.T_DEFAULT,
                    Alphabet.T_CONST_STRING
                },
                new Alphabet[]{
                    Alphabet.T_DEFAULT,
                    Alphabet.T_CONST_INT
                },
                new Alphabet[]{
                    Alphabet.T_DEFAULT,
                    Alphabet.T_NULL
                }
            ),

            // ---------------------------- NT_SELECT ----------------------------
            // NT_SELECT -> select NT_SELECT_COLUMNS | select distinct NT_SELECT_COLUMNS
            new PrefixRuleEx(
                Alphabet.NT_SELECT,
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(new Alphabet[]{
                            Alphabet.T_SELECT,
                        },
                        new Alphabet[]{
                            Alphabet.T_SELECT,
                            Alphabet.NT_SELECT_COLUMNS
                        })
                    .add(new Alphabet[]{
                        Alphabet.T_SELECT,
                        Alphabet.T_DISTINCT
                    }, new Alphabet[]{
                        Alphabet.T_SELECT,
                        Alphabet.T_DISTINCT,
                        Alphabet.NT_SELECT_COLUMNS
                    })
            ),

            // NT_SELECT_COLUMNS -> NT_SELECT_COLUMNS_LIST_ITEM NT_SELECT_COLUMNS_LIST NT_SELECT_FROM
            new Rule(
                Alphabet.NT_SELECT_COLUMNS,
                new Alphabet[]{
                    Alphabet.NT_SELECT_COLUMNS_LIST_ITEM,
                    Alphabet.NT_SELECT_COLUMNS_LIST,
                    Alphabet.NT_SELECT_FROM
                }
            ),

            // NT_SELECT_COLUMNS_LIST -> ,NT_SELECT_COLUMNS_LIST_ITEM NT_SELECT_COLUMNS_LIST|e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_SELECT_COLUMNS_LIST_ITEM
                },
                Alphabet.NT_SELECT_COLUMNS_LIST
            ),

            // NT_SELECT_COLUMNS_LIST_ITEM -> * | NT_EXPRESSION NT_COLUMN_ALIAS_PART
            new OrFallbackRule(
                Alphabet.NT_SELECT_COLUMNS_LIST_ITEM,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION,
                    Alphabet.NT_COLUMN_ALIAS_PART
                },
                Alphabet.T_ASTERISK
            ),

            // NT_COLUMN_ALIAS_PART -> as <id> | <id> | e
            new OrFallbackRule(
                Alphabet.NT_COLUMN_ALIAS_PART,
                null,
                new Alphabet[]{
                    Alphabet.T_AS,
                    Alphabet.T_ID
                },
                new Alphabet[]{
                    Alphabet.T_ID
                }
            ),

            // NT_EXPRESSION -> NT_EXPRESSION_OR NT_EXPRESSION1
            new Rule(
                Alphabet.NT_EXPRESSION,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_OR,
                    Alphabet.NT_EXPRESSION1
                }
            ),

            // NT_EXPRESSION1 -> or NT_EXPRESSION_OR NT_EXPRESSION1| e
            new ListRule(
                Alphabet.T_OR,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_OR
                },
                Alphabet.NT_EXPRESSION1
            ),

            // NT_EXPRESSION_OR -> NT_EXPRESSION_AND NT_EXPRESSION_OR1
            new Rule(
                Alphabet.NT_EXPRESSION_OR,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_AND,
                    Alphabet.NT_EXPRESSION_OR1
                }
            ),

            // NT_EXPRESSION_OR1 -> and NT_EXPRESSION_AND NT_EXPRESSION_OR1| e
            new ListRule(
                Alphabet.T_AND,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_AND
                },
                Alphabet.NT_EXPRESSION_OR1
            ),

            // NT_EXPRESSION_AND -> NT_EXPRESSION_SUBADD NT_EXPRESSION_AND1
            new Rule(
                Alphabet.NT_EXPRESSION_AND,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_SUBADD,
                    Alphabet.NT_EXPRESSION_AND1,
                }
            ),

            // NT_EXPRESSION_AND1 -> + NT_EXPRESSION_SUBADD NT_EXPRESSION_AND1 | - NT_EXPRESSION_SUBADD NT_EXPRESSION_AND1| e
            new ListRule(
                new Alphabet[]{
                    Alphabet.T_ADD,
                    Alphabet.T_SUB
                },
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_SUBADD
                },
                Alphabet.NT_EXPRESSION_AND1
            ),

            // NT_EXPRESSION_SUBADD -> NT_EXPRESSION_MULTDIV NT_EXPRESSION_SUBADD1
            new Rule(
                Alphabet.NT_EXPRESSION_SUBADD,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_MULTDIV,
                    Alphabet.NT_EXPRESSION_SUBADD1,
                }
            ),

            // NT_EXPRESSION_SUBADD1 -> * NT_EXPRESSION_MULTDIV NT_EXPRESSION_SUBADD1 | / NT_EXPRESSION_MULTDIV NT_EXPRESSION_SUBADD1 | e
            new ListRule(
                new Alphabet[]{
                    Alphabet.T_MULT,
                    Alphabet.T_DIV
                },
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_MULTDIV
                },
                Alphabet.NT_EXPRESSION_SUBADD1
            ),

            // NT_EXPRESSION_MULTDIV -> + NT_ELEMENT | - NT_ELEMENT | NT_ELEMENT
            new OrFallbackRule(
                Alphabet.NT_EXPRESSION_MULTDIV,
                new Alphabet[]{
                    Alphabet.NT_ELEMENT
                },
                new Alphabet[]{
                    Alphabet.T_ADD,
                    Alphabet.NT_ELEMENT
                },
                new Alphabet[]{
                    Alphabet.T_SUB,
                    Alphabet.NT_ELEMENT
                }
            ),

            // NT_ELEMENT -> <int> | <string> | NT_FIELD
            new OrFallbackRule(
                Alphabet.NT_ELEMENT,
                new Alphabet[]{
                    Alphabet.NT_FIELD
                },
                new Alphabet[]{
                    Alphabet.T_CONST_STRING
                },
                new Alphabet[]{
                    Alphabet.T_CONST_INT
                }
            ),

            // NT_FIELD -> NT_FIELD_PART NT_FIELD_PARTS
            new Rule(
                Alphabet.NT_FIELD,
                new Alphabet[]{
                    Alphabet.NT_FIELD_PART,
                    Alphabet.NT_FIELD_PARTS,
                }
            ),

            // NT_FIELD_PARTS -> .NT_FIELD_PART NT_FIELD_PARTS| e
            new ListRule(
                Alphabet.T_DOT,
                new Alphabet[]{
                    Alphabet.NT_FIELD_PART,
                },
                Alphabet.NT_FIELD_PARTS
            ),

            // NT_FIELD_PART -> <objectID> | <id>
            new OrRule(
                Alphabet.NT_FIELD_PART,
                Alphabet.T_DB_OBJECT_ID,
                Alphabet.T_ID
            ),

            // NT_SELECT_FROM -> from NT_SELECT_TABLES
            new Rule(
                Alphabet.NT_SELECT_FROM,
                new Alphabet[]{
                    Alphabet.T_FROM,
                    Alphabet.NT_SELECT_TABLES
                }
            ),

            // NT_SELECT_TABLES -> NT_SELECT_TABLE_ID NT_SELECT_TABLES_LIST NT_SELECT_JOIN
            new Rule(
                Alphabet.NT_SELECT_TABLES,
                new Alphabet[]{
                    Alphabet.NT_SELECT_TABLE_ID,
                    Alphabet.NT_SELECT_TABLES_LIST,
                    Alphabet.NT_SELECT_JOIN
                }
            ),

            // NT_SELECT_TABLES_LIST -> ,NT_SELECT_TABLE_ID NT_SELECT_TABLES_LIST| e
            new ListRule(
                Alphabet.T_DOT,
                new Alphabet[]{
                    Alphabet.NT_SELECT_TABLE_ID,
                },
                Alphabet.NT_SELECT_TABLES_LIST
            ),

            // NT_SELECT_TABLE_ID -> NT_SELECT_TABLE_ID_PART NT_SELECT_TABLE_ID_PARTS
            new Rule(
                Alphabet.NT_SELECT_TABLE_ID,
                new Alphabet[]{
                    Alphabet.NT_SELECT_TABLE_ID_PART,
                    Alphabet.NT_SELECT_TABLE_ID_PARTS,
                }
            ),

            // NT_SELECT_TABLE_ID_PARTS -> .NT_SELECT_TABLE_ID_PART NT_SELECT_TABLE_ID_PARTS| e
            new ListRule(
                Alphabet.T_DOT,
                new Alphabet[]{
                    Alphabet.NT_SELECT_TABLE_ID_PART,
                },
                Alphabet.NT_SELECT_TABLE_ID_PARTS
            ),

            // NT_SELECT_TABLE_ID_PART -> <objectID> | <tildeObjectID>
            new OrRule(
                Alphabet.NT_SELECT_TABLE_ID_PART,
                Alphabet.T_DB_OBJECT_ID,
                Alphabet.T_ID
            ),

            // NT_SELECT_JOIN ->
            //			inner join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			left join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			cross join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			right join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			outer join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			NT_SELECT_GROUP
            new OrFallbackRule(
                Alphabet.NT_SELECT_JOIN,
                new Alphabet[]{
                    Alphabet.NT_SELECT_GROUP
                },
                new Alphabet[]{
                    Alphabet.T_INNER,
                    Alphabet.T_JOIN,
                    Alphabet.NT_SELECT_JOIN_ITEM,
                    Alphabet.NT_SELECT_JOIN
                },
                new Alphabet[]{
                    Alphabet.T_LEFT,
                    Alphabet.T_JOIN,
                    Alphabet.NT_SELECT_JOIN_ITEM,
                    Alphabet.NT_SELECT_JOIN
                },
                new Alphabet[]{
                    Alphabet.T_CROSS,
                    Alphabet.T_JOIN,
                    Alphabet.NT_SELECT_JOIN_ITEM,
                    Alphabet.NT_SELECT_JOIN
                },
                new Alphabet[]{
                    Alphabet.T_RIGHT,
                    Alphabet.T_JOIN,
                    Alphabet.NT_SELECT_JOIN_ITEM,
                    Alphabet.NT_SELECT_JOIN
                },
                new Alphabet[]{
                    Alphabet.T_OUTER,
                    Alphabet.T_JOIN,
                    Alphabet.NT_SELECT_JOIN_ITEM,
                    Alphabet.NT_SELECT_JOIN
                }
            ),

            // NT_SELECT_JOIN_ITEM -> NT_SELECT_TABLE_ID on NT_EXPRESSION
            new Rule(
                Alphabet.NT_SELECT_JOIN_ITEM,
                new Alphabet[]{
                    Alphabet.NT_SELECT_TABLE_ID,
                    Alphabet.T_ON,
                    Alphabet.NT_EXPRESSION
                }
            ),

            // NT_SELECT_GROUP -> group by NT_FIELD NT_SELECT_GROUP_LIST| NT_SELECT_ORDER
            new OrFallbackRule(
                Alphabet.NT_SELECT_GROUP,
                new Alphabet[]{
                    Alphabet.NT_SELECT_ORDER,
                },
                new Alphabet[]{
                    Alphabet.T_GROUP_BY,
                    Alphabet.NT_FIELD,
                    Alphabet.NT_SELECT_GROUP_LIST
                }
            ),

            // NT_SELECT_GROUP_LIST -> ,NT_FIELD NT_SELECT_GROUP_LIST| e
            new ListRule(
                Alphabet.T_COMMA,
                new Alphabet[]{
                    Alphabet.NT_FIELD
                },
                Alphabet.NT_SELECT_GROUP_LIST
            ),

            // NT_SELECT_ORDER -> order by NT_SELECT_ORDER_LIST | NT_SELECT_LIMIT
            new OrFallbackRule(
                Alphabet.NT_SELECT_ORDER,
                new Alphabet[]{
                    Alphabet.NT_SELECT_LIMIT,
                },
                new Alphabet[]{
                    Alphabet.T_ORDER_BY,
                    Alphabet.NT_SELECT_GROUP_LIST
                }
            ),

            // NT_SELECT_ORDER_LIST -> ,NT_FIELD NT_SELECT_GROUP_LIST| e
            new ListRule(
                Alphabet.T_COMMA,
                new Alphabet[]{
                    Alphabet.NT_FIELD
                },
                Alphabet.NT_SELECT_GROUP_LIST
            ),

            // NT_SELECT_LIMIT -> limit <int>| NT_SELECT_OFFSET
            new OrFallbackRule(
                Alphabet.NT_SELECT_LIMIT,
                new Alphabet[]{
                    Alphabet.NT_SELECT_OFFSET,
                },
                new Alphabet[]{
                    Alphabet.T_LIMIT,
                    Alphabet.T_CONST_INT
                }
            ),

            // NT_SELECT_OFFSET -> offset <int> | e
            new OrFallbackRule(
                Alphabet.NT_SELECT_OFFSET,
                null,
                new Alphabet[]{
                    Alphabet.T_OFFSET,
                    Alphabet.T_CONST_INT
                }
            ),
        };
    }
}
