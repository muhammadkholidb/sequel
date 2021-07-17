package com.gitlab.muhammadkholidb.sequel.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gitlab.muhammadkholidb.sequel.annotation.DataColumn;
import com.gitlab.muhammadkholidb.sequel.model.DataModel;
import com.gitlab.muhammadkholidb.sequel.sql.Limit;
import com.gitlab.muhammadkholidb.sequel.sql.Where;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@DataSet("table.yml")
public class AbstractRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private TableRepository tableRepository;

    // @formatter:off
    @Test
    public void testRead_shouldSucceed() {
        List<Table> result = tableRepository.read();
        assertThat(result, hasSize(3));
        assertThat(result,
                contains(
                        allOf(
                            hasProperty("id", is(1l)), 
                            hasProperty("number", is(100)),
                            hasProperty("code", is("T100")), 
                            hasProperty("deletedAt", nullValue())),
                        allOf(
                            hasProperty("id", is(2l)), 
                            hasProperty("number", is(200)),
                            hasProperty("code", is("T200")), 
                            hasProperty("deletedAt", nullValue())),
                        allOf(
                            hasProperty("id", is(3l)), 
                            hasProperty("number", is(300)),
                            hasProperty("code", is("T300")), 
                            hasProperty("deletedAt", nullValue()))));
    }

    @Test
    public void testRead_where_shouldSucceed() {
        List<Table> result = tableRepository
                .read(new Where().equals("number", 100).andEqualsIgnoreCase("code", "T100"));
        assertThat(result, hasSize(1));
        assertThat(result, contains(
            allOf(
                hasProperty("id", is(1l)), 
                hasProperty("number", is(100)),
                hasProperty("code", is("T100")), 
                hasProperty("deletedAt", nullValue()))));
    }

    @Test
    public void testRead_where_limit_shouldSucceed() {
        List<Table> result = tableRepository.read(new Where().startsWith("code", "T1"), new Limit(2));
        assertThat(result, hasSize(1));
        assertThat(result, contains(
            allOf(
                hasProperty("id", is(1l)), 
                hasProperty("number", is(100)),
                hasProperty("code", is("T100")), 
                hasProperty("deletedAt", nullValue()))));
    }

    @Test
    public void testRead_where_limit_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.read(new Where().contains("code", "300"), new Limit(100), true);
        assertThat(result, hasSize(2));
        assertThat(result, 
                containsInAnyOrder(
                        allOf(
                            hasProperty("id", is(4l)), 
                            hasProperty("number", is(300)),
                            hasProperty("code", is("T300")), 
                            hasProperty("deletedAt", notNullValue())),
                        allOf(
                            hasProperty("id", is(3l)), 
                            hasProperty("number", is(300)),
                            hasProperty("code", is("T300")), 
                            hasProperty("deletedAt", nullValue()))));
    }

    @Repository
    public static class TableRepository extends AbstractRepository<Table> {

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Table extends DataModel {

        @DataColumn("number")
        private Integer number;

        @DataColumn("code")
        private String code;

        @Override
        public String tableName() {
            return "table";
        }

    }

}