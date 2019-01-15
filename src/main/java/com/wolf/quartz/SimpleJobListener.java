package com.wolf.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * <br/> Created on 03/08/2018 11:06 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SimpleJobListener implements JobListener {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJobListener.class);


    @Override
    public String getName() {
        String simpleName = getClass().getSimpleName();
        logger.info("listener simplename is :{}", simpleName);
        return simpleName;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("jobkey:{} is going to be executed",jobKey);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("jobkey:{} was vetoed and not executed",jobKey);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("jobkey:{} was executed",jobKey);
    }
}
