package com.gitlab.muhammadkholidb.jdbctemplatehelper.repository;

import java.util.List;
import java.util.Optional;

import com.gitlab.muhammadkholidb.jdbctemplatehelper.model.DataModel;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.Limit;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.Order;
import com.gitlab.muhammadkholidb.jdbctemplatehelper.sql.Where;

/**
 * 
 * @author muhammad
 * @param <M>
 */
public interface CommonRepository<M extends DataModel> {

    /**
     * Inserts a new data to the table name defined in the model (SQL INSERT).
     * 
     * @param model a data model.
     * @return generated ID.
     */
    Long create(M model);

    /**
     * Reads all data in the table name defined in the model (SQL SELECT). Data with
     * null value on the column deleted_at will be returned.
     * 
     * @return list of data models.
     */
    List<M> read();

    List<M> read(boolean includeDeleted);

    List<M> read(Where where);

    List<M> read(Where where, boolean includeDeleted);

    List<M> read(Where where, Order order);

    List<M> read(Where where, Order order, boolean includeDeleted);

    List<M> read(Where where, Limit limit);

    List<M> read(Where where, Limit limit, boolean includeDeleted);

    List<M> read(Where where, Order order, Limit limit);

    List<M> read(Where where, Order order, Limit limit, boolean includeDeleted);

    List<M> read(Where where, Order order, Limit limit, boolean includeDeleted, boolean forUpdate);

    List<M> readForUpdate();

    List<M> readForUpdate(boolean includeDeleted);

    List<M> readForUpdate(Where where);

    List<M> readForUpdate(Where where, boolean includeDeleted);

    List<M> readForUpdate(Where where, Order order);

    List<M> readForUpdate(Where where, Order order, boolean includeDeleted);

    List<M> readForUpdate(Where where, Limit limit);

    List<M> readForUpdate(Where where, Limit limit, boolean includeDeleted);

    List<M> readForUpdate(Where where, Order order, Limit limit);

    List<M> readForUpdate(Where where, Order order, Limit limit, boolean includeDeleted);

    Optional<M> readOne(Long id);

    Optional<M> readOne(Long id, boolean forUpdate);

    Optional<M> readOne(Where where);

    Optional<M> readOne(Where where, boolean forUpdate);

    Optional<M> readOne(Where where, Order order);

    Optional<M> readOne(Where where, Order order, boolean forUpdate);

    Integer update(M model);

    Integer update(String[] columns, Object[] values, Long id);

    Integer update(String[] columns, Object[] values, Where where);

    Integer delete(M model);

    Integer delete(Where where);

    Integer delete(Long id);

    Integer delete(M model, boolean force);

    Integer delete(Where where, boolean force);

    Integer delete(Long id, boolean force);

    int count(Where where, Order order, Limit limit);

    int count(Where where);

    int count();

    boolean exists(Long id);

    boolean exists(Where where);
}
