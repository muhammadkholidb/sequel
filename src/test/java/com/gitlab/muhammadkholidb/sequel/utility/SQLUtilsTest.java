package com.gitlab.muhammadkholidb.sequel.utility;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.gitlab.muhammadkholidb.toolbox.data.ListBuilder;

import org.junit.jupiter.api.Test;

public class SQLUtilsTest {

    @Test
    void testFormat_withParamsList_shouldSucceed() {
        String result = SQLUtils.format(
                "select * from table where col1 = ? and col2 = ?",
                new ListBuilder<>().add("a").add("b").build());
        assertThat(result, is("select\n  *\nfrom\n  table\nwhere\n  col1 = a\n  and col2 = b"));
    }

    @Test
    void testFormat_withParamsListEmpty_shouldSucceed() {
        String result = SQLUtils.format("select * from table where col1 = ? and col2 = ?", null);
        assertThat(result, is("select\n  *\nfrom\n  table\nwhere\n  col1 = ?\n  and col2 = ?"));
    }

    @Test
    void testFormat_shouldSucceed() {
        String result = SQLUtils.format("select * from table where col1 = ? and col2 = ?");
        assertThat(result, is("select\n  *\nfrom\n  table\nwhere\n  col1 = ?\n  and col2 = ?"));
    }

}
