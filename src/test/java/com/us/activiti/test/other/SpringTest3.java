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
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class SpringTest3 {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;

	@Autowired
	private TaskService taskService;

	@Autowired
	@Rule
	public ActivitiRule activitiRule;

	@Autowired
	public HistoryService historyService;

	@Test
	@Deployment(resources = { "diagrams/loanrequest3.bpmn" })
	public void executeJavaService() throws Exception {

		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("loanRequest").singleResult();
		List<FormProperty> formList = formService.getStartFormData(pd.getId())
				.getFormProperties();
		assertEquals(4, formList.size());

		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("name", "Cliff");
		formProperties.put("income", "1001");
		formProperties.put("loanAmount", "20000");
		formProperties.put("emailAddress", "cliff.ma@localhost");

		formService.submitStartFormData(pd.getId(), formProperties);
		List<HistoricDetail> historyVariables = historyService
				.createHistoricDetailQuery().formProperties().list();

		assertNotNull(historyVariables);
		assertEquals(4, historyVariables.size());

		HistoricFormProperty formProperty = (HistoricFormProperty) historyVariables
				.get(1);
		assertEquals("loanAmount", formProperty.getPropertyId());
		assertEquals("20000", formProperty.getPropertyValue());

		formProperty = (HistoricFormProperty) historyVariables.get(2);
		assertEquals("income", formProperty.getPropertyId());
		assertEquals("1001", formProperty.getPropertyValue());

	}

}