package com.gitlab.muhammadkholidb.sequel.repository;

import java.util.List;
import java.util.Optional;

import com.gitlab.muhammadkholidb.sequel.model.DataModel;
import com.gitlab.muhammadkholidb.sequel.sql.Limit;
import com.gitlab.muhammadkholidb.sequel.sql.Order;
import com.gitlab.muhammadkholidb.sequel.sql.Where;

/**
 * 
 * @author muhammad
 * @param <M>
 */
public interface CommonRepository<M extends DataModel> {

    /**
     * Inserts a new data to the table.
     * 
     * @param model a data model
     * @return generated ID
     */
    Long create(M model);

    /**
     * Reads all data in the table. Returns data which is not (softly) deleted.
     * 
     * @return list of data models
     */
    List<M> read();

    /**
     * Reads all data in the table with an option to include (softly) deleted data.
     * 
     * @param includeDeleted will include the (softly) deleted data when the value
     *                       is true
     * @return list of data models
     */
    List<M> read(boolean includeDeleted);

    /**
     * Reads all data in the table filtered with specified SQL WHERE condition.
     * Returns data which is not (softly) deleted.
     * 
     * @param where the SQL WHERE condition
     * @return list of data models
     */
    List<M> read(Where where);

    /**
     * Reads all data in the table filtered with specified SQL WHERE condition and
     * an option to include (softly) deleted data.
     * 
     * @param where          the SQL WHERE condition
     * @param includeDeleted wili include the (softly) deleted data when the value
     *                       is true
     * @return list of data models
     */
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

    Optional<M> readOne(Long id, boolean includeDeleted);

    Optional<M> readOne(Where where);

    Optional<M> readOne(Where where, boolean includeDeleted);

    Optional<M> readOne(Where where, Order order);

    Optional<M> readOne(Where where, Order order, boolean includeDeleted);

    Optional<M> readOne(Where where, Order order, boolean includeDeleted, boolean forUpdate);

    Optional<M> readOneForUpdate(Where where, Order order, boolean includeDeleted);

    Optional<M> readOneForUpdate(Where where, Order order);

    Optional<M> readOneForUpdate(Where where);

    Optional<M> readOneForUpdate(Long id);

    Integer update(M model);

    Integer update(String[] columns, Object[] values, Long id);

    Integer update(String[] columns, Object[] values, Where where);

    Integer delete(M model);

    Integer delete(Where where);

    Integer delete(Long id);

    Integer delete(M model, boolean force);

    Integer delete(Where where, boolean force);

    Integer delete(Long id, boolean force);

    Integer delete(List<Long> ids);

    Integer delete(List<Long> ids, boolean force);

    int count(Where where, boolean includeDeleted);

    int count(Where where);

    int count();

    boolean exists(Long id);

    boolean exists(Where where);
}
