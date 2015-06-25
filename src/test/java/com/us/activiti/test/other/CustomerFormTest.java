package com.us.activiti.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
public class CustomerFormTest{

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
				.addClasspathResource("diagrams/CustomerForm.bpmn")
				.addClasspathResource("diagrams/first-step.form")
				.addClasspathResource("diagrams/second-step.form")
				.addClasspathResource("diagrams/start.form")
				.deploy();
		
		// Get start form
		String procDefId = repositoryService.createProcessDefinitionQuery().deploymentId(de.getId()).singleResult().getId();
		Object startForm = formService.getRenderedStartForm(procDefId);
		assertNotNull(startForm);
		
		assertEquals("<input id=\"start-name\" />", startForm);
		
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("startName", "HenryYan");
		ProcessInstance pi = formService.submitStartFormData(procDefId, formProperties);
		assertNotNull(pi);
		
		Task task = taskService.createTaskQuery().taskAssignee("user1").singleResult();
		Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
		assertEquals("<input id=\"start-name\" value=\"HenryYan\" />\n<input id=\"first-name\" />", renderedTaskForm);
		
		formProperties = new HashMap<String, String>();
		formProperties.put("firstName", "kafeitu");
		formService.submitTaskFormData(task.getId(), formProperties);
		
		task = taskService.createTaskQuery().taskAssignee("user2").processInstanceId(pi.getId()).singleResult();
		assertNotNull(task);
		renderedTaskForm = formService.getRenderedTaskForm(task.getId());
		assertEquals("<input id=\"first-name\" value=\"kafeitu\" />", renderedTaskForm);
	
	}

}