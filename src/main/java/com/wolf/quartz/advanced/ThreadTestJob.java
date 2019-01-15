package com.wolf.quartz.advanced;

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
public class ThreadTestJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ThreadTestJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
        logger.info("ThreadTestJob  execute,time:{},threadName:{},jobKey:{}",
                simpleDateFormat.format(System.currentTimeMillis()),
                Thread.currentThread().getName(),context.getJobDetail().getKey());

    }
}
