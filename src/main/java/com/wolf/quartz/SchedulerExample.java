package com.wolf.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Description:
 * <br/> Created on 03/08/2018 10:10 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SchedulerExample {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerExample.class);

    public static void main(String[] args) throws SchedulerException {

//        testInitializeFromHardCode();
        testInitializeFromPropertiesFile();

    }

    private static void testInitializeFromHardCode() throws SchedulerException {
        StdSchedulerFactory factory = new StdSchedulerFactory();

        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
//        properties.put("org.quartz.threadPool.threadCount", 10);//2.3.0版本使用10还不行，String sval = (oval instanceof String) ? (String)oval : null;内部还有个坑！。
        properties.put("org.quartz.threadPool.threadCount", "10");

        factory.initialize(properties);

        Scheduler scheduler = factory.getScheduler();

        scheduler.start();

        logger.info("scheduler started,metadata:{}",scheduler.getMetaData());
    }

    private static void testInitializeFromPropertiesFile() throws SchedulerException {

        System.out.println("org.quartz.properties:"+System.getProperty("org.quartz.properties"));//null则从classpath中读取

        StdSchedulerFactory factory = new StdSchedulerFactory();

        factory.initialize();

        Scheduler scheduler = factory.getScheduler();

        scheduler.start();

        logger.info("scheduler started,metadata:{}",scheduler.getMetaData());
    }
}
