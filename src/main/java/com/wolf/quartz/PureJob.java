package com.wolf.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * <br/> Created on 03/08/2018 9:48 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class PureJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PureJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("pure job,triggerkey:{}",context.getTrigger().getKey());
    }
}
