package com.wolf.quartz.act;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:执行业务逻辑job,顺序执行
 * <br/> Created on 04/08/2018 4:34 AM
 *
 * @author 李超
 * @since 1.0.0
 */
@DisallowConcurrentExecution
public class ScheduleJob implements Job {
    static Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

    public ScheduleJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        jobExecutionContext.getMergedJobDataMap()官方推荐从这里取

        JobDetail jobDetail = jobExecutionContext.getJobDetail();

        JSONObject json = new JSONObject();
        json.put("jobName", jobDetail.getKey().getName());
        json.put("jobGroup", jobDetail.getKey().getGroup());
        json.put("triggerName", jobExecutionContext.getTrigger().getKey().getName());
        json.put("triggerGroup", jobExecutionContext.getTrigger().getKey().getGroup());

        logger.info("job is running: " + json.toString());

        JobDataMap dataMap = jobDetail.getJobDataMap();
        JSONObject confJson = null;
        try {
            confJson = JSONObject.parseObject(dataMap.getString(SchedulerFactory.CONF_INFO));
        } catch (JSONException e) {
        }
        if (confJson == null) {
            logger.error("conf is empty: " + json.toString());
            return;
        }

        // 获取存储类型
        TaskInfo taskInfo = JSON.toJavaObject(confJson, TaskInfo.class);
        if (!isNeedtoRun(taskInfo)) {
            logger.info("no need to run: " + json.toString());
            return;
        }

        System.out.println("business aolorithem....");
    }

    /**
     * 判断job是否需要执行
     *
     * @param taskInfo
     * @return
     */
    public static boolean isNeedtoRun(TaskInfo taskInfo) {

        // 实时or离线
        // job的上下线状态

        return true;
    }

}
