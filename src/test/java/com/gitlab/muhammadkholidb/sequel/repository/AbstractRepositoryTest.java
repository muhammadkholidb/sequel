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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.gitlab.muhammadkholidb.sequel.annotation.DataColumn;
import com.gitlab.muhammadkholidb.sequel.model.DataModel;
import com.gitlab.muhammadkholidb.sequel.sql.Limit;
import com.gitlab.muhammadkholidb.sequel.sql.Order;
import com.gitlab.muhammadkholidb.sequel.sql.Order.Direction;
import com.gitlab.muhammadkholidb.sequel.sql.Where;
import com.gitlab.muhammadkholidb.toolbox.data.ListBuilder;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@DatabaseSetup("/datasets/table.xml")
public class AbstractRepositoryTest extends RepositoryTestBase {

    @Autowired
    private TableRepository tableRepository;

    @Test
    void testRead_shouldSucceed() {
        List<Table> result = tableRepository.read();
        assertThat(result, hasSize(3));
        assertThat(
                result,
                contains(
                        getTableMatchers(1, 4, "T100", false),
                        getTableMatchers(2, 2, "T200", false),
                        getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testRead_where_shouldSucceed() {
        List<Table> result = tableRepository.read(new Where().equals("limit", 4).andEqualsIgnoreCase("code", "T100"));
        assertThat(result, hasSize(1));
        assertThat(result, contains(getTableMatchers(1, 4, "T100", false)));
    }

    @Test
    void testRead_where_limit_shouldSucceed() {
        List<Table> result = tableRepository.read(new Where().startsWith("code", "T1"), new Limit(2));
        assertThat(result, hasSize(1));
        assertThat(result, contains(getTableMatchers(1, 4, "T100", false)));
    }

    @Test
    void testRead_where_limit_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.read(new Where().contains("code", "300"), new Limit(100), true);
        assertThat(result, hasSize(2));
        assertThat(
                result,
                containsInAnyOrder(getTableMatchers(4, 3, "T300", false), getTableMatchers(3, 3, "T300", true)));
    }

    @Test
    void testRead_where_order_shouldSucceed() {
        List<Table> result = tableRepository
                .read(new Where().greaterThan("id", 1), new Order().by("limit", Direction.DESCENDING));
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(4, 3, "T300", false), getTableMatchers(2, 2, "T200", false)));
    }

    @Test
    void testRead_where_order_limit_shouldSucceed() {
        List<Table> result = tableRepository
                .read(new Where().lowerThan("id", 3), new Order().by("limit", Direction.DESCENDING), new Limit(1, 1));
        assertThat(result, hasSize(1));
        assertThat(result, contains(getTableMatchers(2, 2, "T200", false)));
    }

    @Test
    void testRead_where_order_limit_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.read(
                new Where().greaterThanOrEqual("id", 3),
                new Order().by("id", Direction.DESCENDING),
                new Limit(10),
                true);
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(4, 3, "T300", false), getTableMatchers(3, 3, "T300", true)));
    }

    @Test
    void testRead_where_order_limit_includeDeleted_forUpdate_shouldSucceed() {
        List<Table> result = tableRepository.read(
                new Where().lowerThanOrEqual("id", 3),
                new Order().by("id", Direction.DESCENDING),
                new Limit(1),
                true,
                true);
        assertThat(result, hasSize(1));
        assertThat(result, contains(getTableMatchers(3, 3, "T300", true)));
    }

    @Test
    void testRead_where_order_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository
                .read(new Where().in("id", new ListBuilder<>().add(3).add(4).build()), new Order().by("id"), true);
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(3, 3, "T300", true), getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testRead_where_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.read(new Where().equals("limit", 3), true);
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(3, 3, "T300", true), getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testRead_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.read(true);
        assertThat(result, hasSize(4));
        assertThat(
                result,
                contains(
                        getTableMatchers(1, 4, "T100", false),
                        getTableMatchers(2, 2, "T200", false),
                        getTableMatchers(3, 3, "T300", true),
                        getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testReadForUpdate_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate();
        assertThat(result, hasSize(3));
        assertThat(
                result,
                contains(
                        getTableMatchers(1, 4, "T100", false),
                        getTableMatchers(2, 2, "T200", false),
                        getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testReadForUpdate_where_shouldSucceed() {
        List<Table> result = tableRepository
                .readForUpdate(new Where().equals("limit", 4).andEqualsIgnoreCase("code", "T100"));
        assertThat(result, hasSize(1));
        assertThat(result, contains(getTableMatchers(1, 4, "T100", false)));
    }

    @Test
    void testReadForUpdate_where_limit_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate(new Where().startsWith("code", "T1"), new Limit(2));
        assertThat(result, hasSize(1));
        assertThat(result, contains(getTableMatchers(1, 4, "T100", false)));
    }

    @Test
    void testReadForUpdate_where_limit_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate(new Where().contains("code", "300"), new Limit(100), true);
        assertThat(result, hasSize(2));
        assertThat(
                result,
                containsInAnyOrder(getTableMatchers(4, 3, "T300", false), getTableMatchers(3, 3, "T300", true)));
    }

    @Test
    void testReadForUpdate_where_order_shouldSucceed() {
        List<Table> result = tableRepository
                .readForUpdate(new Where().greaterThan("id", 1), new Order().by("limit", Direction.DESCENDING));
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(4, 3, "T300", false), getTableMatchers(2, 2, "T200", false)));
    }

    @Test
    void testReadForUpdate_where_order_limit_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate(
                new Where().lowerThan("id", 3),
                new Order().by("limit", Direction.DESCENDING),
                new Limit(1, 1));
        assertThat(result, hasSize(1));
        assertThat(result, contains(getTableMatchers(2, 2, "T200", false)));
    }

    @Test
    void testReadForUpdate_where_order_limit_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate(
                new Where().greaterThanOrEqual("id", 3),
                new Order().by("id", Direction.DESCENDING),
                new Limit(10),
                true);
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(4, 3, "T300", false), getTableMatchers(3, 3, "T300", true)));
    }

    @Test
    void testReadForUpdate_where_order_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate(
                new Where().in("id", new ListBuilder<>().add(3).add(4).build()),
                new Order().by("id"),
                true);
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(3, 3, "T300", true), getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testReadForUpdate_where_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate(new Where().equals("limit", 3), true);
        assertThat(result, hasSize(2));
        assertThat(result, contains(getTableMatchers(3, 3, "T300", true), getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testReadForUpdate_includeDeleted_shouldSucceed() {
        List<Table> result = tableRepository.readForUpdate(true);
        assertThat(result, hasSize(4));
        assertThat(
                result,
                contains(
                        getTableMatchers(1, 4, "T100", false),
                        getTableMatchers(2, 2, "T200", false),
                        getTableMatchers(3, 3, "T300", true),
                        getTableMatchers(4, 3, "T300", false)));
    }

    @Test
    void testReadOne_shouldSucceed() {
        Optional<Table> result = tableRepository.readOne(3l);
        assertThat(result.isPresent(), is(false));
    }

    @Test
    void testReadOne_includeDeleted_shouldSucceed() {
        Optional<Table> result = tableRepository.readOne(3l, true);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(3, 3, "T300", true));
    }

    @Test
    void testReadOne_where_shouldSucceed() {
        Optional<Table> result = tableRepository
                .readOne(new Where().equals("limit", 4).andEqualsIgnoreCase("code", "T100"));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(1, 4, "T100", false));
    }

    @Test
    void testReadOne_where_order_shouldSucceed() {
        Optional<Table> result = tableRepository
                .readOne(new Where().greaterThan("id", 1), new Order().by("limit", Direction.DESCENDING));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(4, 3, "T300", false));
    }

    @Test
    void testReadOne_where_order_includeDeleted_shouldSucceed() {
        Optional<Table> result = tableRepository
                .readOne(new Where().in("id", new ListBuilder<>().add(3).add(4).build()), new Order().by("id"), true);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(3, 3, "T300", true));
    }

    @Test
    void testReadOne_where_order_includeDeleted_forUpdate_shouldSucceed() {
        Optional<Table> result = tableRepository.readOne(
                new Where().in("id", new ListBuilder<>().add(3).add(4).build()),
                new Order().by("id"),
                true,
                true);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(3, 3, "T300", true));
    }

    @Test
    void testReadOne_where_includeDeleted_shouldSucceed() {
        Optional<Table> result = tableRepository
                .readOne(new Where().equals("limit", 3).andEqualsIgnoreCase("code", "T300"), true);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(3, 3, "T300", true));
    }

    @Test
    void testReadOneForUpdate_shouldSucceed() {
        Optional<Table> result = tableRepository.readOneForUpdate(3l);
        assertThat(result.isPresent(), is(false));
    }

    @Test
    void testReadOneForUpdate_where_shouldSucceed() {
        Optional<Table> result = tableRepository
                .readOneForUpdate(new Where().equals("limit", 4).andEqualsIgnoreCase("code", "T100"));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(1, 4, "T100", false));
    }

    @Test
    void testReadOneForUpdate_where_order_shouldSucceed() {
        Optional<Table> result = tableRepository
                .readOneForUpdate(new Where().greaterThan("id", 1), new Order().by("limit", Direction.DESCENDING));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(4, 3, "T300", false));
    }

    @Test
    void testReadOneForUpdate_where_order_includeDeleted_shouldSucceed() {
        Optional<Table> result = tableRepository.readOneForUpdate(
                new Where().in("id", new ListBuilder<>().add(3).add(4).build()),
                new Order().by("id"),
                true);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), getTableMatchers(3, 3, "T300", true));
    }

    @Test
    void testCount_shouldSucceed() {
        int result = tableRepository.count();
        assertThat(result, is(3));
    }

    @Test
    void testCount_where_shouldSucceed() {
        int result = tableRepository.count(new Where().notEquals("code", "T200"));
        assertThat(result, is(2));
    }

    @Test
    void testCount_where_includeDeleted_shouldSucceed() {
        int result = tableRepository.count(new Where(), true);
        assertThat(result, is(4));
    }

    @Test
    void testExists_shouldSucceed() {
        boolean result = tableRepository.exists(3l);
        assertThat(result, is(false));
    }

    @Test
    void testExists_where_shouldSucceed() {
        boolean result = tableRepository.exists(new Where().equals("limit", 3));
        assertThat(result, is(true));
    }

    @Test
    void testReadOne_where_withJavaTime_shouldSucceed() {
        Optional<Table> result = tableRepository.readOne(
                new Where().lowerThan("created_at", Instant.now()).andLowerThan("zdt", ZonedDateTime.now())
                        .andLowerThan("odt", OffsetDateTime.now()).andLowerThan("ldt", LocalDateTime.now())
                        .andEquals("ld", LocalDate.of(2021, 4, 1)).andEquals("lt", LocalTime.of(21, 0, 0))
                        .andEquals("ot", OffsetTime.of(LocalTime.of(20, 0, 0), ZonedDateTime.now().getOffset())));
        assertThat(result.isPresent(), is(true));
    }

    private Matcher<Table> getTableMatchers(long id, int limit, String code, boolean isDeleted) {
        return allOf(
                hasProperty("id", is(id)),
                hasProperty("limit", is(limit)),
                hasProperty("code", is(code)),
                hasProperty("deletedAt", isDeleted ? notNullValue() : nullValue()));
    }

    @Repository
    static class TableRepository extends AbstractRepository<Table> {

    }

    @Setter
    @Getter
    @ToString(callSuper = true)
    public static class Table extends DataModel {

        @DataColumn("limit")
        private Integer limit;

        @DataColumn("code")
        private String code;

        @DataColumn("zdt")
        private ZonedDateTime zdt;

        @DataColumn("odt")
        private OffsetDateTime odt;

        @DataColumn("ot")
        private OffsetTime ot;

        @DataColumn("ldt")
        private LocalDateTime ldt;

        @DataColumn("ld")
        private LocalDate ld;

        @DataColumn("lt")
        private LocalTime lt;

        @Override
        public String tableName() {
            return "table";
        }

    }

}
