package com.wolf.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:有状态job
 * <br/> Created on 03/08/2018 9:48 AM
 *
 * @author 李超
 * @since 1.0.0
 */
//不带注解则一样的hashcode。但是值不保留..
//带注解则不一样的hashcode，值保留..
@PersistJobDataAfterExecution
public class PersistJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PersistJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("this:"+this);
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        System.out.println("jobDataMap hashCode:"+jobDataMap.hashCode());
        int count = jobDataMap.getInt("count");
        logger.info("count:{}", count);

        jobDataMap.put("count", count + 1);

    }
}
