/*
 * Copyright (c) 2022-2022 Huawei Technologies Co.,Ltd.
 *
 * openGauss is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *
 *           http://license.coscl.org.cn/MulanPSL2
 *
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.opengauss.datachecker.extract.dml;

import org.opengauss.datachecker.common.entry.enums.DataBaseType;
import org.opengauss.datachecker.common.entry.extract.ColumnsMetaData;
import org.opengauss.datachecker.common.util.HexUtil;
import org.opengauss.datachecker.common.util.SqlUtil;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DmlBuilder
 *
 * @author ：wangchao
 * @date ：Created in 2022/6/13
 * @since ：11
 */
public class DmlBuilder {
    /**
     * primaryKeys
     */
    public static final String PRIMARY_KEYS = "primaryKeys";
    /**
     * sql delimiter
     */
    protected static final String DELIMITER = ",";
    /**
     * left bracket
     */
    protected static final String LEFT_BRACKET = "(";
    /**
     * right bracket
     */
    protected static final String RIGHT_BRACKET = ")";
    /**
     * SQL statement conditional query in statement fragment
     */
    protected static final String IN = " in ( :primaryKeys )";
    /**
     * single quotes
     */
    protected static final String SINGLE_QUOTES = "'";
    /**
     * equal
     */
    protected static final String EQUAL = " = ";
    /**
     * and
     */
    protected static final String AND = " and ";
    /**
     * mysql dataType
     */
    protected static final List<String> DIGITAL =
        List.of("int", "integer", "tinyint", "smallint", "mediumint", "bit", "bigint", "double", "float", "decimal",
            "year");
    protected static final List<String> BLOB_LIST = List.of("blob", "tinyblob", "mediumblob", "longblob");
    protected static final List<String> BINARY = List.of("binary", "varbinary");
    /**
     * columns
     */
    protected String columns;

    /**
     * columnsValue
     */
    protected String columnsValue;
    /**
     * schema
     */
    protected String schema;
    /**
     * tableName
     */
    protected String tableName;
    /**
     * condition
     */
    protected String condition;
    /**
     * conditionValue
     */
    protected String conditionValue;
    /**
     * dataBaseType
     */
    protected DataBaseType dataBaseType;

    public DmlBuilder() {
    }

    public DmlBuilder(DataBaseType databaseType) {
        this.dataBaseType = databaseType;
    }

    /**
     * Build SQL column statement fragment
     *
     * @param columnsMetas Field Metadata
     */
    protected void buildColumns(@NotNull List<ColumnsMetaData> columnsMetas) {
        columns = columnsMetas.stream().map(ColumnsMetaData::getColumnName).map(column -> escape(column, dataBaseType))
                              .collect(Collectors.joining(DELIMITER));
    }

    private String escape(String content, DataBaseType dataBase_type) {
        return SqlUtil.escape(content, dataBase_type);
    }

    protected void buildDataBaseType(@NotNull DataBaseType dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    /**
     * DML Builder: setting schema parameters
     *
     * @param schema schema
     */
    protected void buildSchema(@NotNull String schema) {
        this.schema = escape(schema, dataBaseType);
    }

    /**
     * DML Builder: setting tableName parameters
     *
     * @param tableName tableName
     */
    protected void buildTableName(@NotNull String tableName) {
        this.tableName = escape(tableName, dataBaseType);
    }

    /**
     * DML Builder: setting primaryMetas parameters
     *
     * @param primaryMetas primaryMetas
     * @return sql value fragment
     */
    protected String buildConditionCompositePrimary(List<ColumnsMetaData> primaryMetas) {
        return primaryMetas.stream().map(ColumnsMetaData::getColumnName).map(name -> escape(name, dataBaseType))
                           .collect(Collectors.joining(DELIMITER, LEFT_BRACKET, RIGHT_BRACKET));
    }

    /**
     * columnsValueList
     *
     * @param columnsValue    columnsValue
     * @param columnsMetaList columnsMetaList
     * @return columnsValueList
     */
    public List<String> columnsValueList(@NotNull Map<String, String> columnsValue,
        @NotNull List<ColumnsMetaData> columnsMetaList) {
        List<String> valueList = new ArrayList<>();
        columnsMetaList.forEach(columnMeta -> {
            final String columnName = columnMeta.getColumnName();
            if (DIGITAL.contains(columnMeta.getDataType())) {
                valueList.add(columnsValue.get(columnName));
            } else if (BLOB_LIST.contains(columnMeta.getDataType())) {
                valueList.add(SINGLE_QUOTES + columnsValue.get(columnName) + SINGLE_QUOTES);
            } else if (BINARY.contains(columnMeta.getDataType())) {
                valueList.add(SINGLE_QUOTES + HexUtil.HEX_PREFIX + columnsValue.get(columnName) + SINGLE_QUOTES);
            } else {
                String value = columnsValue.get(columnName);
                if (Objects.isNull(value)) {
                    valueList.add("null");
                } else {
                    valueList.add(SINGLE_QUOTES.concat(value).concat(SINGLE_QUOTES));
                }
            }
        });
        return valueList;
    }

    interface Fragment {
        /**
         * DML SQL statement insert fragment
         */
        String DML_INSERT = "insert into #schema.#tablename (#columns) values (#value);";
        /**
         * DML SQL statement replace fragment
         */
        String DML_REPLACE = "replace into #schema.#tablename (#columns) values (#value);";
        String DML_UPDATE = "update #schema.#tablename set #columns  where #condition;";
        /**
         * DML SQL statement select fragment
         */
        String SELECT = "select ";
        /**
         * DML SQL statement delete fragment
         */
        String DELETE = "delete ";
        /**
         * DML SQL statement from fragment
         */
        String FROM = " from ";
        /**
         * DML SQL statement where fragment
         */
        String WHERE = " where ";
        /**
         * DML SQL statement space fragment
         */
        String SPACE = " ";
        String SINGLE_QUOTES = "'";
        String AND = " and ";
        String EQUAL = "=";
        /**
         * DML SQL statement END fragment
         */
        String END = ";";
        String COMMA = " , ";
        /**
         * DML SQL statement linker fragment
         */
        String LINKER = ".";
        /**
         * DML SQL statement schema fragment
         */
        String SCHEMA = "#schema";
        /**
         * DML SQL statement tablename fragment
         */
        String TABLE_NAME = "#tablename";
        /**
         * DML SQL statement columns fragment
         */
        String COLUMNS = "#columns";
        /**
         * DML SQL statement value fragment
         */
        String VALUE = "#value";
        /**
         * DML SQL statement condition fragment
         */
        String CONDITION = "#condition";

    }
}
