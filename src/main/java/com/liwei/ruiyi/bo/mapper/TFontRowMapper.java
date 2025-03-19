package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TFont;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TFontRowMapper implements RowMapper<TFont> {
    @Override
    public TFont mapRow(ResultSet rs, int rowNum) throws SQLException {
        TFont t_font = new TFont();
        t_font.setId(rs.getInt("id"));
        t_font.setFont_name(rs.getString("font_name"));
        return t_font;
    }
}