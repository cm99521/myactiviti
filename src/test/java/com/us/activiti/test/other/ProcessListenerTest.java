package com.us.activiti.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans-jpa.xml")
public class ProcessListenerTest {

	@PersistenceContext
	private EntityManager entityManager;

	Logger log = LoggerFactory.getLogger(JPABookTest.class);
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

	@Test
	public void executeJavaService() throws Exception {
		Deployment de = repositoryService.createDeployment()
				.addClasspathResource("diagrams/ListenerProcess.bpmn").deploy();

		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey("processListener");
		assertNotNull(pi);
		
		Task task = taskService.createTaskQuery().taskAssignee("John")
				.singleResult();
		taskService.complete(task.getId());
		
		List<HistoricDetail> historyList = historyService
				.createHistoricDetailQuery().variableUpdates().processInstanceId(pi.getId()).list();
		
		assertEquals(9, historyList.size());
		
		HistoricVariableUpdate variableUpdate = (HistoricVariableUpdate) historyList
				.get(historyList.size() - 1);
		assertEquals("eventList", variableUpdate.getVariableName());
		
		List<String> variableList = (List<String>) variableUpdate.getValue();
		assertEquals("process:start", variableList.get(0));
		assertEquals("transition:take", variableList.get(1));
		assertEquals("activity:start", variableList.get(2));
		assertEquals("process:end", variableList.get(variableList.size() - 1));
	}

}