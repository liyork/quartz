package com.wolf.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description:
 * <br/> Created on 04/08/2018 2:10 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SimpleSchedulerListener implements SchedulerListener {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSchedulerListener.class);

    @Override
    public void jobScheduled(Trigger trigger) {
        JobKey jobKey = trigger.getJobKey();
        logger.info("job has been scheduled,jobkey:{}", jobKey);
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        logger.info("job is being unscheduled,triggerkey:{}"+triggerKey);
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        logger.info("trigger is finished,jobkey :{}",trigger.getJobKey());
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        logger.info("trigger is being paused,trigerkey:{}",triggerKey);
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        logger.info("trigger group is being paused,triggergroup:{}",triggerGroup);
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        logger.info("trigger is being resumed,triggerkey:{}",triggerKey);
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        logger.info("trigger group is being resumed,triggergroup:{}",triggerGroup);
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        logger.info("jobdetail is added,jobkey:{}",jobDetail.getKey());
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        logger.info("jobkey is deleted,jobkey:{}",jobKey);
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        logger.info("jobkey is paused,jobkey:{}",jobKey);
    }

    @Override
    public void jobsPaused(String jobGroup) {
        logger.info("job group is paused,jobgroup:{}",jobGroup);
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        logger.info("job is resumed,jobkey:{}",jobKey);
    }

    @Override
    public void jobsResumed(String jobGroup) {
        logger.info("jobgroup is resumed,jobgroup:{}",jobGroup);
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        logger.error(msg,cause.getUnderlyingException());
    }

    @Override
    public void schedulerInStandbyMode() {
        logger.info("scheduler is in standby mode");
    }

    @Override
    public void schedulerStarted() {
        logger.info("scheduler has been started");
    }

    @Override
    public void schedulerStarting() {
        logger.info("scheduler is starting");
    }

    @Override
    public void schedulerShutdown() {
        logger.info("scheduler has been shutdown");
    }

    @Override
    public void schedulerShuttingdown() {
        logger.info("scheduler is being shutdown");
    }

    @Override
    public void schedulingDataCleared() {
        logger.info("scheduler has cleared all data");
    }
}
