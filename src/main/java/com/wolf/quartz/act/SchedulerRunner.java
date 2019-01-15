package com.wolf.quartz.act;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description:整体入口
 * <br/> Created on 04/08/2018 4:33 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SchedulerRunner {

    static Logger logger = LoggerFactory.getLogger(SchedulerRunner.class);

    public static void main(String[] args) {

        // 加载quartz配置文件
        System.setProperty("org.quartz.properties", "./conf/quartz.properties");

        // 执行任务解析与调度
        run();
    }

    public static void run(){
        // 获取配置信息表
        List<TaskInfo> taskInfos = GenerateTaskInfo.generateTaskInfoFromMysql();
        if(taskInfos.size() == 0){
            logger.info("there is no tasks from mongoInfo");
            return;
        }

        // 过滤下线任务(这也可以从db中where直接过滤)
        taskInfos = GenerateTaskInfo.filterTask(taskInfos);
        if(taskInfos.size() == 0){
            logger.info("all tasks if offline, no need to run");
            return;
        }

        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        if(scheduler == null){
            logger.error("create scheduler failed");
            return;
        }

        if(isSchedulerClear()){
            clearSchedulerJob(scheduler);
        }

        // 加入任务调度
        for(TaskInfo task : taskInfos){
            SchedulerFactory.addJob2Scheduler(task, scheduler);
        }

        // 加入动态更新任务
        SchedulerFactory.addDynamicUpdateJob2Scheduler(scheduler);

        // 开启任务
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("start scheduler error!");
        }
    }

    public static void clearSchedulerJob(Scheduler scheduler){
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            logger.error("clear  scheduler error!");
        }
    }

    /**
     * 基于配置文件中的信息，加载调度器开始运行时的清洗标识
     * @return
     */
    private static boolean isSchedulerClear(){

        return true;
    }
}
