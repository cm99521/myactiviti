package org.activiti.designer.test;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans-jpa.xml")
public class SingnalEventTest {

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
				.addClasspathResource("diagrams/SingnalEvent.bpmn").deploy();

		Map<String, Object> processVariables = new HashMap<String, Object>();

		ProcessInstance pi = runtimeService.startProcessInstanceByKey(
				"singnalEvent", processVariables);

	}
}