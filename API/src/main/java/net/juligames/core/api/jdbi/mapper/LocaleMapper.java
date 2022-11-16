package net.juligames.core.api.jdbi.mapper;

import net.juligames.core.api.jdbi.Locale;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class LocaleMapper implements RowMapper<Locale> {
    @Override
    public Locale map(ResultSet rs, StatementContext ctx) throws SQLException {
        return null;
    }
}
