package com.gitlab.muhammadkholidb.sequel.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

import com.gitlab.muhammadkholidb.toolbox.data.DateTimeUtils;

import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;

public class CustomArgumentPreparedStatementSetter extends ArgumentPreparedStatementSetter {

    public CustomArgumentPreparedStatementSetter(Object[] args) {
        super(args);
    }

    @Override
    protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
        if (argValue instanceof Instant) {
            super.doSetValue(ps, parameterPosition,
                    new SqlParameterValue(Types.TIMESTAMP, Timestamp.from((Instant) argValue)));
            return;
        }
        if (argValue instanceof LocalDate) {
            LocalDate ld = (LocalDate) argValue;
            super.doSetValue(ps, parameterPosition,
                    new SqlParameterValue(Types.DATE, new Date(DateTimeUtils.toInstant(ld).toEpochMilli())));
            return;
        }
        if (argValue instanceof LocalDateTime) {
            LocalDateTime ldt = (LocalDateTime) argValue;
            super.doSetValue(ps, parameterPosition,
                    new SqlParameterValue(Types.TIMESTAMP, Timestamp.from(DateTimeUtils.toInstant(ldt))));
            return;
        }
        if (argValue instanceof LocalTime) {
            LocalTime lt = (LocalTime) argValue;
            super.doSetValue(ps, parameterPosition,
                    new SqlParameterValue(Types.TIME, new Time(DateTimeUtils.toInstant(lt).toEpochMilli())));
            return;
        }
        if (argValue instanceof OffsetDateTime) {
            OffsetDateTime odt = (OffsetDateTime) argValue;
            super.doSetValue(ps, parameterPosition,
                    new SqlParameterValue(Types.TIMESTAMP, Timestamp.from(odt.toInstant())));
            return;
        }
        if (argValue instanceof OffsetTime) {
            OffsetTime ot = (OffsetTime) argValue;
            super.doSetValue(ps, parameterPosition,
                    new SqlParameterValue(Types.TIME, new Time(DateTimeUtils.toInstant(ot).toEpochMilli())));
            return;
        }
        if (argValue instanceof ZonedDateTime) {
            ZonedDateTime zdt = (ZonedDateTime) argValue;
            super.doSetValue(ps, parameterPosition,
                    new SqlParameterValue(Types.TIMESTAMP, Timestamp.from(zdt.toInstant())));
            return;
        }
        super.doSetValue(ps, parameterPosition, argValue);
    }

}
