package com.us.activiti.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans-jpa.xml")
public class SingnalIntermediateCatchTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;

	@Autowired
	private TaskService taskService;

	@Autowired
	public HistoryService historyService;
	@Autowired
	public IdentityService identityService;

	@Test
	public void executeJavaService() {
		repositoryService.createDeployment()
				.addClasspathResource("diagrams/SingnalIntermediateCatch.bpmn")
				.deploy();

		ProcessInstance pi = runtimeService.startProcessInstanceByKey("singnalIntermediateCatch");

		// 完成一个任务
		Task task1 = taskService.createTaskQuery()
				.processInstanceId(pi.getId()).singleResult();
		
		taskService.complete(task1.getId());

		//sendSignal(pi.getProcessInstanceId());

		Task task2 = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		
		taskService.complete(task2.getId());

		// 判断process instance已经结束
		assertEquals(0, runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(pi.getProcessInstanceId()).count());

	}

	/**
	 * 给特定的process definition的所有process instance发送signal
	 */
	private void sendSignal(String processInstanceId) {
		List<Execution> executions = runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId)
				.signalEventSubscriptionName("userFinished").list();
		for (Execution execution : executions) {
			runtimeService.signalEventReceived("userFinished",
					execution.getId());
		}
	}
}