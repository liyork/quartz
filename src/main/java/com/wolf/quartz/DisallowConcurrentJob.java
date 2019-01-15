package com.wolf.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:同一job禁止并行
 * <br/> Created on 03/08/2018 9:48 AM
 *
 * @author 李超
 * @since 1.0.0
 */
@DisallowConcurrentExecution
public class DisallowConcurrentJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(DisallowConcurrentJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            logger.info(" scheduler name:{}，thread name:{},time:{},jobkey:{}", context.getScheduler().getSchedulerName(),
                   Thread.currentThread().getName(),System.currentTimeMillis(),context.getJobDetail().getKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
