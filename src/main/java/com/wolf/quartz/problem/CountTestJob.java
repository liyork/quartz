package com.wolf.quartz.problem;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * Description:
 * <br/> Created on 03/08/2018 9:48 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class CountTestJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CountTestJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
        logger.info("CountTestJob  execute,time:{},jobkey:{}",
                simpleDateFormat.format(System.currentTimeMillis()),context.getJobDetail().getKey());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
