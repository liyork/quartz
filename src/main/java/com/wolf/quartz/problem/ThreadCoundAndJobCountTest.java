package com.wolf.quartz.problem;

import com.wolf.quartz.SimpleQuartzExample;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Description:测试任务慢时，线程数和job数之间的关系。
 *
 * 总结：
 * a、b、c任务，1个线程池2个线程x线程，y线程
 * 显然线程充分利用，但是a若慢则可能拖慢b和c的执行，不过这里有2线程，可以防止这个。
 *
 * 3任务，3线程呢？
 * 有竞争，也合理，也可以备份。
 *
 * 3任务，4线程呢？
 * 可能有剩余。
 *
 * 关键还是在任务快慢上。最好是分job分线程。慢的用一个线程池，快的用一个。隔离开。但一个schedule就挂多个trigger和jobdetail，
 * 那就应该启动多个scheduler了。
 *
 * <br/> Created on 04/08/2018 6:08 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ThreadCoundAndJobCountTest {

    private static final Logger logger = LoggerFactory.getLogger(ThreadCoundAndJobCountTest.class);

    public static void main(String[] args) throws SchedulerException, InterruptedException {

//        oneThreadJobFastTest();

//        oneThreadJobSlowTest();

//        oneThreadJobSlowTest1();

        oneThreadJobSlowTest2();
    }

    //1线程多任务任务也没事，只要任务很快执行完。
    private static void oneThreadJobFastTest() throws SchedulerException, InterruptedException {
        StdSchedulerFactory factory = new StdSchedulerFactory();

        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", "1");

        factory.initialize(properties);

        Scheduler scheduler = factory.getScheduler();


        for (int i = 0; i < 10; i++) {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(2).repeatForever();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(i + "CountTestJob_Trigger", i + "CountTestJob_Trigger_Group")
                    .withSchedule(scheduleBuilder)
                    .build();

            JobDetail jobDetail = SimpleQuartzExample.createJobDetail(CountTestJob.class, i + "CountTestJobJob_Job", i + "CountTestJobJob_Job_Group");

            scheduler.scheduleJob(jobDetail, trigger);
        }

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(500000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }


    //修改CountTestJob，添加Thread.sleep(5);
    //1个线程下，任务只能按照内部5s的频率执行，而且集中在0任务上。。其他任务都没有执行机会。内部似乎是有队列
    //任务改成2s，也几乎是0、1任务被执行。。。
    //默认是MISFIRE_INSTRUCTION_SMART_POLICY，对于错过时间的job执行完上个job后马上执行本job。。所以0会一直执行。。。
    private static void oneThreadJobSlowTest() throws SchedulerException, InterruptedException {
        StdSchedulerFactory factory = new StdSchedulerFactory();

        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", "1");

        factory.initialize(properties);

        Scheduler scheduler = factory.getScheduler();


        for (int i = 0; i < 10; i++) {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ?");
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(i + "CountTestJob_Trigger", i + "CountTestJob_Trigger_Group")
                    .withSchedule(scheduleBuilder)
                    .build();

            JobDetail jobDetail = SimpleQuartzExample.createJobDetail(CountTestJob.class, i + "CountTestJobJob_Job", i + "CountTestJobJob_Job_Group");

            scheduler.scheduleJob(jobDetail, trigger);
        }

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(500000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }


    //withMisfireHandlingInstructionIgnoreMisfires 则忽略失败的job，那么就是依次执行了。。
    private static void oneThreadJobSlowTest1() throws SchedulerException, InterruptedException {
        StdSchedulerFactory factory = new StdSchedulerFactory();

        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", "1");

        factory.initialize(properties);

        Scheduler scheduler = factory.getScheduler();


        for (int i = 0; i < 10; i++) {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ?")
                    .withMisfireHandlingInstructionIgnoreMisfires();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(i + "CountTestJob_Trigger", i + "CountTestJob_Trigger_Group")
                    .withSchedule(scheduleBuilder)
                    .build();

            JobDetail jobDetail = SimpleQuartzExample.createJobDetail(CountTestJob.class, i + "CountTestJobJob_Job", i + "CountTestJobJob_Job_Group");

            scheduler.scheduleJob(jobDetail, trigger);
        }

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(500000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }


    //withMisfireHandlingInstructionDoNothing，什么也不做，但是还是执行。。所以还是都在0上。
    private static void oneThreadJobSlowTest2() throws SchedulerException, InterruptedException {
        StdSchedulerFactory factory = new StdSchedulerFactory();

        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", "1");

        factory.initialize(properties);

        Scheduler scheduler = factory.getScheduler();


        for (int i = 0; i < 10; i++) {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ?")
                    .withMisfireHandlingInstructionDoNothing();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(i + "CountTestJob_Trigger", i + "CountTestJob_Trigger_Group")
                    .withSchedule(scheduleBuilder)
                    .build();

            JobDetail jobDetail = SimpleQuartzExample.createJobDetail(CountTestJob.class, i + "CountTestJobJob_Job", i + "CountTestJobJob_Job_Group");

            scheduler.scheduleJob(jobDetail, trigger);
        }

        logger.info("executor scheduler");

        scheduler.start();

        Thread.sleep(500000);

        logger.info("shutdown scheduler");
        scheduler.shutdown();
    }

    //综上，若是执行任务很耗时，那么可以执行的job数量约等于线程数。
    private static void oneThreadJobSlowTest3() {

    }

}
