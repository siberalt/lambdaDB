package lambdaDB.syntax.analyzer;


import syntax.analyzer.*;
import syntax.compilator.CompilerInterface;
import global.helpers.MapWrapper;
import syntax.scanner.input.InputInterface;
import syntax.lang.LetterManager;
import syntax.lang.letter.NotTerminal;
import syntax.scanner.Scanner;
import syntax.scanner.lexem.recognizer.*;

import java.util.Arrays;
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
            .add(Alphabet.T_KEY, "KEY")
            .add(Alphabet.T_AUTO_INCREMENT, "AUTO_INCREMENT")
            .add(Alphabet.T_TYPE_TINY_INT, "TINYINT")
        ),

        //Punctuation marks
        new StringRecognizer('\"', Alphabet.T_CONST_STRING),
        new StringRecognizer('`', Alphabet.T_DB_OBJECT_ID),
        new BaseRecognizer(new MapWrapper<Enum<? extends Enum<?>>, String>()
            .add(Alphabet.T_DOT, ".")
            .add(Alphabet.T_SEMICOLON, ";")
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
        LetterManager<Alphabet> langManager = new LetterManager<>(Alphabet.class);
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
            .setLexemeRecognizers(List.of(SqlSyntaxAnalyzer.lexemeRecognizers))
            .setNotTerminalGenerator(type -> {
                String[] parts = type.name()
                    .substring(3)
                    .toLowerCase()
                    .split("_");
                NotTerminal notTerminal = new NotTerminal(type);

                List<Enum<? extends Enum<?>>> queries = Arrays.asList(
                    Alphabet.NT_SELECT,
                    Alphabet.NT_DELETE,
                    Alphabet.NT_UPDATE,
                    Alphabet.NT_INSERT
                );

                if (!queries.contains(type)) {
                    notTerminal.setView("<" + String.join(" ", parts) + ">");
                } else {
                    notTerminal.setView(String.join(" ", parts));
                }

                return notTerminal;
            });

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

    public static RuleInterface[] rules() {
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
            new PrefixRuleSingle(
                Alphabet.NT_QUERY,
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>>()
                    .add(new Alphabet[]{Alphabet.T_INSERT}, Alphabet.NT_INSERT)
                    .add(new Alphabet[]{Alphabet.T_SELECT}, Alphabet.NT_SELECT)
                    .add(new Alphabet[]{Alphabet.T_UPDATE}, Alphabet.NT_UPDATE)
                    .add(new Alphabet[]{Alphabet.T_DELETE}, Alphabet.NT_DELETE)
                    .add(new Alphabet[]{Alphabet.T_CREATE}, Alphabet.NT_CREATE_TABLE)
            ),

            // NT_INSERT -> insert into O_INSERT NT_TABLE_ID O_INSERT_TABLE (NT_INSERT_COLUMNS
            new Rule(
                Alphabet.NT_INSERT,
                new Alphabet[]{
                    Alphabet.T_INSERT,
                    Alphabet.O_INSERT,
                    Alphabet.NT_TABLE_ID,
                    Alphabet.O_INSERT_TABLE,
                    Alphabet.T_LEFT_PARENTHESIS,
                    Alphabet.NT_INSERT_COLUMNS,
                }
            ),

            // NT_INSERT_COLUMNS -> NT_FIELD O_FIELD NT_INSERT_FIELD_LIST) NT_INSERT_VALUES_START
            new Rule(
                Alphabet.NT_INSERT_COLUMNS,
                new Alphabet[]{
                    Alphabet.NT_FIELD,
                    Alphabet.O_FIELD,
                    Alphabet.NT_INSERT_FIELD_LIST,
                    Alphabet.T_RIGHT_PARENTHESIS,
                    Alphabet.NT_INSERT_VALUES_START
                }
            ),

            // NT_INSERT_COLUMNS_LIST -> ,NT_FIELD O_FIELD NT_INSERT_FIELD_LIST| e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_FIELD,
                    Alphabet.O_FIELD
                },
                Alphabet.NT_INSERT_FIELD_LIST
            ),

            // NT_INSERT_VALUES_START -> values (O_INSERT_VALUES NT_INSERT_VALUES
            new Rule(Alphabet.NT_INSERT_VALUES_START,
                new Alphabet[]{
                    Alphabet.T_VALUES,
                    Alphabet.T_LEFT_PARENTHESIS,
                    Alphabet.O_INSERT_VALUES,
                    Alphabet.NT_INSERT_VALUES
                }
            ),

            // NT_INSERT_VALUES -> NT_EXPRESSION NT_INSERT_VALUES_LIST)
            new Rule(Alphabet.NT_INSERT_VALUES,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION,
                    Alphabet.NT_INSERT_VALUES_LIST,
                    Alphabet.T_RIGHT_PARENTHESIS
                }),

            // NT_INSERT_VALUES_LIST -> ,NT_EXPRESSION NT_INSERT_VALUES_LIST| e
            new ListRule(
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION
                },
                Alphabet.NT_INSERT_VALUES_LIST
            ),

            /////////////////////////////////////////////////////////////////////////

            // ---------------------------- NT_CREATE_TABLE ----------------------------
            // NT_CREATE_TABLE -> create table <id> O_CREATE_TABLE_BEGIN (NT_CREATE_TABLE_FIELDS
            new Rule(
                Alphabet.NT_CREATE_TABLE,
                new Alphabet[]{
                    Alphabet.T_CREATE,
                    Alphabet.T_TABLE,
                    Alphabet.T_ID,
                    Alphabet.O_CREATE_TABLE_BEGIN,
                    Alphabet.T_LEFT_PARENTHESIS,
                    Alphabet.NT_CREATE_TABLE_FIELDS,
                }
            ),

            // NT_CREATE_TABLE_FIELDS -> NT_CREATE_TABLE_FIELD NT_CREATE_TABLE_FIELD_LIST O_CREATE_TABLE_END)
            new Rule(
                Alphabet.NT_CREATE_TABLE_FIELDS,
                new Alphabet[]{
                    Alphabet.NT_CREATE_TABLE_FIELD,
                    Alphabet.NT_CREATE_TABLE_FIELD_LIST,
                    Alphabet.O_CREATE_TABLE_END,
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

            // NT_CREATE_TABLE_FIELD -> <id> O_CREATE_TABLE_FIELD NT_CREATE_TABLE_FIELD_TYPE NT_CREATE_TABLE_FIELD_CONFIG
            new Rule(
                Alphabet.NT_CREATE_TABLE_FIELD,
                new Alphabet[]{
                    Alphabet.T_ID,
                    Alphabet.O_CREATE_TABLE_FIELD,
                    Alphabet.NT_CREATE_TABLE_FIELD_TYPE,
                    Alphabet.NT_CREATE_TABLE_FIELD_CONFIG,
                }
            ),

            // NT_CREATE_TABLE_FIELD_TYPE -> int O_CREATE_TABLE_FIELD_TYPE| varchar O_CREATE_TABLE_FIELD_TYPE | varchar O_CREATE_TABLE_FIELD_TYPE (<int> O_CREATE_TABLE_FIELD_TYPE_SIZE)
            new PrefixRuleMultiple(
                Alphabet.NT_CREATE_TABLE_FIELD_TYPE,
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_TYPE_INT
                        },
                        new Alphabet[]{
                            Alphabet.T_TYPE_INT,
                            Alphabet.O_CREATE_TABLE_FIELD_TYPE
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_TYPE_TINY_INT
                        },
                        new Alphabet[]{
                            Alphabet.T_TYPE_TINY_INT,
                            Alphabet.O_CREATE_TABLE_FIELD_TYPE
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_TYPE_VARCHAR
                        },
                        new Alphabet[]{
                            Alphabet.T_TYPE_VARCHAR,
                            Alphabet.O_CREATE_TABLE_FIELD_TYPE
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_TYPE_VARCHAR,
                            Alphabet.T_LEFT_PARENTHESIS,
                        },
                        new Alphabet[]{
                            Alphabet.T_TYPE_VARCHAR,
                            Alphabet.O_CREATE_TABLE_FIELD_TYPE,
                            Alphabet.T_LEFT_PARENTHESIS,
                            Alphabet.T_CONST_INT,
                            Alphabet.O_CREATE_TABLE_FIELD_TYPE_SIZE,
                            Alphabet.T_RIGHT_PARENTHESIS,
                        }
                    )

            ),

            // NT_CREATE_TABLE_FIELD_CONFIG -> not null O_CREATE_TABLE_NOT_NULL NT_CREATE_TABLE_FIELD_CONFIG |
            //                                 default <string> O_CREATE_TABLE_DEFAULT_VALUE NT_CREATE_TABLE_FIELD_CONFIG |
            //                                 default <int> O_CREATE_TABLE_DEFAULT_VALUE NT_CREATE_TABLE_FIELD_CONFIG|
            //                                 default null O_CREATE_TABLE_DEFAULT_VALUE NT_CREATE_TABLE_FIELD_CONFIG|
            //                                 auto_increment O_CREATE_TABLE_AUTO_INCREMENT NT_CREATE_TABLE_FIELD_CONFIG|
            //                                 primary key O_CREATE_TABLE_PRIMARY_KEY NT_CREATE_TABLE_FIELD_CONFIG| e
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_CREATE_TABLE_FIELD_CONFIG,
                null,
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_NOT,
                            Alphabet.T_NULL
                        },
                        new Alphabet[]{
                            Alphabet.T_NOT,
                            Alphabet.T_NULL,
                            Alphabet.O_CREATE_TABLE_NOT_NULL,
                            Alphabet.NT_CREATE_TABLE_FIELD_CONFIG
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_DEFAULT,
                            Alphabet.T_CONST_STRING
                        },
                        new Alphabet[]{
                            Alphabet.T_DEFAULT,
                            Alphabet.T_CONST_STRING,
                            Alphabet.O_CREATE_TABLE_DEFAULT_VALUE,
                            Alphabet.NT_CREATE_TABLE_FIELD_CONFIG
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_DEFAULT,
                            Alphabet.T_CONST_INT
                        },
                        new Alphabet[]{
                            Alphabet.T_DEFAULT,
                            Alphabet.T_CONST_INT,
                            Alphabet.O_CREATE_TABLE_DEFAULT_VALUE,
                            Alphabet.NT_CREATE_TABLE_FIELD_CONFIG
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_DEFAULT,
                            Alphabet.T_NULL
                        },
                        new Alphabet[]{
                            Alphabet.T_DEFAULT,
                            Alphabet.T_NULL,
                            Alphabet.O_CREATE_TABLE_DEFAULT_VALUE,
                            Alphabet.NT_CREATE_TABLE_FIELD_CONFIG
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_AUTO_INCREMENT
                        },
                        new Alphabet[]{
                            Alphabet.T_AUTO_INCREMENT,
                            Alphabet.O_CREATE_TABLE_AUTO_INCREMENT,
                            Alphabet.NT_CREATE_TABLE_FIELD_CONFIG
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_PRIMARY,
                            Alphabet.T_KEY
                        },
                        new Alphabet[]{
                            Alphabet.T_PRIMARY,
                            Alphabet.T_KEY,
                            Alphabet.O_CREATE_TABLE_PRIMARY_KEY,
                            Alphabet.NT_CREATE_TABLE_FIELD_CONFIG
                        }
                    )
            ),

            // ---------------------------- NT_SELECT ----------------------------
            // NT_SELECT -> select O_SELECT NT_SELECT_COLUMNS | select O_SELECT distinct O_SELECT_DISTINCT NT_SELECT_COLUMNS
            new PrefixRuleMultiple(
                Alphabet.NT_SELECT,
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_SELECT,
                        },
                        new Alphabet[]{
                            Alphabet.T_SELECT,
                            Alphabet.O_SELECT,
                            Alphabet.NT_SELECT_COLUMNS
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_SELECT,
                            Alphabet.T_DISTINCT
                        },
                        new Alphabet[]{
                            Alphabet.T_SELECT,
                            Alphabet.O_SELECT,
                            Alphabet.T_DISTINCT,
                            Alphabet.O_SELECT_DISTINCT,
                            Alphabet.NT_SELECT_COLUMNS
                        }
                    )
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
                Alphabet.T_MULT
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

            // NT_EXPRESSION -> NT_EXPRESSION_EQ NT_EXPRESSION1
            new Rule(
                Alphabet.NT_EXPRESSION,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_EQ,
                    Alphabet.NT_EXPRESSION1
                }
            ),

            // NT_EXPRESSION1 -> = NT_EXPRESSION_EQ O_CAST_TYPES NT_EXPRESSION1 | e
            new ListRule(
                Alphabet.T_EQUAL,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_EQ,
                    Alphabet.O_CAST_TYPES
                },
                Alphabet.NT_EXPRESSION1
            ),

            // NT_EXPRESSION_EQ -> NT_EXPRESSION_OR NT_EXPRESSION_EQ1
            new Rule(
                Alphabet.NT_EXPRESSION_EQ,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_OR,
                    Alphabet.NT_EXPRESSION_EQ1
                }
            ),

            // NT_EXPRESSION_EQ1 -> or NT_EXPRESSION_OR O_CAST_TYPES NT_EXPRESSION1 | e
            new ListRule(
                Alphabet.T_OR,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_OR,
                    Alphabet.O_CAST_TYPES
                },
                Alphabet.NT_EXPRESSION_EQ1
            ),

            // NT_EXPRESSION_OR -> NT_EXPRESSION_AND NT_EXPRESSION_OR1
            new Rule(
                Alphabet.NT_EXPRESSION_OR,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_AND,
                    Alphabet.NT_EXPRESSION_OR1
                }
            ),

            // NT_EXPRESSION_OR1 -> and NT_EXPRESSION_AND O_CAST_TYPES NT_EXPRESSION_OR1| e
            new ListRule(
                Alphabet.T_AND,
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_AND,
                    Alphabet.O_CAST_TYPES
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

            // NT_EXPRESSION_AND1 -> + NT_EXPRESSION_SUBADD O_CAST_TYPES NT_EXPRESSION_AND1 | - NT_EXPRESSION_SUBADD O_CAST_TYPES NT_EXPRESSION_AND1| e
            new ListRule(
                new Alphabet[]{
                    Alphabet.T_ADD,
                    Alphabet.T_SUB
                },
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_SUBADD,
                    Alphabet.O_CAST_TYPES
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

            // NT_EXPRESSION_SUBADD1 -> * NT_EXPRESSION_MULTDIV O_CAST_TYPES NT_EXPRESSION_SUBADD1 | / NT_EXPRESSION_MULTDIV O_CAST_TYPES NT_EXPRESSION_SUBADD1 | e
            new ListRule(
                new Alphabet[]{
                    Alphabet.T_MULT,
                    Alphabet.T_DIV
                },
                new Alphabet[]{
                    Alphabet.NT_EXPRESSION_MULTDIV,
                    Alphabet.O_CAST_TYPES
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

            // NT_ELEMENT -> <int> O_CONST | <string> O_CONST | NT_FIELD | (NT_EXPRESSION)
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_ELEMENT,
                new Alphabet[]{
                    Alphabet.NT_FIELD
                },
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{Alphabet.T_CONST_STRING},
                        new Alphabet[]{
                            Alphabet.T_CONST_STRING,
                            Alphabet.O_CONST
                        }
                    )
                    .add(
                        new Alphabet[]{Alphabet.T_CONST_INT},
                        new Alphabet[]{
                            Alphabet.T_CONST_INT,
                            Alphabet.O_CONST
                        }
                    )
                    .add(
                        new Alphabet[]{Alphabet.T_LEFT_PARENTHESIS},
                        new Alphabet[]{
                            Alphabet.T_LEFT_PARENTHESIS,
                            Alphabet.NT_EXPRESSION,
                            Alphabet.T_RIGHT_PARENTHESIS
                        }
                    )

            ),

            // NT_FIELD -> NT_FIELD_PART O_FIELD NT_FIELD_PARTS
            new Rule(
                Alphabet.NT_FIELD,
                new Alphabet[]{
                    Alphabet.NT_FIELD_PART,
                    Alphabet.O_FIELD,
                    Alphabet.NT_FIELD_PARTS,
                }
            ),

            // NT_FIELD_PARTS -> .NT_FIELD_PART O_FIELD NT_FIELD_PARTS| e
            new ListRule(
                Alphabet.T_DOT,
                new Alphabet[]{
                    Alphabet.NT_FIELD_PART,
                    Alphabet.O_FIELD
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

            // NT_SELECT_TABLE_ID -> NT_TABLE_ID NT_SELECT_TABLE_ALIAS_PART| (NT_SELECT) NT_SELECT_TABLE_ALIAS_PART
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_SELECT_TABLE_ID,
                new Alphabet[]{
                    Alphabet.NT_TABLE_ID,
                    Alphabet.NT_SELECT_TABLE_ALIAS_PART
                },
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_LEFT_PARENTHESIS
                        },
                        new Alphabet[]{
                            Alphabet.T_LEFT_PARENTHESIS,
                            Alphabet.NT_SELECT,
                            Alphabet.T_RIGHT_PARENTHESIS,
                            Alphabet.NT_SELECT_TABLE_ALIAS_PART
                        }
                    )
            ),

            // NT_TABLE_ID -> NT_TABLE_ID_PART NT_TABLE_ID_PARTS
            new Rule(
                Alphabet.NT_TABLE_ID,
                new Alphabet[]{
                    Alphabet.NT_TABLE_ID_PART,
                    Alphabet.NT_TABLE_ID_PARTS,
                }
            ),

            // NT_SELECT_TABLE_ALIAS_PART -> as <id> | <id> | e
            new OrFallbackRule(
                Alphabet.NT_SELECT_TABLE_ALIAS_PART,
                null,
                new Alphabet[]{
                    Alphabet.T_AS,
                    Alphabet.T_ID
                },
                new Alphabet[]{
                    Alphabet.T_ID
                }
            ),

            // NT_TABLE_ID_PARTS -> .NT_SELECT_TABLE_ID_PART NT_SELECT_TABLE_ID_PARTS| e
            new ListRule(
                Alphabet.T_DOT,
                new Alphabet[]{
                    Alphabet.NT_TABLE_ID_PART,
                },
                Alphabet.NT_TABLE_ID_PARTS
            ),

            // NT_SELECT_TABLE_ID_PART -> <objectID> | <tildeObjectID>
            new OrRule(
                Alphabet.NT_TABLE_ID_PART,
                Alphabet.T_DB_OBJECT_ID,
                Alphabet.T_ID
            ),

            // NT_SELECT_JOIN ->
            //			inner O_SELECT_JOIN_TYPE join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			left O_SELECT_JOIN_TYPE join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			cross O_SELECT_JOIN_TYPE join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			right O_SELECT_JOIN_TYPE join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			outer O_SELECT_JOIN_TYPE join NT_SELECT_JOIN_ITEM NT_SELECT_JOIN |
            //			NT_SELECT_WHERE
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_SELECT_JOIN,
                new Alphabet[]{
                    Alphabet.NT_SELECT_WHERE
                },
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_INNER,
                            Alphabet.T_JOIN,
                        },
                        new Alphabet[]{
                            Alphabet.T_INNER,
                            Alphabet.O_SELECT_JOIN_TYPE,
                            Alphabet.T_JOIN,
                            Alphabet.NT_SELECT_JOIN_ITEM,
                            Alphabet.NT_SELECT_JOIN
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_LEFT,
                            Alphabet.T_JOIN,
                        },
                        new Alphabet[]{
                            Alphabet.T_LEFT,
                            Alphabet.O_SELECT_JOIN_TYPE,
                            Alphabet.T_JOIN,
                            Alphabet.NT_SELECT_JOIN_ITEM,
                            Alphabet.NT_SELECT_JOIN
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_CROSS,
                            Alphabet.O_SELECT_JOIN_TYPE,
                            Alphabet.T_JOIN,
                        },
                        new Alphabet[]{
                            Alphabet.T_CROSS,
                            Alphabet.O_SELECT_JOIN_TYPE,
                            Alphabet.T_JOIN,
                            Alphabet.NT_SELECT_JOIN_ITEM,
                            Alphabet.NT_SELECT_JOIN
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_RIGHT,
                            Alphabet.O_SELECT_JOIN_TYPE,
                            Alphabet.T_JOIN,
                        },
                        new Alphabet[]{
                            Alphabet.T_RIGHT,
                            Alphabet.T_JOIN,
                            Alphabet.O_SELECT_JOIN_TYPE,
                            Alphabet.NT_SELECT_JOIN_ITEM,
                            Alphabet.NT_SELECT_JOIN
                        }
                    )
                    .add(
                        new Alphabet[]{
                            Alphabet.T_OUTER,
                            Alphabet.O_SELECT_JOIN_TYPE,
                            Alphabet.T_JOIN,
                        },
                        new Alphabet[]{
                            Alphabet.T_OUTER,
                            Alphabet.T_JOIN,
                            Alphabet.NT_SELECT_JOIN_ITEM,
                            Alphabet.NT_SELECT_JOIN
                        }
                    )
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

            // NT_SELECT_WHERE -> WHERE NT_EXPRESSION| NT_SELECT_GROUP
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_SELECT_WHERE,
                new Alphabet[]{
                    Alphabet.NT_SELECT_GROUP
                },
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_WHERE
                        },
                        new Alphabet[]{
                            Alphabet.T_WHERE,
                            Alphabet.NT_EXPRESSION
                        }
                    )
            ),

            // NT_SELECT_GROUP -> group by NT_FIELD NT_SELECT_GROUP_LIST| NT_SELECT_ORDER
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_SELECT_GROUP,
                new Alphabet[]{
                    Alphabet.NT_SELECT_ORDER,
                },
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_GROUP_BY,
                        },
                        new Alphabet[]{
                            Alphabet.T_GROUP_BY,
                            Alphabet.NT_FIELD,
                            Alphabet.NT_SELECT_GROUP_LIST
                        }
                    )
            ),

            // NT_SELECT_GROUP_LIST -> ,NT_FIELD NT_SELECT_GROUP_LIST| e
            new ListRule(
                Alphabet.T_COMMA,
                new Alphabet[]{
                    Alphabet.NT_FIELD
                },
                Alphabet.NT_SELECT_GROUP_LIST
            ),

            // NT_SELECT_ORDER -> order by NT_FIELD NT_SELECT_ORDER_LIST | NT_SELECT_LIMIT
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_SELECT_ORDER,
                new Alphabet[]{
                    Alphabet.NT_SELECT_LIMIT,
                },
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_ORDER_BY,
                        },
                        new Alphabet[]{
                            Alphabet.T_ORDER_BY,
                            Alphabet.NT_FIELD,
                            Alphabet.NT_SELECT_ORDER_LIST
                        }
                    )
            ),

            // NT_SELECT_ORDER_LIST -> ,NT_FIELD NT_SELECT_GROUP_LIST| e
            new ListRule(
                Alphabet.T_COMMA,
                new Alphabet[]{
                    Alphabet.NT_FIELD
                },
                Alphabet.NT_SELECT_ORDER_LIST
            ),

            // NT_SELECT_LIMIT -> limit <int>| NT_SELECT_OFFSET
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_SELECT_LIMIT,
                new Alphabet[]{
                    Alphabet.NT_SELECT_OFFSET,
                },
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_LIMIT,
                        },
                        new Alphabet[]{
                            Alphabet.T_LIMIT,
                            Alphabet.T_CONST_INT
                        }
                    )
            ),

            // NT_SELECT_OFFSET -> offset <int> | e
            new FallbackPrefixRuleMultiple(
                Alphabet.NT_SELECT_OFFSET,
                null,
                new MapWrapper<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]>()
                    .add(
                        new Alphabet[]{
                            Alphabet.T_OFFSET,
                        },
                        new Alphabet[]{
                            Alphabet.T_OFFSET,
                            Alphabet.T_CONST_INT
                        }
                    )
            ),
        };
    }
}
