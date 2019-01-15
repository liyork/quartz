package com.wolf.quartz.advanced;

import com.wolf.quartz.SimpleQuartzExample;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Description:
 * <p>
 * <br/> Created on 04/08/2018 6:08 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ThreadUseTest {

    private static final Logger logger = LoggerFactory.getLogger(ThreadUseTest.class);

    public static void main(String[] args) throws SchedulerException, InterruptedException {

        testThreadUse();
    }

    //一个scheduler用一个线程池，每个线程都可能服务于任何jobdetail
    private static void testThreadUse() throws SchedulerException, InterruptedException {
        StdSchedulerFactory factory = new StdSchedulerFactory();

        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", "10");

        factory.initialize(properties);

        Scheduler scheduler = factory.getScheduler();


        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2).repeatForever();


        for (int i = 0; i < 2; i++) {
            JobDetail jobDetail = SimpleQuartzExample.
                    createJobDetail(ThreadTestJob.class, i+"_ThreadTestJobJob_Job", i+"_ThreadTestJobJob_Job_Group");

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(i+"_ThreadTestJob_Trigger", i+"_ThreadTestJob_Trigger_Group")
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        }

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(500000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }

}
