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

package org.opengauss.datachecker.check.load;

import lombok.extern.slf4j.Slf4j;
import org.opengauss.datachecker.check.client.FeignClientService;
import org.opengauss.datachecker.common.entry.enums.Endpoint;
import org.opengauss.datachecker.common.entry.extract.ExtractConfig;
import org.opengauss.datachecker.common.util.ThreadUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * CheckDatabaseLoader
 *
 * @author ：wangchao
 * @date ：Created in 2022/10/31
 * @since ：11
 */
@Slf4j
@Order(97)
@Service
public class CheckDatabaseLoader extends AbstractCheckLoader {
    @Resource
    private FeignClientService feignClient;

    /**
     * Initialize the verification result environment
     */
    @Override
    public void load(CheckEnvironment checkEnvironment) {
        int retry = 1;
        ExtractConfig sourceConfig = feignClient.getEndpointConfig(Endpoint.SOURCE);
        ExtractConfig sinkConfig = feignClient.getEndpointConfig(Endpoint.SINK);
        while (retry <= maxRetryTimes && (sourceConfig == null || sinkConfig == null)) {
            sourceConfig = feignClient.getEndpointConfig(Endpoint.SOURCE);
            sinkConfig = feignClient.getEndpointConfig(Endpoint.SINK);
            log.error("load database configuration ,retry={}", retry);
            ThreadUtil.sleepOneSecond();
            retry++;
        }
        if (sourceConfig == null) {
            shutdown("source endpoint server has error");
        }
        if (sinkConfig == null) {
            shutdown("sink endpoint server has error");
        }
        checkEnvironment.addExtractDatabase(Endpoint.SOURCE, sourceConfig.getDatabase());
        checkEnvironment.addExtractDatabase(Endpoint.SINK, sinkConfig.getDatabase());
        log.info("check service load database configuration success.");
    }

}
