package com.gitlab.muhammadkholidb.jdbctemplatehelper.repository;

import static com.gitlab.muhammadkholidb.jdbctemplatehelper.model.DataModel.C_CREATED_AT;
import static com.gitlab.muhammadkholidb.jdbctemplatehelper.model.DataModel.C_DELETED_AT;
import static com.gitlab.muhammadkholidb.jdbctemplatehelper.model.DataModel.C_ID;
import static com.gitlab.muhammadkholidb.jdbctemplatehelper.model.DataModel.C_UPDATED_AT;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.annotation.DataColumn;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.model.DataModel;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.Limit;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.LimitFactory;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.Order;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.Where;

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

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected LimitFactory limitFactory;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${jdbc.formatsql:false}")
    protected Boolean formatSql;

    protected Class<M> modelClass;

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
        try {
            Method method = this.modelClass.getMethod("tableName");
            tableName = (String) method.invoke(this.modelClass.getDeclaredConstructor().newInstance());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {

            log.error("Cannot find and invoke method tableName() of the underlying class", e);
        }
    }

    protected AbstractRepository() {
        this(null);
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
        log.trace("Generated SQL: {}", Boolean.TRUE.equals(formatSql) ? "\n" + SqlFormatter.format(sql) : sql);
    }

    @Override
    public List<M> read(Where where, Order order, Limit limit, boolean includeDeleted, boolean forUpdate) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(tableName);
        if (where != null) {
            sb.append(where.getClause());
        }
        if (!includeDeleted) {
            sb.append(where == null ? " WHERE " : " AND ");
            sb.append(C_DELETED_AT);
            sb.append(" IS NULL ");
        }
        if (order != null) {
            sb.append(order.getClause());
        }
        if (limit != null) {
            sb.append(limitFactory.getClause(limit));
        }
        if (forUpdate) {
            sb.append(" FOR UPDATE "); // Set table row locking
        }
        String sql = sb.toString();
        printGeneratedSQL(sql);
        return jdbcTemplate.query(sql, where == null ? new Object[] {} : where.getValues().toArray(), BeanPropertyRowMapper.newInstance(modelClass));
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
    public int count(Where where, Order order, Limit limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(id) FROM ");
        sb.append(tableName);
        if (where != null) {
            sb.append(where.getClause());
        }
        sb.append(where == null ? " WHERE " : " AND ");
        sb.append(C_DELETED_AT);
        sb.append(" IS NULL ");
        if (order != null) {
            sb.append(order.getClause());
        }
        if (limit != null) {
            sb.append(limitFactory.getClause(limit));
        }
        String sql = sb.toString();
        printGeneratedSQL(sql);
        return jdbcTemplate.queryForObject(sql, where == null ? new Object[] {} : where.getValues().toArray(), Integer.class);
    }

    @Override
    public int count(Where where) {
        return count(where, null, null);
    }

    @Override
    public int count() {
        return count(null);
    }

    @Override
    public Optional<M> readOne(Long id) {
        return readOne(new Where().equals(C_ID, id), false);
    }

    @Override
    public Optional<M> readOne(Long id, boolean forUpdate) {
        return readOne(new Where().equals(C_ID, id), forUpdate);
    }

    @Override
    public Optional<M> readOne(Where where) {
        return readOne(where, null, false);
    }

    @Override
    public Optional<M> readOne(Where where, boolean forUpdate) {
        return readOne(where, null, forUpdate);
    }

    @Override
    public Optional<M> readOne(Where where, Order order) {
        return readOne(where, order, false);
    }

    @Override
    public Optional<M> readOne(Where where, Order order, boolean forUpdate) {
        List<M> list = read(where, order, new Limit(1), forUpdate);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
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
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < listValues.size(); i++) {
                ps.setObject(i + 1, listValues.get(i));
            }
            return ps;
        }, holder);
        return getGeneratedId(holder);
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
        sb1.append(table);
        sb1.append(" ( ");
        StringBuilder sb2 = new StringBuilder();
        int i = 0;
        for (String name : columns) {
            sb1.append(name);
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
        sb.append(table);
        sb.append(" SET ");
        int i = 0;
        for (String name : columns) {
            sb.append(name);
            sb.append("=?");
            i++;
            if (i < columns.size()) {
                sb.append(", ");
            }
        }
        if (where != null) {
            sb.append(where.getClause());
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
        sb.append(tableName);
        if (where != null) {
            sb.append(where.getClause());
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

}
