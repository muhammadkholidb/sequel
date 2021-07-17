package com.gitlab.muhammadkholidb.sequel.repository;

import static com.gitlab.muhammadkholidb.sequel.model.DataModel.C_CREATED_AT;
import static com.gitlab.muhammadkholidb.sequel.model.DataModel.C_DELETED_AT;
import static com.gitlab.muhammadkholidb.sequel.model.DataModel.C_ID;
import static com.gitlab.muhammadkholidb.sequel.model.DataModel.C_UPDATED_AT;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.gitlab.muhammadkholidb.sequel.annotation.DataColumn;
import com.gitlab.muhammadkholidb.sequel.model.DataModel;
import com.gitlab.muhammadkholidb.sequel.sql.Limit;
import com.gitlab.muhammadkholidb.sequel.sql.LimitFactory;
import com.gitlab.muhammadkholidb.sequel.sql.Order;
import com.gitlab.muhammadkholidb.sequel.sql.Where;
import com.gitlab.muhammadkholidb.sequel.utility.SQLUtils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * @param <M>
 * @author muhammad
 */
@Slf4j
public abstract class AbstractRepository<M extends DataModel> implements CommonRepository<M> {

    /**
     * SQL reserved words for several databases including ANSI standard. Reference:
     * https://www.drupal.org/docs/develop/coding-standards/list-of-sql-reserved-words
     */
    public static final String DEFAULT_SQL_RESERVED_WORDS = "A,ABORT,ABS,ABSOLUTE,ACCESS,ACTION,ADA,ADD,ADMIN,AFTER,AGGREGATE,ALIAS,ALL,ALLOCATE,ALSO,ALTER,ALWAYS,ANALYSE,ANALYZE,AND,ANY,ARE,ARRAY,AS,ASC,ASENSITIVE,ASSERTION,ASSIGNMENT,ASYMMETRIC,AT,ATOMIC,ATTRIBUTE,ATTRIBUTES,AUDIT,AUTHORIZATION,AUTO_INCREMENT,AVG,AVG_ROW_LENGTH,BACKUP,BACKWARD,BEFORE,BEGIN,BERNOULLI,BETWEEN,BIGINT,BINARY,BIT,BIT_LENGTH,BITVAR,BLOB,BOOL,BOOLEAN,BOTH,BREADTH,BREAK,BROWSE,BULK,BY,C,CACHE,CALL,CALLED,CARDINALITY,CASCADE,CASCADED,CASE,CAST,CATALOG,CATALOG_NAME,CEIL,CEILING,CHAIN,CHANGE,CHAR,CHAR_LENGTH,CHARACTER,CHARACTER_LENGTH,CHARACTER_SET_CATALOG,CHARACTER_SET_NAME,CHARACTER_SET_SCHEMA,CHARACTERISTICS,CHARACTERS,CHECK,CHECKED,CHECKPOINT,CHECKSUM,CLASS,CLASS_ORIGIN,CLOB,CLOSE,CLUSTER,CLUSTERED,COALESCE,COBOL,COLLATE,COLLATION,COLLATION_CATALOG,COLLATION_NAME,COLLATION_SCHEMA,COLLECT,COLUMN,COLUMN_NAME,COLUMNS,COMMAND_FUNCTION,COMMAND_FUNCTION_CODE,COMMENT,COMMIT,COMMITTED,COMPLETION,COMPRESS,COMPUTE,CONDITION,CONDITION_NUMBER,CONNECT,CONNECTION,CONNECTION_NAME,CONSTRAINT,CONSTRAINT_CATALOG,CONSTRAINT_NAME,CONSTRAINT_SCHEMA,CONSTRAINTS,CONSTRUCTOR,CONTAINS,CONTAINSTABLE,CONTINUE,CONVERSION,CONVERT,COPY,CORR,CORRESPONDING,COUNT,COVAR_POP,COVAR_SAMP,CREATE,CREATEDB,CREATEROLE,CREATEUSER,CROSS,CSV,CUBE,CUME_DIST,CURRENT,CURRENT_DATE,CURRENT_DEFAULT_TRANSFORM_GROUP,CURRENT_PATH,CURRENT_ROLE,CURRENT_TIME,CURRENT_TIMESTAMP,CURRENT_TRANSFORM_GROUP_FOR_TYPE,CURRENT_USER,CURSOR,CURSOR_NAME,CYCLE,DATA,DATABASE,DATABASES,DATE,DATETIME,DATETIME_INTERVAL_CODE,DATETIME_INTERVAL_PRECISION,DAY,DAY_HOUR,DAY_MICROSECOND,DAY_MINUTE,DAY_SECOND,DAYOFMONTH,DAYOFWEEK,DAYOFYEAR,DBCC,DEALLOCATE,DEC,DECIMAL,DECLARE,DEFAULT,DEFAULTS,DEFERRABLE,DEFERRED,DEFINED,DEFINER,DEGREE,DELAY_KEY_WRITE,DELAYED,DELETE,DELIMITER,DELIMITERS,DENSE_RANK,DENY,DEPTH,DEREF,DERIVED,DESC,DESCRIBE,DESCRIPTOR,DESTROY,DESTRUCTOR,DETERMINISTIC,DIAGNOSTICS,DICTIONARY,DISABLE,DISCONNECT,DISK,DISPATCH,DISTINCT,DISTINCTROW,DISTRIBUTED,DIV,DO,DOMAIN,DOUBLE,DROP,DUAL,DUMMY,DUMP,DYNAMIC,DYNAMIC_FUNCTION,DYNAMIC_FUNCTION_CODE,EACH,ELEMENT,ELSE,ELSEIF,ENABLE,ENCLOSED,ENCODING,ENCRYPTED,END,END-EXEC,ENUM,EQUALS,ERRLVL,ESCAPE,ESCAPED,EVERY,EXCEPT,EXCEPTION,EXCLUDE,EXCLUDING,EXCLUSIVE,EXEC,EXECUTE,EXISTING,EXISTS,EXIT,EXP,EXPLAIN,EXTERNAL,EXTRACT,FALSE,FETCH,FIELDS,FILE,FILLFACTOR,FILTER,FINAL,FIRST,FLOAT,FLOAT4,FLOAT8,FLOOR,FLUSH,FOLLOWING,FOR,FORCE,FOREIGN,FORTRAN,FORWARD,FOUND,FREE,FREETEXT,FREETEXTTABLE,FREEZE,FROM,FULL,FULLTEXT,FUNCTION,FUSION,G,GENERAL,GENERATED,GET,GLOBAL,GO,GOTO,GRANT,GRANTED,GRANTS,GREATEST,GROUP,GROUPING,HANDLER,HAVING,HEADER,HEAP,HIERARCHY,HIGH_PRIORITY,HOLD,HOLDLOCK,HOST,HOSTS,HOUR,HOUR_MICROSECOND,HOUR_MINUTE,HOUR_SECOND,IDENTIFIED,IDENTITY,IDENTITY_INSERT,IDENTITYCOL,IF,IGNORE,ILIKE,IMMEDIATE,IMMUTABLE,IMPLEMENTATION,IMPLICIT,IN,INCLUDE,INCLUDING,INCREMENT,INDEX,INDICATOR,INFILE,INFIX,INHERIT,INHERITS,INITIAL,INITIALIZE,INITIALLY,INNER,INOUT,INPUT,INSENSITIVE,INSERT,INSERT_ID,INSTANCE,INSTANTIABLE,INSTEAD,INT,INT1,INT2,INT3,INT4,INT8,INTEGER,INTERSECT,INTERSECTION,INTERVAL,INTO,INVOKER,IS,ISAM,ISNULL,ISOLATION,ITERATE,JOIN,K,KEY,KEY_MEMBER,KEY_TYPE,KEYS,KILL,LANCOMPILER,LANGUAGE,LARGE,LAST,LAST_INSERT_ID,LATERAL,LEAD,LEADING,LEAST,LEAVE,LEFT,LENGTH,LESS,LEVEL,LIKE,LIMIT,LINENO,LINES,LISTEN,LN,LOAD,LOCAL,LOCALTIME,LOCALTIMESTAMP,LOCATION,LOCATOR,LOCK,LOGIN,LOGS,LONG,LONGBLOB,LONGTEXT,LOOP,LOW_PRIORITY,LOWER,M,MAP,MATCH,MATCHED,MAX,MAX_ROWS,MAXEXTENTS,MAXVALUE,MEDIUMBLOB,MEDIUMINT,MEDIUMTEXT,MEMBER,MERGE,MESSAGE_LENGTH,MESSAGE_OCTET_LENGTH,MESSAGE_TEXT,METHOD,MIDDLEINT,MIN,MIN_ROWS,MINUS,MINUTE,MINUTE_MICROSECOND,MINUTE_SECOND,MINVALUE,MLSLABEL,MOD,MODE,MODIFIES,MODIFY,MODULE,MONTH,MONTHNAME,MORE,MOVE,MULTISET,MUMPS,MYISAM,NAME,NAMES,NATIONAL,NATURAL,NCHAR,NCLOB,NESTING,NEW,NEXT,NO,NO_WRITE_TO_BINLOG,NOAUDIT,NOCHECK,NOCOMPRESS,NOCREATEDB,NOCREATEROLE,NOCREATEUSER,NOINHERIT,NOLOGIN,NONCLUSTERED,NONE,NORMALIZE,NORMALIZED,NOSUPERUSER,NOT,NOTHING,NOTIFY,NOTNULL,NOWAIT,NULL,NULLABLE,NULLIF,NULLS,NUMBER,NUMERIC,OBJECT,OCTET_LENGTH,OCTETS,OF,OFF,OFFLINE,OFFSET,OFFSETS,OIDS,OLD,ON,ONLINE,ONLY,OPEN,OPENDATASOURCE,OPENQUERY,OPENROWSET,OPENXML,OPERATION,OPERATOR,OPTIMIZE,OPTION,OPTIONALLY,OPTIONS,OR,ORDER,ORDERING,ORDINALITY,OTHERS,OUT,OUTER,OUTFILE,OUTPUT,OVER,OVERLAPS,OVERLAY,OVERRIDING,OWNER,PACK_KEYS,PAD,PARAMETER,PARAMETER_MODE,PARAMETER_NAME,PARAMETER_ORDINAL_POSITION,PARAMETER_SPECIFIC_CATALOG,PARAMETER_SPECIFIC_NAME,PARAMETER_SPECIFIC_SCHEMA,PARAMETERS,PARTIAL,PARTITION,PASCAL,PASSWORD,PATH,PCTFREE,PERCENT,PERCENT_RANK,PERCENTILE_CONT,PERCENTILE_DISC,PLACING,PLAN,PLI,POSITION,POSTFIX,POWER,PRECEDING,PRECISION,PREFIX,PREORDER,PREPARE,PREPARED,PRESERVE,PRIMARY,PRINT,PRIOR,PRIVILEGES,PROC,PROCEDURAL,PROCEDURE,PROCESS,PROCESSLIST,PUBLIC,PURGE,QUOTE,RAID0,RAISERROR,RANGE,RANK,RAW,READ,READS,READTEXT,REAL,RECHECK,RECONFIGURE,RECURSIVE,REF,REFERENCES,REFERENCING,REGEXP,REGR_AVGX,REGR_AVGY,REGR_COUNT,REGR_INTERCEPT,REGR_R2,REGR_SLOPE,REGR_SXX,REGR_SXY,REGR_SYY,REINDEX,RELATIVE,RELEASE,RELOAD,RENAME,REPEAT,REPEATABLE,REPLACE,REPLICATION,REQUIRE,RESET,RESIGNAL,RESOURCE,RESTART,RESTORE,RESTRICT,RESULT,RETURN,RETURNED_CARDINALITY,RETURNED_LENGTH,RETURNED_OCTET_LENGTH,RETURNED_SQLSTATE,RETURNS,REVOKE,RIGHT,RLIKE,ROLE,ROLLBACK,ROLLUP,ROUTINE,ROUTINE_CATALOG,ROUTINE_NAME,ROUTINE_SCHEMA,ROW,ROW_COUNT,ROW_NUMBER,ROWCOUNT,ROWGUIDCOL,ROWID,ROWNUM,ROWS,RULE,SAVE,SAVEPOINT,SCALE,SCHEMA,SCHEMA_NAME,SCHEMAS,SCOPE,SCOPE_CATALOG,SCOPE_NAME,SCOPE_SCHEMA,SCROLL,SEARCH,SECOND,SECOND_MICROSECOND,SECTION,SECURITY,SELECT,SELF,SENSITIVE,SEPARATOR,SEQUENCE,SERIALIZABLE,SERVER_NAME,SESSION,SESSION_USER,SET,SETOF,SETS,SETUSER,SHARE,SHOW,SHUTDOWN,SIGNAL,SIMILAR,SIMPLE,SIZE,SMALLINT,SOME,SONAME,SOURCE,SPACE,SPATIAL,SPECIFIC,SPECIFIC_NAME,SPECIFICTYPE,SQL,SQL_BIG_RESULT,SQL_BIG_SELECTS,SQL_BIG_TABLES,SQL_CALC_FOUND_ROWS,SQL_LOG_OFF,SQL_LOG_UPDATE,SQL_LOW_PRIORITY_UPDATES,SQL_SELECT_LIMIT,SQL_SMALL_RESULT,SQL_WARNINGS,SQLCA,SQLCODE,SQLERROR,SQLEXCEPTION,SQLSTATE,SQLWARNING,SQRT,SSL,STABLE,START,STARTING,STATE,STATEMENT,STATIC,STATISTICS,STATUS,STDDEV_POP,STDDEV_SAMP,STDIN,STDOUT,STORAGE,STRAIGHT_JOIN,STRICT,STRING,STRUCTURE,STYLE,SUBCLASS_ORIGIN,SUBLIST,SUBMULTISET,SUBSTRING,SUCCESSFUL,SUM,SUPERUSER,SYMMETRIC,SYNONYM,SYSDATE,SYSID,SYSTEM,SYSTEM_USER,TABLE,TABLE_NAME,TABLES,TABLESAMPLE,TABLESPACE,TEMP,TEMPLATE,TEMPORARY,TERMINATE,TERMINATED,TEXT,TEXTSIZE,THAN,THEN,TIES,TIME,TIMESTAMP,TIMEZONE_HOUR,TIMEZONE_MINUTE,TINYBLOB,TINYINT,TINYTEXT,TO,TOAST,TOP,TOP_LEVEL_COUNT,TRAILING,TRAN,TRANSACTION,TRANSACTION_ACTIVE,TRANSACTIONS_COMMITTED,TRANSACTIONS_ROLLED_BACK,TRANSFORM,TRANSFORMS,TRANSLATE,TRANSLATION,TREAT,TRIGGER,TRIGGER_CATALOG,TRIGGER_NAME,TRIGGER_SCHEMA,TRIM,TRUE,TRUNCATE,TRUSTED,TSEQUAL,TYPE,UESCAPE,UID,UNBOUNDED,UNCOMMITTED,UNDER,UNDO,UNENCRYPTED,UNION,UNIQUE,UNKNOWN,UNLISTEN,UNLOCK,UNNAMED,UNNEST,UNSIGNED,UNTIL,UPDATE,UPDATETEXT,UPPER,USAGE,USE,USER,USER_DEFINED_TYPE_CATALOG,USER_DEFINED_TYPE_CODE,USER_DEFINED_TYPE_NAME,USER_DEFINED_TYPE_SCHEMA,USING,UTC_DATE,UTC_TIME,UTC_TIMESTAMP,VACUUM,VALID,VALIDATE,VALIDATOR,VALUE,VALUES,VAR_POP,VAR_SAMP,VARBINARY,VARCHAR,VARCHAR2,VARCHARACTER,VARIABLE,VARIABLES,VARYING,VERBOSE,VIEW,VOLATILE,WAITFOR,WHEN,WHENEVER,WHERE,WHILE,WIDTH_BUCKET,WINDOW,WITH,WITHIN,WITHOUT,WORK,WRITE,WRITETEXT,X509,XOR,YEAR,YEAR_MONTH,ZEROFILL,ZONE";

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected LimitFactory limitFactory;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    protected DatabaseMetaData databaseMetaData;

    @Value("${sequel.jdbc.formatsql:false}")
    protected Boolean formatSql;

    protected Class<M> modelClass;

    protected String sqlQuoteString;

    protected Set<String> sqlReservedWords;

    private String tableName;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected AbstractRepository(Class<M> modelClass) {
        if (modelClass == null) {
            Type t = getClass().getGenericSuperclass();
            ParameterizedType pt = (ParameterizedType) t;
            this.modelClass = (Class) pt.getActualTypeArguments()[0];
        } else {
            this.modelClass = modelClass;
        }
        tableName = getModelTableName();
    }

    protected AbstractRepository() {
        this(null);
    }

    @PostConstruct
    void init() {
        sqlQuoteString = getIdentifierQuoteString();
        sqlReservedWords = getSQLKeywords();
    }

    private String getModelTableName() {
        try {
            Method method = this.modelClass.getMethod("tableName");
            return (String) method.invoke(this.modelClass.getDeclaredConstructor().newInstance());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {
            throw new UnsupportedOperationException(
                    "Unable to find and invoke method tableName() of the underlying class", e);
        }
    }

    private String getIdentifierQuoteString() {
        try {
            return databaseMetaData.getIdentifierQuoteString();
        } catch (SQLException e) {
            return "";
        }
    }

    private Set<String> getSQLKeywords() {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, DEFAULT_SQL_RESERVED_WORDS.split(","));
        try {
            Collections.addAll(set, databaseMetaData.getSQLKeywords().toUpperCase().split(","));
        } catch (SQLException e) {
            // Do nothing
        }
        return set;
    }

    protected boolean isReserved(String word) {
        return sqlReservedWords.contains(word.toUpperCase());
    }

    protected String quoteIfReserved(String word) {
        return isReserved(word) ? sqlQuoteString + word + sqlQuoteString : word;
    }

    @Override
    public List<M> read() {
        return read(false);
    }

    @Override
    public List<M> read(boolean includeDeleted) {
        return read(null, includeDeleted);
    }

    @Override
    public List<M> read(Where where) {
        return read(where, false);
    }

    @Override
    public List<M> read(Where where, boolean includeDeleted) {
        return read(where, null, null, includeDeleted);
    }

    @Override
    public List<M> read(Where where, Order order) {
        return read(where, order, null, false);
    }

    @Override
    public List<M> read(Where where, Order order, boolean includeDeleted) {
        return read(where, order, null, includeDeleted);
    }

    @Override
    public List<M> read(Where where, Limit limit) {
        return read(where, null, limit, false);
    }

    @Override
    public List<M> read(Where where, Limit limit, boolean includeDeleted) {
        return read(where, null, limit, includeDeleted);
    }

    @Override
    public List<M> read(Where where, Order order, Limit limit) {
        return read(where, order, limit, false);
    }

    @Override
    public List<M> read(Where where, Order order, Limit limit, boolean includeDeleted) {
        return read(where, order, limit, includeDeleted, false);
    }

    private void printGeneratedSQL(String sql) {
        log.trace("Generated SQL: {}", Boolean.TRUE.equals(formatSql) ? "\n" + SQLUtils.format(sql) : sql);
    }

    @Override
    public List<M> read(Where where, Order order, Limit limit, boolean includeDeleted, boolean forUpdate) {
        if (where == null) {
            where = new Where();
        }
        List<Object> values = where.getValues();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(quoteIfReserved(tableName));
        if (!values.isEmpty()) {
            sb.append(where.getClause(this::quoteIfReserved));
        }
        if (!includeDeleted) {
            sb.append(values.isEmpty() ? " WHERE " : " AND ");
            sb.append(C_DELETED_AT);
            sb.append(" IS NULL ");
        }
        if (order != null) {
            sb.append(order.getClause(this::quoteIfReserved));
        }
        if (limit != null) {
            sb.append(limitFactory.getClause(limit));
        }
        if (forUpdate) {
            sb.append(" FOR UPDATE "); // Set table row locking
        }
        String sql = sb.toString();
        printGeneratedSQL(sql);
        return jdbcTemplate.query(sql, values.isEmpty() ? new Object[] {} : values.toArray(),
                BeanPropertyRowMapper.newInstance(modelClass));
    }

    @Override
    public List<M> readForUpdate() {
        return readForUpdate(false);
    }

    @Override
    public List<M> readForUpdate(boolean includeDeleted) {
        return readForUpdate(null, includeDeleted);
    }

    @Override
    public List<M> readForUpdate(Where where) {
        return readForUpdate(where, false);
    }

    @Override
    public List<M> readForUpdate(Where where, boolean includeDeleted) {
        return readForUpdate(where, null, null, includeDeleted);
    }

    @Override
    public List<M> readForUpdate(Where where, Limit limit) {
        return readForUpdate(where, limit, false);
    }

    @Override
    public List<M> readForUpdate(Where where, Limit limit, boolean includeDeleted) {
        return readForUpdate(where, null, limit, includeDeleted);
    }

    @Override
    public List<M> readForUpdate(Where where, Order order) {
        return readForUpdate(where, order, false);
    }

    @Override
    public List<M> readForUpdate(Where where, Order order, boolean includeDeleted) {
        return readForUpdate(where, order, null, includeDeleted);
    }

    @Override
    public List<M> readForUpdate(Where where, Order order, Limit limit) {
        return read(where, order, limit, false, true);
    }

    @Override
    public List<M> readForUpdate(Where where, Order order, Limit limit, boolean includeDeleted) {
        return read(where, order, limit, includeDeleted, true);
    }

    @Override
    public Optional<M> readOne(Long id) {
        return readOne(new Where().equals(C_ID, id), false);
    }

    @Override
    public Optional<M> readOne(Long id, boolean includeDeleted) {
        return readOne(new Where().equals(C_ID, id), includeDeleted);
    }

    @Override
    public Optional<M> readOne(Where where) {
        return readOne(where, null, false);
    }

    @Override
    public Optional<M> readOne(Where where, boolean includeDeleted) {
        return readOne(where, null, includeDeleted);
    }

    @Override
    public Optional<M> readOne(Where where, Order order) {
        return readOne(where, order, false);
    }

    @Override
    public Optional<M> readOne(Where where, Order order, boolean includeDeleted) {
        return readOne(where, order, includeDeleted, false);
    }

    @Override
    public Optional<M> readOne(Where where, Order order, boolean includeDeleted, boolean forUpdate) {
        List<M> list = read(where, order, new Limit(1), includeDeleted, forUpdate);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<M> readOneForUpdate(Long id) {
        return readOneForUpdate(new Where().equals(C_ID, id));
    }

    @Override
    public Optional<M> readOneForUpdate(Where where) {
        return readOneForUpdate(where, null);
    }

    @Override
    public Optional<M> readOneForUpdate(Where where, Order order) {
        return readOneForUpdate(where, order, false);
    }

    @Override
    public Optional<M> readOneForUpdate(Where where, Order order, boolean includeDeleted) {
        List<M> list = readForUpdate(where, order, new Limit(1), includeDeleted);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public int count(Where where, boolean includeDeleted) {
        if (where == null) {
            where = new Where();
        }
        List<Object> values = where.getValues();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(id) FROM ");
        sb.append(quoteIfReserved(tableName));
        if (!values.isEmpty()) {
            sb.append(where.getClause(this::quoteIfReserved));
        }
        if (!includeDeleted) {
            sb.append(values.isEmpty() ? " WHERE " : " AND ");
            sb.append(C_DELETED_AT);
            sb.append(" IS NULL ");
        }
        String sql = sb.toString();
        printGeneratedSQL(sql);
        return jdbcTemplate.queryForObject(sql, values.isEmpty() ? new Object[] {} : values.toArray(), Integer.class);
    }

    @Override
    public int count(Where where) {
        return count(where, false);
    }

    @Override
    public int count() {
        return count(null);
    }

    @Override
    public Long create(M model) {
        if (model == null) {
            throw new IllegalArgumentException("Data model must not be null");
        }
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Field field : FieldUtils.getFieldsWithAnnotation(modelClass, DataColumn.class)) {
            field.setAccessible(true);
            try {
                Object val = field.get(model);
                if (val != null) { // Get rid the field with null value from generated SQL
                    values.add(val);
                    columns.add(field.getAnnotation(DataColumn.class).value());
                }
            } catch (Exception e) {
                log.error("Failed to get value of field: {}: {}", field.getName(), e.getMessage());
            }
        }
        return executeInsert(columns.toArray(new String[columns.size()]), values.toArray());
    }

    private <T> List<T> toList(T[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    protected Long executeInsert(String[] columns, Object[] values) {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Array of columns and values must be in the same length");
        }
        Timestamp now = Timestamp.from(Instant.now());
        List<String> listColumns = toList(columns);
        List<Object> listValues = toList(values);
        int idxCreateDt = listColumns.indexOf(C_CREATED_AT);
        if (idxCreateDt != -1) {
            listValues.set(idxCreateDt, now);
        } else {
            listColumns.add(C_CREATED_AT);
            listValues.add(now);
        }
        int idxUpdateDt = listColumns.indexOf(C_UPDATED_AT);
        if (idxUpdateDt != -1) {
            listValues.set(idxUpdateDt, now);
        } else {
            listColumns.add(C_UPDATED_AT);
            listValues.add(now);
        }
        String sql = buildSqlInsert(tableName, listColumns).toString();
        printGeneratedSQL(sql);
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[] { "id" });
            for (int i = 0; i < listValues.size(); i++) {
                ps.setObject(i + 1, listValues.get(i));
            }
            return ps;
        }, holder);
        return holder.getKey().longValue();
    }

    private Long getGeneratedId(GeneratedKeyHolder holder) {
        String[] keyNames = new String[] { C_ID, "GENERATED_ID", "GENERATED_KEY" };
        Map<String, Object> keys = holder.getKeys();
        if (keys != null) {
            for (String name : keyNames) {
                Object key = keys.get(name);
                if (key != null) {
                    return (Long) key;
                }
            }
        }
        throw new NullPointerException("Cannot get generated id with keys: " + Arrays.toString(keyNames));
    }

    protected StringBuilder buildSqlInsert(String table, List<String> columns) {
        StringBuilder sb1 = new StringBuilder();
        sb1.append(" INSERT INTO ");
        sb1.append(quoteIfReserved(table));
        sb1.append(" ( ");
        StringBuilder sb2 = new StringBuilder();
        int i = 0;
        for (String name : columns) {
            sb1.append(quoteIfReserved(name));
            sb2.append("?");
            i++;
            if (i < columns.size()) {
                sb1.append(", ");
                sb2.append(", ");
            }
        }
        sb1.append(" ) ");
        sb1.append(" VALUES ( ");
        sb1.append(sb2);
        sb1.append(" ) ");
        return sb1;
    }

    @Override
    public boolean exists(Where where) {
        return count(where) > 0;
    }

    @Override
    public boolean exists(Long id) {
        return exists(new Where().equals(C_ID, id));
    }

    protected StringBuilder buildSqlUpdate(String table, List<String> columns, Where where) {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE ");
        sb.append(quoteIfReserved(table));
        sb.append(" SET ");
        int i = 0;
        for (String name : columns) {
            sb.append(quoteIfReserved(name));
            sb.append("=?");
            i++;
            if (i < columns.size()) {
                sb.append(", ");
            }
        }
        if (where != null) {
            sb.append(where.getClause(this::quoteIfReserved));
        }
        return sb;
    }

    @Override
    public Integer update(M model) {
        if (model == null) {
            throw new IllegalArgumentException("Data model must not be null");
        }
        if (model.getId() == null) {
            throw new IllegalArgumentException("Model id cannot be null");
        }
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Field field : FieldUtils.getFieldsWithAnnotation(modelClass, DataColumn.class)) {
            field.setAccessible(true);
            try {
                values.add(field.get(model));
                columns.add(field.getAnnotation(DataColumn.class).value());
            } catch (Exception e) {
                log.error("Failed to get value of field: {}: {}", field.getName(), e.getMessage());
            }
        }
        return update(columns.toArray(new String[columns.size()]), values.toArray(), model.getId());
    }

    @Override
    public Integer update(String[] columns, Object[] values, Where where) {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Array of columns and values must be in the same length");
        }
        Timestamp now = Timestamp.from(Instant.now());
        List<String> listColumns = toList(columns);
        List<Object> listValues = toList(values);
        int idxCreateDt = listColumns.indexOf(C_CREATED_AT);
        if (idxCreateDt != -1) {
            listColumns.remove(idxCreateDt);
            listValues.remove(idxCreateDt);
        }
        int idxUpdateDt = listColumns.indexOf(C_UPDATED_AT);
        if (idxUpdateDt != -1) {
            listValues.set(idxUpdateDt, now);
        } else {
            listColumns.add(C_UPDATED_AT);
            listValues.add(now);
        }
        if (where != null) {
            listValues.addAll(where.getValues());
        }
        String sql = buildSqlUpdate(tableName, listColumns, where).toString();
        printGeneratedSQL(sql);
        return jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < listValues.size(); i++) {
                ps.setObject(i + 1, listValues.get(i));
            }
            return ps;
        });
    }

    @Override
    public Integer update(String[] columns, Object[] values, Long id) {
        return update(columns, values, new Where().equals(C_ID, id));
    }

    @Override
    public Integer delete(Where where) {
        return delete(where, false);
    }

    @Override
    public Integer delete(Long id) {
        return delete(id, false);
    }

    @Override
    public Integer delete(M model) {
        return delete(model, false);
    }

    @Override
    public Integer delete(Where where, boolean force) {
        if (!force) {
            return update(new String[] { C_DELETED_AT }, new Object[] { Timestamp.from(Instant.now()) }, where);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM ");
        sb.append(quoteIfReserved(tableName));
        if (where != null) {
            sb.append(where.getClause(this::quoteIfReserved));
        }
        String sql = sb.toString();
        printGeneratedSQL(sql);
        return jdbcTemplate.update(sql, where == null ? new Object[] {} : where.getValues().toArray());
    }

    @Override
    public Integer delete(Long id, boolean force) {
        return delete(new Where().equals(C_ID, id), force);
    }

    @Override
    public Integer delete(M model, boolean force) {
        return delete(model.getId(), force);
    }

    @Override
    public Integer delete(List<Long> ids, boolean force) {
        return delete(new Where().in(C_ID, ids), force);
    }

    @Override
    public Integer delete(List<Long> ids) {
        return delete(ids, false);
    }

}
