package com.us.activiti.test.event.timerevent;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import com.us.activiti.test.BaseJunitTest;

/**
 * Event test
 * 
 * @author Cliff Ma
 */
public class EventTest extends BaseJunitTest{

	@Test
	@Deployment(resources = "diagrams/event/timerEvent/timerStartEvent.bpmn")
    public void testTriggerAutomatic() throws Exception {
        // 部署之后引擎会自动创建一个定时启动事件的Job
        JobQuery jobQuery = managementService.createJobQuery();
        assertEquals(1, jobQuery.count());

        // 模拟时间30秒之后
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        System.out.println(".... start ...." + sdf.format(new Date()));
        processEngineConfiguration.getClock().setCurrentTime(new Date(System.currentTimeMillis() + 30000));
        waitForJobExecutorToProcessAllJobs(5000L, 1L);
        System.out.println(".... end ...." + sdf.format(new Date()));

        assertEquals(0, jobQuery.count());

        // 检查是否启动了流程实例
        long count = runtimeService.createProcessInstanceQuery().processDefinitionKey("timerStartEvent").count();
        assertEquals(1, count);
    }

	@Test
    @Deployment(resources = "diagrams/event/timerEvent/timerStartEvent.bpmn")
    public void testTriggerManual() throws Exception {
        // 部署之后引擎会自动创建一个定时启动事件的Job
        JobQuery jobQuery = managementService.createJobQuery();
        assertEquals(1, jobQuery.count());

        // 手动触发作业的执行
        Job job = jobQuery.singleResult();
        managementService.executeJob(job.getId());

        assertEquals(0, jobQuery.count());

        // 检查是否启动了流程实例
        long count = runtimeService.createProcessInstanceQuery().processDefinitionKey("timerStartEvent").count();
        assertEquals(1, count);
    }

}
