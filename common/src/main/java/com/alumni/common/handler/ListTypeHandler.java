package com.alumni.common.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONWriter;
import com.alumni.common.core.domain.model.FileDTO;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用 List<Object> TypeHandler （Fastjson2版本）
 * @author lifeng
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
@Component
public class ListTypeHandler extends BaseTypeHandler<List<Object>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<Object> param, JdbcType jdbcType) throws SQLException {
        String json = JSON.toJSONString(param,
                JSONWriter.Feature.WriteNullListAsEmpty,
                JSONWriter.Feature.WriteMapNullValue,
                JSONWriter.Feature.WriteNullStringAsEmpty);
        ps.setString(i, json);
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName), columnName);
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex), null);
    }

    @Override
    public List<Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex), null);
    }

    private List<Object> convert(String json, String columnName) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }

        Object parsedData = parseByColumn(json, columnName);
        if (parsedData instanceof List) {
            // 转为 List<Object>
            return new ArrayList<>((List<?>) parsedData);
        }
        return new ArrayList<>();
    }

    /**
     * 根据列名映射不同类型
     */
    private Object parseByColumn(String json, String column) {
        if (column == null) {
            return JSONArray.parseArray(json, Object.class);
        }

        switch (column) {
            case "dept_ids":
                return JSONArray.parseArray(json, Long.class);
            case "file_url":
            case "dept_cover":
            case "carousel":
            case "logo":
            case "avatar":
                return JSONArray.parseArray(json, FileDTO.class);
            default:
                // 默认使用 Object，避免强类型导致失败
                return JSONArray.parseArray(json, Object.class);
        }
    }
}
