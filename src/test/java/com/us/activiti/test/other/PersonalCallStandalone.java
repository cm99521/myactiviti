package com.us.activiti.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class PersonalCallStandalone{

	Logger log = LoggerFactory.getLogger(PersonalCallStandalone.class);
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

	@BeforeClass
	public static void routeLoggingToSlf4j() {
		
	}

	@Test
	public void executeJavaService() throws Exception {

		Deployment de = repositoryService.createDeployment()
				.addClasspathResource("diagrams/PersonalMobileContract.bpmn").deploy();
		
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("personalMobileContract").deploymentId(de.getId()).singleResult();
		
		List<FormProperty> formList = formService.getStartFormData(pd.getId())
				.getFormProperties();
		assertEquals(2, formList.size());

		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("customerNumber", "11");
		formProperties.put("contractType", "c4");

		ProcessInstance pi = formService.submitStartFormData(pd.getId(), formProperties);
		
		List<HistoricDetail> historyVariables = historyService
				.createHistoricDetailQuery().formProperties().processInstanceId(pi.getId()).list();

		assertNotNull(historyVariables);
		assertEquals(2, historyVariables.size());

		HistoricFormProperty formProperty = (HistoricFormProperty) historyVariables
				.get(0);
		assertEquals("contractType", formProperty.getPropertyId());
		assertEquals("c4", formProperty.getPropertyValue());
		
		formProperty = (HistoricFormProperty) historyVariables
				.get(1);
		assertEquals("customerNumber", formProperty.getPropertyId());
		assertEquals("11", formProperty.getPropertyValue());
		


	}

}