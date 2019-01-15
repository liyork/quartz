package com.wolf.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.OrMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Description:
 * <br/> Created on 03/08/2018 9:53 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SimpleQuartzExample {

    private static final Logger logger = LoggerFactory.getLogger(SimpleQuartzExample.class);

    public static void main(String[] args) throws Exception {

//        Class<HelloWorldJob> jobClass = HelloWorldJob.class;
        Class<PersistJob> jobClass = PersistJob.class;
        testBaseUsage(jobClass);

//        testTwoSchedulerConcurrent();
//        testOneSchedulerConcurrent();

//        testListener();

//        testTriggerListener();

//        testSchedulerListener();
    }


    private static void testBaseUsage(Class<? extends Job> jobClass) throws SchedulerException, InterruptedException {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        Trigger trigger = createTrigger("HelloWorld_Trigger", "HelloWorld_Trigger_Group");

        JobDetail jobDetail = createJobDetail(jobClass,"HelloWorldJob_Job", "HelloWorldJob_Job_Group");

        scheduler.scheduleJob(jobDetail, trigger);

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(5000);

        logger.info("standby scheduler");
        scheduler.standby();

        Thread.sleep(5000);

        logger.info("restart scheduler");
        scheduler.start();

        Thread.sleep(5000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }

    private static void startSameJobDetailSchedule(Scheduler scheduler, JobDetail jobDetail) throws SchedulerException, InterruptedException {

        Trigger trigger = createTrigger("HelloWorld_Trigger", "HelloWorld_Trigger_Group");

        scheduler.scheduleJob(jobDetail, trigger);

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(70000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }

    //两个scheduler不影响。
    private static void testTwoSchedulerConcurrent() throws SchedulerException {

        StdSchedulerFactory factory = new StdSchedulerFactory();
        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceName", "shceduler1");
        props.put("org.quartz.threadPool.threadCount", "10");
        factory.initialize(props);
        Scheduler scheduler = factory.getScheduler();

        JobDetail jobDetail = createJobDetail(DisallowConcurrentJob.class,"HelloWorldJob_Job", "HelloWorldJob_Job_Group");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startSameJobDetailSchedule(scheduler, jobDetail);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        //一个scheduler执行方法完后才能执行下一个调度，似乎两个调度不影响

        StdSchedulerFactory factory2 = new StdSchedulerFactory();
        Properties props2 = new Properties();
        props2.put("org.quartz.scheduler.instanceName", "shceduler2");
        props2.put("org.quartz.threadPool.threadCount", "10");
        factory2.initialize(props2);
        Scheduler scheduler2 = factory2.getScheduler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startSameJobDetailSchedule(scheduler2,jobDetail);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //报错org.quartz.ObjectAlreadyExistsException: Unable to store Job : 'HelloWorldJob_Job_Group.HelloWorldJob_Job',
    // because one already exists with this identification.
    //同一个scheduler只能存放一个jobdetail，唯一的！
    //综上，这里指的并发，其实就是一个scheduler对一个jobdetail进行的，由于job卡或者其他原因导致的上一个未结束，下一个又来了。
    private static void testOneSchedulerConcurrent() throws SchedulerException {

        StdSchedulerFactory factory = new StdSchedulerFactory();
        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceName", "shceduler1");
        props.put("org.quartz.threadPool.threadCount", "10");
        factory.initialize(props);
        Scheduler scheduler = factory.getScheduler();

        JobDetail jobDetail = createJobDetail(DisallowConcurrentJob.class,"HelloWorldJob_Job", "HelloWorldJob_Job_Group");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startSameJobDetailSchedule(scheduler, jobDetail);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startSameJobDetailSchedule(scheduler,jobDetail);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private static void testListener() throws SchedulerException, InterruptedException {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail1 = createJobDetail(PureJob.class,"HelloWorld1_job", "HelloWorld1_job_Group");
        Trigger trigger1 = createTrigger("HelloWorld1_Trigger", "HelloWorld1_Trigger_Group");

        JobDetail jobDetail2 = createJobDetail(PureJob.class,"HelloWorld2_job", "HelloWorld2_job_Group");
        Trigger trigger2 = createTrigger("HelloWorld2_Trigger", "HelloWorld2_Trigger_Group");

        scheduler.scheduleJob(jobDetail1, trigger1);
        scheduler.scheduleJob(jobDetail2, trigger2);

        //注册所有
        scheduler.getListenerManager().addJobListener(new SimpleJobListener(), EverythingMatcher.allJobs());
        //注册一个key
//        scheduler.getListenerManager().addJobListener(new SimpleJobListener(),
//                KeyMatcher.keyEquals(JobKey.jobKey("HelloWorld1_job","HelloWorld1_job_Group")));
        //注册同一组
//        scheduler.getListenerManager().addJobListener(new SimpleJobListener(), GroupMatcher.jobGroupEquals("HelloWorld2_Group"));
        //注册两个组
        scheduler.getListenerManager().addJobListener(new SimpleJobListener(), OrMatcher.or(GroupMatcher.jobGroupEquals("HelloWorld1_Group"), GroupMatcher.jobGroupEquals("HelloWorld2_Group")));

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(70000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();

    }

    public static SimpleScheduleBuilder createSchedulerBuilder() {
        return SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2).repeatForever();
    }

    public static JobDetail createJobDetail(Class<? extends Job> jobClass,String jobName,String jobGroup) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName,jobGroup)
                .usingJobData("message", "welcome to study quartz")
                .usingJobData("count", "1")
                .build();
    }


    public static Trigger createTrigger(String triggerName,String triggerGropu) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerName,triggerGropu)
                .withSchedule(createSchedulerBuilder())
                .build();
    }

    private static void testTriggerListener() throws SchedulerException, InterruptedException {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail1 = createJobDetail(PureJob.class,"HelloWorld1_job", "HelloWorld1_job_Group");
        Trigger trigger1 = createTrigger("HelloWorld1_Trigger", "HelloWorld1_Trigger_Group");

        JobDetail jobDetail2 = createJobDetail(PureJob.class,"HelloWorld2_job", "HelloWorld2_job_Group");
        Trigger trigger2 = createTrigger("HelloWorld2_Trigger", "HelloWorld2_Trigger_Group");

        scheduler.scheduleJob(jobDetail1, trigger1);
        scheduler.scheduleJob(jobDetail2, trigger2);

        //注册所有
//        scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener("simpleTrigger"), EverythingMatcher.allTriggers());

        //指定key
//        scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener("simpleTrigger"),
//                KeyMatcher.keyEquals(TriggerKey.triggerKey("HelloWorld1_Trigger","HelloWorld1_Trigger_Group")));

        //指定组
        scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener("simpleTrigger11"), GroupMatcher.groupEquals("HelloWorld2_Trigger_Group"));

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(70000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }

    private static void testSchedulerListener() throws SchedulerException, InterruptedException {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail1 = createJobDetail(PureJob.class,"HelloWorld1_job", "HelloWorld1_job_Group");
        Trigger trigger1 = createTrigger("HelloWorld1_Trigger", "HelloWorld1_Trigger_Group");

        scheduler.scheduleJob(jobDetail1, trigger1);

        scheduler.getListenerManager().addSchedulerListener(new SimpleSchedulerListener());

//        scheduler.getListenerManager().removeSchedulerListener(new SimpleSchedulerListener());

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(3000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }
}
