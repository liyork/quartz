package com.wolf.quartz.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Description:构造信息和job
 * <br/> Created on 04/08/2018 4:34 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class GenerateTaskInfo {

    static Logger logger = LoggerFactory.getLogger(GenerateTaskInfo.class);

    /**
     * 从数据库中读取任务相关信息
     *
     * @return
     */
    public static List<TaskInfo> generateTaskInfoFromMongo() {
        // 将任务信息进行封装
        List<TaskInfo> tasks = new ArrayList<>();
        TaskInfo task = new TaskInfo();
        tasks.add(task);

        return tasks;
    }


    /**
     * 基于业务表中的信息构造定时任务表达式
     *
     * @return
     */
    public static String generateCronInfo() {


        StringBuilder sb = new StringBuilder();
        sb.append("0/10 * * * * ?");

        return sb.toString();
    }

    /**
     * 过滤下线的任务
     *
     * @param tasks
     * @return
     */
    public static List<TaskInfo> filterTask(List<TaskInfo> tasks) {
        List<TaskInfo> taskInfos = new ArrayList<>();
        for (TaskInfo taskInfo : tasks) {
            // 过滤下线的信源状态或实时的信源
            if (taskInfo.getSourceStatus() == 0
                    || taskInfo.getSourceType() != 0) {
                continue;
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }

    /**
     * 基于业务名称对任务进行分组
     *
     * @param oriTasks
     * @return
     */
    public static Map<String, List<TaskInfo>> groupTaskByCategory(List<TaskInfo> oriTasks) {
        Map<String, List<TaskInfo>> categoryTasks = new HashMap<>();
        for (TaskInfo oriTask : oriTasks) {
            if (!categoryTasks.containsKey(oriTask.getCategoryId())) {
                List<TaskInfo> taskInfos = new ArrayList<>();
                taskInfos.add(oriTask);
                categoryTasks.put(oriTask.getCategoryId(), taskInfos);
            } else {
                boolean hasSameSourceId = false;
                for (TaskInfo taskInfo : categoryTasks.get(oriTask.getCategoryId())) {
                    if (taskInfo.getSourceId().equals(oriTask.getSourceId())) {
                        hasSameSourceId = true;
                        break;
                    }
                }
                if (!hasSameSourceId) {
                    categoryTasks.get(oriTask.getCategoryId()).add(oriTask);
                }
            }
        }
        return categoryTasks;
    }

    public static List<TaskInfo> generateTaskInfoFromMysql() {
        return null;
    }
}
