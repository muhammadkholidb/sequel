package com.gitlab.muhammadkholidb.sequel.utility;

import com.gitlab.muhammadkholidb.toolbox.data.ListBuilder;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class SQLUtilTest {

    @Test
    void testFormat_withParamsList_shouldSucceed() {
        String result = SQLUtils.format("select * from table where col1 = ? and col2 = ?",
                new ListBuilder<String>().add("a").add("b").build());
        MatcherAssert.assertThat(result, Matchers.is("select\n  *\nfrom\n  table\nwhere\n  col1 = a\n  and col2 = b"));
    }

    @Test
    void testFormat_withParamsListEmpty_shouldSucceed() {
        String result = SQLUtils.format("select * from table where col1 = ? and col2 = ?", null);
        MatcherAssert.assertThat(result, Matchers.is("select\n  *\nfrom\n  table\nwhere\n  col1 = ?\n  and col2 = ?"));
    }

    @Test
    void testFormat_shouldSucceed() {
        String result = SQLUtils.format("select * from table where col1 = ? and col2 = ?");
        MatcherAssert.assertThat(result, Matchers.is("select\n  *\nfrom\n  table\nwhere\n  col1 = ?\n  and col2 = ?"));
    }

}
