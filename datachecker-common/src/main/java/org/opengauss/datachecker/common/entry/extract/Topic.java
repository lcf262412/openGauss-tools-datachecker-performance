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

package org.opengauss.datachecker.common.entry.extract;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Topic
 *
 * @author ：wangchao
 * @date ：Created in 2022/6/14
 * @since ：11
 */
@ToString
@Data
@Accessors(chain = true)
public class Topic {
    /**
     * tableName
     */
    private String tableName;
    /**
     * Current table, corresponding topic name
     */
    private String topicName;
    /**
     * The total number of partitions of data in the current table in Kafka topic
     */
    private int partitions;

}
