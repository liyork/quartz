package com.wolf.quartz;

import org.quartz.*;
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
public class HelloWorldJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info(this+",Hello World,time:" + System.currentTimeMillis());//每次执行执行的都是新构造的job，不会有线程问题

        JobDetail jobDetail = context.getJobDetail();
        logger.info("group and name:{}", jobDetail.getKey());
        logger.info("job class:{}", jobDetail.getJobClass());

        Scheduler scheduler = context.getScheduler();
        try {
            logger.info("scheduler name:{}", scheduler.getSchedulerName());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
        logger.info("job fired at :{}", simpleDateFormat.format(context.getFireTime()));

        logger.info("job next fire time:{}", simpleDateFormat.format(context.getNextFireTime()));

        logger.info("get message from context:{}",context.getJobDetail().getJobDataMap().get("message"));

    }
}
