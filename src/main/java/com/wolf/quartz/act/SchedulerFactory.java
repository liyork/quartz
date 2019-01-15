package com.wolf.quartz.act;

import com.alibaba.fastjson.JSON;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:工厂，操作job、trigger、scheduler
 * <br/> Created on 04/08/2018 4:35 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SchedulerFactory {
    static Logger logger = LoggerFactory.getLogger(SchedulerFactory.class);

    public static final String CONF_INFO = "conf_info";
    public static final String DYNAMIC_UPDATE_JOB_NAME = "dynamicUpdateJob";
    public static final String DYNAMIC_UPDATE_GROUP_NAME = "dynamicUpdateGroup";
    public static final String DYNAMIC_UPDATE_CRONTINFO = "*/30 * * * * ?";

    /**
     * 将任务加入任务调度中
     * @param taskInfo
     * @param scheduler
     */
    public static void addJob2Scheduler(TaskInfo taskInfo, Scheduler scheduler) {
        try {
            JobDetail jobDetail = generateJobDetail(taskInfo);
            if(jobDetail == null){
                logger.error("create job failed!");
                return;
            }

            Trigger triger = generateTrigger(taskInfo);
            if(triger == null){
                logger.error("create trigger failed!");
                return;
            }

            // 加载执行Job及定时器
            scheduler.scheduleJob(jobDetail,triger);
        } catch (SchedulerException e) {
            logger.error("create scheduler error, error message: "+e.toString());
        }
    }

    public static void addDynamicUpdateJob2Scheduler(Scheduler scheduler) {
        try {
            JobDetail jobDetail = generateDynamicUpdateJobDetail(DYNAMIC_UPDATE_JOB_NAME, DYNAMIC_UPDATE_GROUP_NAME);
            if(jobDetail == null){
                logger.error("create job failed!");
                return;
            }

            Trigger triger = generateTrigger(DYNAMIC_UPDATE_JOB_NAME, DYNAMIC_UPDATE_GROUP_NAME, DYNAMIC_UPDATE_CRONTINFO);
            if(triger == null){
                logger.error("create trigger failed!");
                return;
            }

            // 加载执行Job及定时器
            scheduler.scheduleJob(jobDetail,triger);
        } catch (SchedulerException e) {
            logger.error("create scheduler error, error message: "+e.toString());
        }
    }

    /**
     * 于信源信息生成对应的job
     * @param taskInfo
     * @return
     */
    public static JobDetail generateJobDetail(TaskInfo taskInfo) {
        String jobName = taskInfo.getSourceName();
        if(jobName.trim().length() == 0){
            logger.error("job name is empty, please check!");
            return null;
        }

        String jobGroup = taskInfo.getCategoryName();
        if(jobGroup.trim().length() == 0){
            logger.error("job group is empty, please check!");
            return null;
        }

        return JobBuilder.newJob(ScheduleJob.class)
                .withIdentity(jobName, jobGroup)
                .requestRecovery()//可恢复if a 'recovery' or 'fail-over' situation is encountered.
                .usingJobData(CONF_INFO, JSON.toJSONString(taskInfo)).build();
    }

    public static JobDetail generateDynamicUpdateJobDetail(String jobName, String jobGroup) {
        if(jobName.trim().length() == 0){
            logger.error("job name is empty, please check!");
            return null;
        }

        if(jobGroup.trim().length() == 0){
            logger.error("job group is empty, please check!");
            return null;
        }

        return JobBuilder.newJob(DynamicUpdateJob.class)
                .withIdentity(jobName, jobGroup)
                .requestRecovery()
                .build();
    }

    /**
     * 基于信源信息生成对应的trigger
     * @param taskInfo
     * @return
     */
    public static Trigger generateTrigger(TaskInfo taskInfo) {
        String sourceTriggerName = taskInfo.getSourceName();
        if(sourceTriggerName.trim().length() == 0){
            logger.error("trigger name is empty, please check!");
            return null;
        }
        String sourceTriggerGroup = taskInfo.getCategoryName();
        if(sourceTriggerGroup.trim().length() == 0){
            logger.error("trigger group is empty, please check!");
            return null;
        }
        String cronInfo = taskInfo.getCronInfo();
        if(cronInfo.trim().length() == 0){
            logger.error("cron timer info is empty, please check!");
            return null;
        }
        return TriggerBuilder.newTrigger().withIdentity(sourceTriggerName,
                sourceTriggerGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronInfo))
                .build();
    }

    public static Trigger generateTrigger(String sourceTriggerName, String sourceTriggerGroup, String cronInfo) {
        if(sourceTriggerName.trim().length() == 0){
            logger.error("trigger name is empty, please check!");
            return null;
        }
        if(sourceTriggerGroup.trim().length() == 0){
            logger.error("trigger group is empty, please check!");
            return null;
        }
        if(cronInfo.trim().length() == 0){
            logger.error("cron timer info is empty, please check!");
            return null;
        }
        return TriggerBuilder.newTrigger().withIdentity(sourceTriggerName,
                sourceTriggerGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronInfo))
                .build();
    }
}
