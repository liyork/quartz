Quartz is a richly featured, open source job scheduling library that can be integrated within virtually any Java application
Quartz can be used to create simple or complex schedules for executing tens, hundreds, or even tens-of-thousands of jobs; jobs whose tasks are defined as standard Java components that may execute virtually anything you may program them to do
quartz就是基于java实现的任务调度框架，用于执行你想要执行的任何任务

quart主要有核心模块：
Job(被执行的任务)、
JobDetail：Conveys the detail properties of a given Job instance,Quartz store a JobDetail
Trigger(触发时机， a single Trigger can only point to one Job)、
Scheduler(Scheduler is responsible for executing Job when their associated Trigger fire)

优先级仅用于所有相同时间触发的triggers

"Misfire  Instructions"

秒 分 时 日 月 周 年


集群中的每个节点是一个独立的Quartz应用，且独立的Quartz节点并不与另一节点通信，而是通过相同的数据库表来感知另一Quartz应用

Scheduler可以处于 standby 模式，即“待机”模式，处于该模式下的Scheduler不能 触发Job执行条件，不能执行作业。


分组用来管理一批job

线程数一般要大于job数，不然有job慢了，可能后面就有job执行不了了
不需要并发执行的并发了，就多执行了没用的次数。浪费资源。

最佳实践：
org.quartz.scheduler.skipUpdateCheck=true
在JobDataMap应该只存储原始的数据类型（包括字符串），这样可以避免数据序列化的问题以及长期运行的问题。
Job.execute()方法通常应该从JobExecutionContext发现的JobDataMap中取回数据，而不是直接从JobDetail中取数据。
使用TriggerUtils
一定不要直接写数据到Quartz表(通过SQL语句写调度数据到数据库表，而不应该使用调度API来写数据)
一定不要把多个非集群的调度器实例指向同一个数据库表
官方推荐数据源的最大连接数应该配置为线程池的最小工作线程数的3倍
避免调度Job的时间安排在夏令时转换的交界处
job执行时间不要太长。
job内部不要抛出异常。让Job能够捕获它可能会遇到的所有异常，处理这些异常，然后再重调度Job
可恢复性和幂等性
在监听器中保持代码简洁高效
监听器不要抛出异常
暴露Quartz调度器的api功能要谨慎。
job中，check jobExecutionContext.getScheduler().isShutdown() periodically or program your job to become InterruptableJob and properly respond to interruptions.
@PersistJobDataAfterExecution配置时，一起带上@DisallowConcurrentExecution，防止并发产生的问题，两个线程都使用一个map
