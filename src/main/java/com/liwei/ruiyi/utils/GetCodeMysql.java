package com.liwei.ruiyi.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class GetCodeMysql {
    private final String tableSpace = "ruiyi";
    private final String tableName = "t_profit_day";
    private static final String PACKAGE_PATH = "com.liwei.ruiyi";
//    private final String url = "jdbc:mysql://81.70.86.120:8866/ruiyi?characterEncoding=utf8&serverTimezone=UTC&rewriteBatchedStatements=true";
    private final String url = "jdbc:mysql://localhost:3306/ruiyi?characterEncoding=utf8&serverTimezone=UTC&rewriteBatchedStatements=true";
    private final String driverName = "com.mysql.cj.jdbc.Driver";
    private final String userName = "root";
    private final String password = "Liv88625200@@";

    public static void main(String[] args) throws Exception {
        new GetCodeMysql().generate();
    }

    public void generate() throws Exception {
        if (!confirmContinue()) return;

        Class.forName(driverName);
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             Statement stmt = conn.createStatement()) {

            List<Map<String, String>> columns = getTableColumns(stmt);
            generateEntity(columns);
            generateRowMapper(columns);
//            String tableName = "t_profit_day";
//            generateDaoCode(tableName);
//            generateServiceCode(tableName);
        }
    }

    private boolean confirmContinue() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("当前表：" + tableName + "，是否继续？(Y/N)");
        String confirm = scanner.nextLine();
        return "Y".equalsIgnoreCase(confirm);
    }

    private List<Map<String, String>> getTableColumns(Statement stmt) throws Exception {
        String sql = "SELECT column_name, data_type FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA='" + tableSpace + "' AND TABLE_NAME='" + tableName + "' ORDER BY ORDINAL_POSITION";
        ResultSet rs = stmt.executeQuery(sql);

        List<Map<String, String>> columns = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> column = new HashMap<>();
            column.put("name", rs.getString("column_name"));
            column.put("type", rs.getString("data_type"));
            columns.add(column);
        }
        return columns;
    }

    private void generateEntity(List<Map<String, String>> columns) {
        String fileName = toClassName(tableName);
        String filePath = getFilePath("bo", fileName + ".java");

        String content = """
                package %s.bo;
                
                import lombok.*;
                import java.math.BigDecimal;
                import java.util.*;
                
                @Data
                public class %s {
                """.formatted(PACKAGE_PATH, fileName);

        StringBuilder sb = new StringBuilder();
        sb.append(content);

        for (Map<String, String> column : columns) {
            String fieldType = mapSqlTypeToJavaType(column.get("type"));
            String comment = """
                        private %s %s;
                    """.formatted(fieldType, toCamelCase(column.get("name")));
            sb.append(comment);
        }
        sb.append("}");

        writeFile(filePath, sb.toString());
    }

    private void generateRowMapper(List<Map<String, String>> columns) {
        String className = toClassName(tableName);
        String filePath = getFilePath("bo.mapper", className + "Mapper.java");

        String mapperContent = """
                package %s.bo.mapper;
                
                import %s.bo.%s;
                import org.springframework.jdbc.core.RowMapper;
                import java.sql.*;
                
                public class %sMapper implements RowMapper<%s> {
                    @Override
                    public %s mapRow(ResultSet rs, int rowNum) throws SQLException {
                        %s obj = new %s();
                """.formatted(PACKAGE_PATH, PACKAGE_PATH, className, className, className, className, className, className);

        StringBuilder sb = new StringBuilder(mapperContent);

        for (Map<String, String> column : columns) {
            String fieldName = toCamelCase(column.get("name"));
            String fieldType = mapSqlTypeToJavaType(column.get("type"));
            String setMethod = """
                            obj.set%s(rs.get%s("%s"));
                    """.formatted(capitalize(fieldName), getResultSetMethod(fieldType), column.get("name"));
            sb.append(setMethod);
        }

        String endContent = """
                    return obj;
                }
            }
            """;
        sb.append(endContent);
        writeFile(filePath, sb.toString());
    }

    private void generateDaoCode(String tableName) {
        String className = toClassName(tableName);
        String daoFilePath = getFilePath("dao", className + "Dao.java");
        String daoImplFilePath = getFilePath("dao.impl", className + "DaoImpl.java");

        String daoContent = """
                package %s.dao;
                
                import org.springframework.stereotype.Service;
                
                @Service
                public interface %sDao {
                }
                """.formatted(PACKAGE_PATH, className);

        String daoImplContent = """
                package %s.dao.impl;
                
                import %s.dao.%sDao;
                import org.springframework.stereotype.Repository;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.jdbc.core.JdbcTemplate;
                
                @Repository("%sDao")
                public class %sDaoImpl implements %sDao {
                    @Autowired
                    private JdbcTemplate jdbc;
                
                    public JdbcTemplate getJdbc() {
                        return jdbc;
                    }
                }
                """.formatted(PACKAGE_PATH, PACKAGE_PATH, className, className.substring(1).toLowerCase(),
                className, className);

        writeFile(daoFilePath, daoContent);
        writeFile(daoImplFilePath, daoImplContent);
    }

    private void generateServiceCode(String tableName) {
        String className = toClassName(tableName);
        String serviceFilePath = getFilePath("service", className.substring(1) + "Service.java");
        String serviceImplFilePath = getFilePath("service.impl", className.substring(1) + "ServiceImpl.java");

        String serviceContent = """
                package %s.service;
                
                import org.springframework.stereotype.Service;
                
                @Service
                public interface %sService {
                }
                """.formatted(PACKAGE_PATH, className.substring(1));

        String serviceImplContent = """
                package %s.service.impl;
                
                import %s.service.%sService;
                import %s.dao.%sDao;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.stereotype.Repository;
                import java.util.List;
                
                @Repository("%sService")
                public class %sServiceImpl implements %sService {
                    @Autowired
                    private %sDao %sDao;
                }
                """.formatted(PACKAGE_PATH, PACKAGE_PATH, className.substring(1), PACKAGE_PATH, className,
                className.substring(1).toLowerCase(), className.substring(1), className.substring(1),
                className, className.substring(1).toLowerCase());

        writeFile(serviceFilePath, serviceContent);
        writeFile(serviceImplFilePath, serviceImplContent);
    }

    private String getFilePath(String subPackage, String fileName) {
        return System.getProperty("user.dir") + "\\src\\main\\java\\" + PACKAGE_PATH.replace(".", "\\") + "\\" + subPackage.replace(".", "\\") + "\\" + fileName;
    }

    private void writeFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            FileUtils.forceMkdirParent(file);
            FileUtils.writeStringToFile(file, content, "UTF-8");
            System.out.println("生成成功：" + filePath);
        } catch (IOException e) {
            throw new RuntimeException("文件生成失败：" + filePath, e);
        }
    }

    private String mapSqlTypeToJavaType(String sqlType) {
        return switch (sqlType) {
            case "varchar", "text", "char", "enum", "json", "date", "datetime", "timestamp","mediumtext" -> "String";
            case "int", "integer" -> "Integer";
            case "bigint" -> "Long";
            case "tinyint" -> "Boolean";
            case "decimal", "numeric" -> "BigDecimal";
            case "double", "float" -> "Double";
            default -> "Object";
        };
    }

    private String getResultSetMethod(String fieldType) {
        return switch (fieldType) {
            case "String" -> "String";
            case "Integer" -> "Int";
            case "Long" -> "Long";
            case "Boolean" -> "Boolean";
            case "BigDecimal" -> "BigDecimal";
            case "Double" -> "Double";
            case "Date" -> "Timestamp";
            default -> "Object";
        };
    }

    private String toCamelCase(String input) {
        return Arrays.stream(input.split("_")).reduce((a, b) -> a + capitalize(b)).orElse(input);
    }

    private String toClassName(String input) {
        return Arrays.stream(input.split("_")).map(this::capitalize).reduce("", String::concat);
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
