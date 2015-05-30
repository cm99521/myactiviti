package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class FormTest {

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
				.addClasspathResource("diagrams/StartForm.bpmn").deploy();
		
		ProcessDefinition definition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey("startFormTest").orderByProcessDefinitionVersion().desc().list().get(0);
		
		assertNotNull(definition);
		List<FormProperty> formList = formService.getStartFormData(
				definition.getId()).getFormProperties();
		
		assertEquals(4, formList.size());
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("name", "Miss Piggy");
		formProperties.put("emailAddress", "piggy@localhost");
		formProperties.put("income", "400");
		formProperties.put("loanAmount", "100");
		formService.submitStartFormData(definition.getId(), formProperties);
		
		List<HistoricDetail> historyVariables = historyService
				.createHistoricDetailQuery().formProperties().orderByTime().desc().listPage(0, 4);
		
		assertNotNull(historyVariables);
		assertEquals(4, historyVariables.size());
		HistoricFormProperty formProperty = (HistoricFormProperty) historyVariables
				.get(0);
		assertEquals("loanAmount", formProperty.getPropertyId());
		assertEquals("100", formProperty.getPropertyValue());
		formProperty = (HistoricFormProperty) historyVariables.get(1);
		assertEquals("income", formProperty.getPropertyId());
		assertEquals("400", formProperty.getPropertyValue());

	}
}