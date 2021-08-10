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

import org.apache.commons.lang3.StringUtils;
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
     * ISO 9075 SQL Standard Keywords/Reserved Words. Taken from
     * https://firebirdsql.org/en/iso-9075-sql-standard-keywords-reserved-words/
     */
    public static final String DEFAULT_SQL_RESERVED_WORDS = "ABSOLUTE,ACTION,ADD,AFTER,ALL,ALLOCATE,ALTER,AND,ANY,ARE,ARRAY,"
            + "AS,ASC,ASENSITIVE,ASSERTION,ASYMMETRIC,AT,ATOMIC,AUTHORIZATION,AVG,BEFORE,BEGIN,BETWEEN,BIGINT,BINARY,BIT,"
            + "BIT_LENGTH,BLOB,BOOLEAN,BOTH,BREADTH,BY,CALL,CALLED,CASCADE,CASCADED,CASE,CAST,CATALOG,CHAR,CHAR_LENGTH,"
            + "CHARACTER,CHARACTER_LENGTH,CHECK,CLOB,CLOSE,COALESCE,COLLATE,COLLATION,COLUMN,COMMIT,CONDITION,CONNECT,"
            + "CONNECTION,CONSTRAINT,CONSTRAINTS,CONSTRUCTOR,CONTAINS,CONTINUE,CONVERT,CORRESPONDING,COUNT,CREATE,CROSS,CUBE,"
            + "CURRENT,CURRENT_DATE,CURRENT_DEFAULT_TRANSFORM_GROUP,CURRENT_PATH,CURRENT_ROLE,CURRENT_TIME,CURRENT_TIMESTAMP,"
            + "CURRENT_TRANSFORM_GROUP_FOR_TYPE,CURRENT_USER,CURSOR,CYCLE,DATA,DATE,DAY,DEALLOCATE,DEC,DECIMAL,DECLARE,DEFAULT,"
            + "DEFERRABLE,DEFERRED,DELETE,DEPTH,DEREF,DESC,DESCRIBE,DESCRIPTOR,DETERMINISTIC,DIAGNOSTICS,DISCONNECT,DISTINCT,"
            + "DO,DOMAIN,DOUBLE,DROP,DYNAMIC,EACH,ELEMENT,ELSE,ELSEIF,END,EQUALS,ESCAPE,EXCEPT,EXCEPTION,EXEC,EXECUTE,EXISTS,"
            + "EXIT,EXTERNAL,EXTRACT,FALSE,FETCH,FILTER,FIRST,FLOAT,FOR,FOREIGN,FOUND,FREE,FROM,FULL,FUNCTION,GENERAL,GET,GLOBAL,"
            + "GO,GOTO,GRANT,GROUP,GROUPING,HANDLER,HAVING,HOLD,HOUR,IDENTITY,IF,IMMEDIATE,IN,INDICATOR,INITIALLY,INNER,INOUT,"
            + "INPUT,INSENSITIVE,INSERT,INT,INTEGER,INTERSECT,INTERVAL,INTO,IS,ISOLATION,ITERATE,JOIN,KEY,LANGUAGE,LARGE,LAST,"
            + "LATERAL,LEADING,LEAVE,LEFT,LEVEL,LIKE,LOCAL,LOCALTIME,LOCALTIMESTAMP,LOCATOR,LOOP,LOWER,MAP,MATCH,MAX,MEMBER,"
            + "MERGE,METHOD,MIN,MINUTE,MODIFIES,MODULE,MONTH,MULTISET,NAMES,NATIONAL,NATURAL,NCHAR,NCLOB,NEW,NEXT,NO,NONE,NOT,"
            + "NULL,NULLIF,NUMERIC,OBJECT,OCTET_LENGTH,OF,OLD,ON,ONLY,OPEN,OPTION,OR,ORDER,ORDINALITY,OUT,OUTER,OUTPUT,OVER,"
            + "OVERLAPS,PAD,PARAMETER,PARTIAL,PARTITION,PATH,POSITION,PRECISION,PREPARE,PRESERVE,PRIMARY,PRIOR,PRIVILEGES,"
            + "PROCEDURE,PUBLIC,RANGE,READ,READS,REAL,RECURSIVE,REF,REFERENCES,REFERENCING,RELATIVE,RELEASE,REPEAT,RESIGNAL,"
            + "RESTRICT,RESULT,RETURN,RETURNS,REVOKE,RIGHT,ROLE,ROLLBACK,ROLLUP,ROUTINE,ROW,ROWS,SAVEPOINT,SCHEMA,SCOPE,SCROLL,"
            + "SEARCH,SECOND,SECTION,SELECT,SENSITIVE,SESSION,SESSION_USER,SET,SETS,SIGNAL,SIMILAR,SIZE,SMALLINT,SOME,SPACE,"
            + "SPECIFIC,SPECIFICTYPE,SQL,SQLCODE,SQLERROR,SQLEXCEPTION,SQLSTATE,SQLWARNING,START,STATE,STATIC,SUBMULTISET,"
            + "SUBSTRING,SUM,SYMMETRIC,SYSTEM,SYSTEM_USER,TABLE,TABLESAMPLE,TEMPORARY,THEN,TIME,TIMESTAMP,TIMEZONE_HOUR,"
            + "TIMEZONE_MINUTE,TO,TRAILING,TRANSACTION,TRANSLATE,TRANSLATION,TREAT,TRIGGER,TRIM,TRUE,UNDER,UNDO,UNION,UNIQUE,"
            + "UNKNOWN,UNNEST,UNTIL,UPDATE,UPPER,USAGE,USER,USING,VALUE,VALUES,VARCHAR,VARYING,VIEW,WHEN,WHENEVER,WHERE,WHILE,"
            + "WINDOW,WITH,WITHIN,WITHOUT,WORK,WRITE,YEAR,ZONE";

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

    @Value("${sequel.jdbc.tableschema:}")
    protected String tableSchema;

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
        loadSqlQuoteString();
        loadSqlReservedWords();
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

    private void loadSqlQuoteString() {
        try {
            sqlQuoteString = databaseMetaData.getIdentifierQuoteString().trim();
        } catch (SQLException e) {
            sqlQuoteString = "";
        }
    }

    private void loadSqlReservedWords() {
        sqlReservedWords = new HashSet<>();
        Collections.addAll(sqlReservedWords, DEFAULT_SQL_RESERVED_WORDS.split(","));
        try {
            Collections.addAll(sqlReservedWords, databaseMetaData.getSQLKeywords().toUpperCase().split(","));
        } catch (SQLException e) {
            // Do nothing
        }
    }

    protected boolean isReserved(String word) {
        return sqlReservedWords.contains(word.toUpperCase());
    }

    protected String quoteIfReserved(String word) {
        return isReserved(word) ? quote(word) : word;
    }

    /**
     * Quotes the word with database specific quote string. Might be quoted by empty
     * string if no quote string spcified by the database.
     * 
     * @param word the word to be quoted with
     * @return the quoted word
     */
    protected String quote(String word) {
        return sqlQuoteString + word + sqlQuoteString;
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
        if (StringUtils.isNotBlank(tableSchema)) {
            sb.append(quoteIfReserved(tableSchema));
            sb.append(".");
        }
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
        if (StringUtils.isNotBlank(tableSchema)) {
            sb.append(quoteIfReserved(tableSchema));
            sb.append(".");
        }
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
        if (StringUtils.isNotBlank(tableSchema)) {
            sb1.append(quoteIfReserved(tableSchema));
            sb1.append(".");
        }
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
        if (StringUtils.isNotBlank(tableSchema)) {
            sb.append(quoteIfReserved(tableSchema));
            sb.append(".");
        }
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
        if (StringUtils.isNotBlank(tableSchema)) {
            sb.append(quoteIfReserved(tableSchema));
            sb.append(".");
        }
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
