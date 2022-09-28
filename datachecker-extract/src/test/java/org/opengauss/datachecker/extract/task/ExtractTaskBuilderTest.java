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

package org.opengauss.datachecker.extract.task;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.opengauss.datachecker.common.entry.extract.ExtractTask;
import org.opengauss.datachecker.extract.cache.MetaDataCache;
import org.opengauss.datachecker.extract.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * ExtractTaskBuilderTest
 *
 * @author ：wangchao
 * @date ：Created in 2022/5/14
 * @since ：11
 */
@Slf4j
@SpringBootTest
public class ExtractTaskBuilderTest {
    @Autowired
    private ExtractTaskBuilder extractTaskBuilder;
    @Autowired
    private MetaDataService metadataService;

    /**
     * init
     */
    @PostConstruct
    public void init() {
        MetaDataCache.initCache();
        MetaDataCache.putMap(metadataService.queryMetaDataOfSchema());
    }

    /**
     * builderTest
     */
    @Test
    public void builderTest() {
        Set<String> tables = MetaDataCache.getAllKeys();
        List<ExtractTask> extractTasks = extractTaskBuilder.builder(tables);
        for (ExtractTask task : extractTasks) {
            log.info("" + task);
        }
    }
}