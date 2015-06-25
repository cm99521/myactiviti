package com.us.activiti.test.event.throwerror;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

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

	@Deployment(resources = "diagrams/event/boundaryEvent/throwErrorManual.bpmn")
	@Test
	public void testThrowErrorManual() throws Exception {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("throwErrorManual");
		assertNotNull(processInstance);

		// 流转至用户任务
		Task task = taskService.createTaskQuery().singleResult();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("pass", true);
		taskService.complete(task.getId(), variables);

		// 流程执行完成
		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		assertEquals(1, count);
	}

}
