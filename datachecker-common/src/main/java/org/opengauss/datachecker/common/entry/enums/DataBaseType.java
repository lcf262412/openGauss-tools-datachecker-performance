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

package org.opengauss.datachecker.common.entry.enums;

import lombok.Getter;

/**
 * DataBaseType {@value API_DESCRIPTION }
 *
 * @author ：wangchao
 * @date ：Created in 2022/5/29
 * @since ：11
 */
@Getter
public enum DataBaseType implements IEnum {
    /**
     * MySQL database type
     */
    MS("MYSQL", "MYSQL"),
    /**
     * open gauss database type
     */
    OG("OPENGAUSS", "OPENGAUSS"),
    /**
     * oracle database type
     */
    O("ORACLE", "ORACLE");

    private final String code;
    private final String description;

    DataBaseType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * DataBaseType api description
     */
    public static final String API_DESCRIPTION = "Database type [MS-MYSQL,OG-OPENGAUSS,O-ORACLE]";
}