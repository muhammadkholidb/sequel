package com.gitlab.muhammadkholidb.sequel.jdbc;

import java.beans.PropertyDescriptor;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

import com.gitlab.muhammadkholidb.toolbox.data.TimeUtils;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class CustomBeanRowMapper<T> extends BeanPropertyRowMapper<T> {

    public CustomBeanRowMapper(Class<T> modelClass) {
        super(modelClass);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return getColumnValue(rs, index, pd.getPropertyType());
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, Class<?> type) throws SQLException {
        if (Instant.class == type) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp == null ? null : timestamp.toInstant();
        }
        if (ZonedDateTime.class == type) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp == null ? null : timestamp.toInstant().atZone(TimeUtils.parseZoneId(timestamp));
        }
        if (OffsetDateTime.class == type) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp == null ? null : timestamp.toInstant().atOffset(TimeUtils.parseZoneOffset(timestamp));
        }
        if (OffsetTime.class == type) {
            Time time = rs.getTime(index);
            return time == null ? null : time.toLocalTime().atOffset(TimeUtils.parseZoneOffset(time));
        }
        if (LocalDate.class == type) {
            Date date = rs.getDate(index);
            return date == null ? null : date.toLocalDate();
        }
        if (LocalDateTime.class == type) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp == null ? null : timestamp.toLocalDateTime();
        }
        if (LocalTime.class == type) {
            Time time = rs.getTime(index);
            return time == null ? null : time.toLocalTime();
        }
        return super.getColumnValue(rs, index, type);
    }

}
