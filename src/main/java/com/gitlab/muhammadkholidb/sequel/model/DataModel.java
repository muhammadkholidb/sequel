package com.gitlab.muhammadkholidb.sequel.model;

import java.time.LocalDateTime;

import com.gitlab.muhammadkholidb.sequel.annotation.DataColumn;

import lombok.Data;

/**
 * @author muhammad
 */
@Data
public abstract class DataModel {

    public static final String C_ID = "id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_UPDATED_AT = "updated_at";
    public static final String C_DELETED_AT = "deleted_at";

    @DataColumn(C_ID)
    private Long id;

    @DataColumn(C_CREATED_AT)
    private LocalDateTime createdAt;

    @DataColumn(C_UPDATED_AT)
    private LocalDateTime updatedAt;

    @DataColumn(C_DELETED_AT)
    private LocalDateTime deletedAt;

    public abstract String tableName();

}
