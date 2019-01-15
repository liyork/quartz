package com.wolf.quartz;

import org.quartz.*;

import java.util.Date;
import java.util.TimeZone;

/**
 * Description:
 * <br/> Created on 03/08/2018 10:23 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class TriggerDemo {

    public static void main(String[] args) {
        testSimpleSchedule();

        testCronSchedule();

    }

    private static void testSimpleSchedule() {
        //定时执行，排除某些日期
        TriggerBuilder.newTrigger().withIdentity("aaa","group")
                .forJob("jobName").withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .modifiedByCalendar("calName")
                .build();
        //特定时间触发，非重复
        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .startAt(new Date())
                .forJob("job1", "group1")
                .build();


        //指定时间触发，间隔8s，执行10次
        SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(8).withRepeatCount(10);
        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .startAt(new Date())
                .withSchedule(schedBuilder)
                .forJob("job1", "group1")
                .build();


        //5分钟后执行，触发一次
        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE))
                .forJob("job1", "group1")
                .build();


        //立即执行，22点结束
        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .withSchedule(schedBuilder.repeatForever())
                .forJob("job1", "group1")
                .endAt(DateBuilder.dateOf(22,0,0))
                .build();

        //下一小时触发，间隔2小时
        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .startAt(DateBuilder.evenHourDate(null))
                .withSchedule(schedBuilder.withIntervalInHours(2).repeatForever())
                .forJob("job1", "group1")
                .build();

        //设定未启动指令
        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .withSchedule(schedBuilder.withIntervalInHours(2).repeatForever().withMisfireHandlingInstructionNextWithExistingCount())
                .forJob("job1", "group1")
                .build();
    }

    private static void testCronSchedule() {

        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?"))
                .forJob("name", "group")
                .build();

        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(10,42))
                .forJob("name", "group")
                .build();


        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.weeklyOnDayAndHourAndMinute(DateBuilder.WEDNESDAY, 10, 42)
                .inTimeZone(TimeZone.getTimeZone("Asiz/Shanghai"));
        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .withSchedule(cronScheduleBuilder)
                .forJob("name", "group")
                .build();


        //未启动指令处理方式
        CronScheduleBuilder schedBuilder = cronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?");
        schedBuilder.withMisfireHandlingInstructionFireAndProceed();

        TriggerBuilder.newTrigger()
                .withIdentity("name", "group")
                .withSchedule(schedBuilder)
                .forJob("name", "group")
                .build();
    }
}
