package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class JavaBPMNTest {

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

	private ProcessInstance startProcessInstance() {

		Deployment dep = repositoryService
				.createDeployment()
				.addClasspathResource(
						"diagrams/bookorder_with_javaservice.bpmn").deploy();

		List<ProcessDefinition>  defs = repositoryService
				.createProcessDefinitionQuery().list();
		
		
		String myDefId = "";
		for(ProcessDefinition def : defs){
			if(dep.getId().equals(def.getDeploymentId())){
				myDefId = def.getId();
				break;
			}
		}
		
		assertNotNull(myDefId);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", 3456L);
		return runtimeService.startProcessInstanceById(myDefId, variableMap);
	}

	@Test
	public void executeJavaService() {
		ProcessInstance processInstance = startProcessInstance();
		
		
	}
}