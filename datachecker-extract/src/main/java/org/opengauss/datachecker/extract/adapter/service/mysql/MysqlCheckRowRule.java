package org.opengauss.datachecker.extract.adapter.service.mysql;

import lombok.extern.slf4j.Slf4j;
import org.opengauss.datachecker.extract.adapter.service.CheckRowRuleService;
import org.springframework.stereotype.Service;

/**
 * MysqlCheckRowRule
 *
 * @author ：wangchao
 * @date ：Created in 2022/12/2
 * @since ：11
 */
@Slf4j
@Service
public class MysqlCheckRowRule extends CheckRowRuleService {

    @Override
    protected String convert(String text) {
        return "`" + text + "`";
    }

    @Override
    protected String convertCondition(String text) {
        return text;
    }
}
