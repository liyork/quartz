package com.wolf.quartz.act;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:动态job，动态检测配置信息并更新job，从按照配置信息更新正在执行的job，也可以另开线程执行。
 * <br/> Created on 04/08/2018 4:35 AM
 *
 * @author 李超
 * @since 1.0.0
 */
@DisallowConcurrentExecution
public class DynamicUpdateJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(DynamicUpdateJob.class);

    public DynamicUpdateJob(){}

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();

        JSONObject json = new JSONObject();
        json.put("jobName", jobDetail.getKey().getName());
        json.put("jobGroup", jobDetail.getKey().getGroup());
        json.put("triggerName", jobExecutionContext.getTrigger().getKey().getName());
        json.put("triggerGroup", jobExecutionContext.getTrigger().getKey().getGroup());

        logger.info("job is running: "+json.toString());

        // 获取当前的调度器
        Scheduler scheduler = jobExecutionContext.getScheduler();

        // 获取配置信息中的任务(注意需要保持)
        List<TaskInfo> confTaskInfos = GenerateTaskInfo.generateTaskInfoFromMysql();

        // 获取所有的job信息
        List<JobKey> schedulerJobKeys = acquireJobKeysWithinSceduler(scheduler);


        //整体来讲还是以配置的任务为准进行上下线正在执行的任务(若是配置新增的则create，若还在配置则更新，若不在配置了则删除)
        //正在执行的任务[b,d]    配置任务[a,b,c]
        //d-delete

        // 1. 配置任务不存在，而sheduler相关任务存在，则进行下线处理
        for(JobKey schedulerJobKey : schedulerJobKeys ){
            boolean hasSameJobKeyInConfTask = false;
            for(TaskInfo confTaskInfo : confTaskInfos){
                if(generateJobKey(confTaskInfo).equals(schedulerJobKey)){
                    hasSameJobKeyInConfTask = true;
                    break;
                }
            }
            if(!hasSameJobKeyInConfTask){
                try {
                    scheduler.deleteJob(schedulerJobKey);
                    logger.info("delete offline job: "+schedulerJobKey.toString());
                } catch (SchedulerException e) {
                    logger.error("delete offline job error: "+json.toString());
                }
            }
        }

        //配置任务[a,b,c]  正在执行的任务[b,d]
        //a-create/b-update or delete/c-create

        // 2 配置任务与调度器任务比较
        for(TaskInfo confTaskInfo : confTaskInfos){
            JobKey confJobKey = generateJobKey(confTaskInfo);

            boolean hasSameJob = false;
            for(JobKey schedulerJobKey : schedulerJobKeys ){
                if(confJobKey.equals(schedulerJobKey)){
                    hasSameJob = true;
                    break;
                }
            }

            if(hasSameJob){  //具有相同名称的job
                logger.info("has same jobKey: "+confJobKey);
                JobDetail schedulerJobDetail = null;
                try {
                    schedulerJobDetail = scheduler.getJobDetail(confJobKey);
                } catch (SchedulerException e) {
                    logger.error("get job detail from scheduler error: "+confJobKey);
                }
                if(schedulerJobDetail == null) continue;

                // 1) 是否需要下线
                if(!ScheduleJob.isNeedtoRun(confTaskInfo)){
                    try {
                        logger.info("has same jobKey and offline the job "+confJobKey);
                        scheduler.deleteJob(confJobKey);
                    } catch (SchedulerException e) {
                        logger.error("delete offline job error: "+confJobKey);
                    }
                }else{
                    // 2) 是否需要更新任务
                    TaskInfo schedulerTaskInfo = parseTaskInfoFromJobDataMap(schedulerJobDetail);
                    logger.info("confTaskInfo: " + confTaskInfo);
                    logger.info("schedulerTaskInfo: " + schedulerTaskInfo);
                    if(!confTaskInfo.equals(schedulerTaskInfo)){
                        try {
                            logger.info("has same jobKey and update the job "+confJobKey);
                            scheduler.deleteJob(confJobKey);
                            com.wolf.quartz.act.SchedulerFactory.addJob2Scheduler(confTaskInfo, scheduler);
                        } catch (SchedulerException e) {
                            logger.error("update scheduler info error: "+confJobKey);
                        }
                    }else{
                        logger.info("the job info is same "+confJobKey);
                    }
                }
            }else{ // 创建新的Job
                // 1) 是否满足上线的条件
                if(!ScheduleJob.isNeedtoRun(confTaskInfo)){
                    logger.info("the status is offline, no need to create new job: "+confJobKey);
                    continue;
                }

                logger.info("no same jobKey and create job "+confJobKey);

                // 2) 上线
                com.wolf.quartz.act.SchedulerFactory.addJob2Scheduler(confTaskInfo, scheduler);
            }
        }
    }

    protected List<JobKey> acquireJobKeysWithinSceduler(Scheduler scheduler){
        List<JobKey> jobKeys = new ArrayList<>();
        try {
            for(String groupName : scheduler.getJobGroupNames()){
                if(groupName.equals(com.wolf.quartz.act.SchedulerFactory.DYNAMIC_UPDATE_GROUP_NAME)){
                    continue;
                }
                for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))){
                    jobKeys.add(jobKey);
                }
            }
        } catch (SchedulerException e) {
        }
        return jobKeys;
    }

    protected  TaskInfo parseTaskInfoFromJobDataMap(JobDetail jobDetail){
        try {
            String confInfo = jobDetail.getJobDataMap().getString(SchedulerFactory.CONF_INFO);
            return JSON.toJavaObject(JSONObject.parseObject(confInfo), TaskInfo.class);
        } catch (Exception e) {
            logger.error("parse task info from JobDataMap error!");
            return null;
        }
    }

    protected JobKey generateJobKey(TaskInfo taskInfo){
        return generateJobKey(taskInfo.getSourceName(), taskInfo.getCategoryName());
    }

    protected JobKey generateJobKey(String jobName, String jobGroup){
        return JobKey.jobKey(jobName,jobGroup);
    }
}
