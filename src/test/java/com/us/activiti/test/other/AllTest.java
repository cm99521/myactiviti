package com.us.activiti.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class AllTest {

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
	@Deployment(resources = { "diagrams/bookorder.bpmn" })
	public void executeJavaService() throws Exception {
		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("isbn", "123456");
		identityService.setAuthenticatedUserId("kermit");
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("bookorder", variableMap);

		assertNotNull(processInstance.getId());
		List<Task> taskList = taskService.createTaskQuery()
				.taskCandidateUser("kermit").list();
		assertEquals(1, taskList.size());

		System.out.println("found task " + taskList.get(0).getName());
		taskService.complete(taskList.get(0).getId());

	}

	@Test
	public void deleteDeployment() {
		String deploymentID = repositoryService.createDeployment()
				.addClasspathResource("diagrams/bookorder.bpmn").deploy()
				.getId();

		org.activiti.engine.repository.Deployment deployment = repositoryService
				.createDeploymentQuery().singleResult();
		assertNotNull(deployment);
		assertEquals(deploymentID, deployment.getId());
		System.out.println("Found deployment " + deployment.getId()
				+ ", deployed at " + deployment.getDeploymentTime());

		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().latestVersion().singleResult();
		assertNotNull(processDefinition);
		assertEquals("bookorder", processDefinition.getKey());
		System.out.println("Found process definition "
				+ processDefinition.getId());

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery().singleResult();

		assertNotNull(processInstance);
		assertEquals(processDefinition.getId(),
				processInstance.getProcessDefinitionId());

		repositoryService.deleteDeployment(deploymentID, true);
		deployment = repositoryService.createDeploymentQuery().singleResult();
		assertNull(deployment);
		processDefinition = repositoryService.createProcessDefinitionQuery()
				.singleResult();
		assertNull(processDefinition);

		processInstance = runtimeService.createProcessInstanceQuery()
				.singleResult();
		assertNull(processInstance);
	}

	@Test
	public void testAddUser() {

		List<User> users = identityService.createUserQuery().list();
		for (User u : users) {
			if ("Cliff Ma".equalsIgnoreCase(u.getId())) {
				identityService.deleteUser(u.getId());
			}
		}

		User newUser = identityService.newUser("Cliff Ma");
		identityService.saveUser(newUser);

		users = identityService.createUserQuery().list();
		assert (users.size() > 0);

		boolean found = false;
		for (User u : users) {
			if ("Cliff Ma".equalsIgnoreCase(u.getId())) {
				found = true;
			}
		}

		assert (found);

	}

	@Test
	public void testAddGroup() {
		List<Group> groups = identityService.createGroupQuery().list();
		Iterator<Group> it = groups.iterator();
		while (it.hasNext()) {
			Group g = it.next();
			if ("unistacks".equalsIgnoreCase(g.getId())) {
				identityService.deleteGroup(g.getId());
			}
		}

		Group newGroup = identityService.newGroup("unistacks");
		newGroup.setName("Unistacks");
		identityService.saveGroup(newGroup);

		groups = identityService.createGroupQuery().list();
		assert (groups.size() > 0);
		boolean found = false;
		for (Group g : groups) {
			if ("unistacks".equalsIgnoreCase(g.getId())) {
				found = true;
				break;
			}
		}
		assert (found);

	}

	@Test
	public void testAddMembership() {
		identityService.deleteMembership("Cliff", "unistacks");
		identityService.createMembership("Cliff Ma", "unistacks");
	}

	@Deployment(resources = { "diagrams/bookorder.bpmn" })
	@Test
	public void testMembership() {

		testAddUser();
		testAddGroup();
		testAddMembership();

		identityService.setAuthenticatedUserId("Cliff Ma");

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);

		Task task = taskService.createTaskQuery().taskCandidateUser("Cliff Ma")
				.singleResult();
		assertNotNull(task);
		assertEquals("Work on Order", task.getName());
	}

	private String startAndComplete() {

		repositoryService.createDeployment()
				.addClasspathResource("diagrams/bookorder.bpmn").deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		String processInstanceID = runtimeService.startProcessInstanceByKey(
				"bookorder", variableMap).getId();

		Task task = taskService.createTaskQuery().taskCandidateGroup("sales")
				.singleResult();
		variableMap = new HashMap<String, Object>();
		variableMap.put("extraInfo", "Extra information");
		variableMap.put("isbn", "654321");
		taskService.complete(task.getId(), variableMap);
		return processInstanceID;
	}

	@Test
	public void queryHistoricInstances() {

		String processInstanceID = startAndComplete();

		HistoricProcessInstance historicProcessInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceID).singleResult();
		assertNotNull(historicProcessInstance);
		assertEquals(processInstanceID, historicProcessInstance.getId());

		System.out.println("history process with definition id "
				+ historicProcessInstance.getProcessDefinitionId()
				+ ", started at " + historicProcessInstance.getStartTime()
				+ ", \nended at " + historicProcessInstance.getEndTime()
				+ ", \nduration was "
				+ historicProcessInstance.getDurationInMillis());
	}

	@Test
	public void queryHistoricActivities() {
		int oldSize = historyService.createHistoricActivityInstanceQuery()
				.list().size();

		startAndComplete();
		List<HistoricActivityInstance> activityList = historyService
				.createHistoricActivityInstanceQuery().list();
		assertEquals(4 + oldSize, activityList.size());
		for (HistoricActivityInstance historicActivityInstance : activityList) {
			assertNotNull(historicActivityInstance.getActivityId());
			System.out.println("history activity " + historicActivityInstance);
		}
	}

	@Test
	public void queryHistoricVariableUpdates() {
		int oldSize = historyService.createHistoricDetailQuery()
				.variableUpdates().list().size();

		String processInstacneId = startAndComplete();

		List<HistoricDetail> historicVariableUpdateList = historyService
				.createHistoricDetailQuery().variableUpdates().list();
		assertNotNull(historicVariableUpdateList);

		assertEquals(3 + oldSize, historicVariableUpdateList.size());

		for (HistoricDetail historicDetail : historicVariableUpdateList) {
			if (historicDetail.getProcessInstanceId().equals(processInstacneId)) {
				assertTrue(historicDetail instanceof HistoricVariableUpdate);
				HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) historicDetail;
				assertNotNull(historicVariableUpdate.getExecutionId());
				System.out.println("historic variable update,revision "
						+ historicVariableUpdate.getRevision()
						+ ", variable type name "
						+ historicVariableUpdate.getVariableTypeName()
						+ ", variable name "
						+ historicVariableUpdate.getVariableName()
						+ ", Variable value '"
						+ historicVariableUpdate.getValue() + "'");
			}
		}
	}
}