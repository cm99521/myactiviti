package com.us.activiti.test.event.terminateendevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import com.us.activiti.test.BaseJunitTest;

/**
 * Event test
 * 
 * @author Cliff Ma
 */
public class EventTest extends BaseJunitTest {

	 /**
     * 先完成【任务A】，流程未结束
     */
	@Test
    @Deployment(resources = "diagrams/event/terminateEndEvent/terminateEndEvent.bpmn")
    public void testOne() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEvent");
        assertNotNull(processInstance);

        // 完成第一个
        Task task1 = taskService.createTaskQuery().taskDefinitionKey("usertask1").singleResult();
        taskService.complete(task1.getId());

        // 流程还在运行
        long count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count();
        assertEquals(1, count);

        Task task2 = taskService.createTaskQuery().taskDefinitionKey("usertask2").singleResult();
        taskService.complete(task2.getId());

        // 流程已结束
        count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count();
        assertEquals(0, count);
    }

    /**
     * 直接完成【任务B】整个流程实例结束
     */
	@Test
    @Deployment(resources = "diagrams/event/terminateEndEvent/terminateEndEvent.bpmn")
    public void testSecond() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEvent");
        assertNotNull(processInstance);

        // 完成第一个
        Task task1 = taskService.createTaskQuery().taskDefinitionKey("usertask2").singleResult();
        taskService.complete(task1.getId());

        // 流程已经结束
        long count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count();
        assertEquals(0, count);
    }
	
	
	/**
     * 先完成子流程，流程未结束
     */
	@Test
    @Deployment(resources = "diagrams/event/terminateEndEvent/terminateEndEventWithSubprocess.bpmn")
    public void testOneWithSubProcess() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEventWithSubprocess");
        assertNotNull(processInstance);

        // 查询子任务的
        Task task = taskService.createTaskQuery().taskDefinitionKey("subTask").singleResult();
        taskService.complete(task.getId());

        // 完成主流程用户任务
        task = taskService.createTaskQuery().taskDefinitionKey("masterTask").singleResult();
        taskService.complete(task.getId());

        checkFinished(processInstance);
    }

    /**
     * 直接触发“Receive Task”，流程结束
     */
	@Test
    @Deployment(resources = "diagrams/event/terminateEndEvent/terminateEndEventWithSubprocess.bpmn")
    public void testSecondWithSubProcess() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEventWithSubprocess");
        assertNotNull(processInstance);

        // 完成主流程用户任务
        Task task = taskService.createTaskQuery().taskDefinitionKey("masterTask").singleResult();
        taskService.complete(task.getId());

        checkFinished(processInstance);
    }

    private void checkFinished(ProcessInstance processInstance) {
        // 验证流程已结束
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        assertNotNull(historicProcessInstance.getEndTime());

        // 查询历史任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().list();
        for (HistoricTaskInstance hti : list) {
            System.out.println(hti.getName() + "  " + hti.getDeleteReason());
        }

        // 流程结束后校验监听器设置的变量
        HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().variableName("settedOnEnd").singleResult();
        assertEquals(true, variableInstance.getValue());
    }

}
