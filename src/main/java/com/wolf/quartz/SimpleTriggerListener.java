package com.wolf.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * <br/> Created on 04/08/2018 1:40 AM
 *
 * @author 李超
 * @since 1.0.0
 */
@DisallowConcurrentExecution
public class SimpleTriggerListener implements TriggerListener {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTriggerListener.class);

    private String name;

    public SimpleTriggerListener(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        TriggerKey triggerKey = trigger.getKey();
        logger.info("triggerKey was fired,key:{}", triggerKey);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        TriggerKey triggerKey = trigger.getKey();
        logger.info("triggerkey was not vetoed,key:{}", triggerKey);
        return true;//true则本次job不会被执行，仅仅是本trigger控制的
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        TriggerKey triggerKey = trigger.getKey();
        logger.info("trigger misfired ,key:{}", triggerKey);
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        TriggerKey triggerKey = trigger.getKey();
        logger.info("trigger is complete ,key:{}", triggerKey);
    }
}
