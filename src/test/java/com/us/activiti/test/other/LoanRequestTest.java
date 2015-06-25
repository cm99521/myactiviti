package com.us.activiti.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.viti.activiti.javabean.LoanApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class LoanRequestTest {

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
				.addClasspathResource("diagrams/LoadRequest.bpmn").deploy();

		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("income", 100l);
		processVariables.put("loanAmount", 10l);
		processVariables.put("emailAddress", "cliff.ma@unistacks.com");

		ProcessInstance pi = runtimeService.startProcessInstanceByKey("loanrequest",
				processVariables);
		List<HistoricDetail> historyVariables = historyService
				.createHistoricDetailQuery().variableUpdates().orderByTime().desc().listPage(0, 6);
				//.orderByVariableName().asc().list();
		assertNotNull(historyVariables);;
		assertEquals(6, historyVariables.size());
		
		
		HistoricVariableUpdate loanAppUpdate = null;
		for(HistoricDetail hd : historyVariables){
			HistoricVariableUpdate up = (HistoricVariableUpdate)  hd;
			if(up.getVariableName().equals("loanApplication")){
				loanAppUpdate = up;
			}
		}
		assertEquals("loanApplication", loanAppUpdate.getVariableName());
		LoanApplication la = (LoanApplication) loanAppUpdate.getValue();
		assertEquals(true, la.getCreditCheckOK());
		
		
		Task task = taskService.createTaskQuery().taskAssignee("fozzie").orderByTaskCreateTime().desc().list().get(0);
		int managerTaskSize = taskService.createTaskQuery().taskCandidateGroup("management").list().size();
		assertNotNull(task);
		taskService.claim(task.getId(), "fozzie");
		assertEquals(managerTaskSize,0);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Task taskManager = taskService.createTaskQuery().taskCandidateGroup("management").orderByTaskCreateTime().desc().list().get(0);
		assertNotNull(taskManager);
		processVariables.put("requestApproved", false);
		taskService.complete(task.getId(), processVariables);

	}
}