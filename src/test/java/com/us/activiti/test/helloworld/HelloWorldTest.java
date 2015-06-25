package com.us.activiti.test.helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import com.us.activiti.test.BaseJunitTest;

/**
 * HelloWorld test
 *
 * @author Cliff Ma
 */
public class HelloWorldTest extends BaseJunitTest {


	@Test
	@Deployment(resources = { "diagrams/helloworld/SayHelloToLeave.bpmn" })
	public void testHelloWorld() throws Exception {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		assertEquals("SayHelloToLeave", processDefinition.getKey());

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("applyUser", "employee1");
		variables.put("days", 3);

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("SayHelloToLeave", variables);
		assertNotNull(processInstance);
		System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());

		Task taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		assertNotNull(taskOfDeptLeader);
		assertEquals("领导审批", taskOfDeptLeader.getName());

		taskService.claim(taskOfDeptLeader.getId(), "leaderUser");
		variables = new HashMap<String, Object>();
		variables.put("approved", true);
		taskService.complete(taskOfDeptLeader.getId(), variables);

		taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		assertNull(taskOfDeptLeader);

		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		assertEquals(1, count);
	}

	
	@Test
	@Deployment(resources = { "diagrams/helloworld/SayHelloToLeave.bpmn" })
    public void testStartProcess() throws Exception {
        

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().singleResult();
        assertEquals("SayHelloToLeave", processDefinition.getKey());


        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyUser", "employee1");
        variables.put("days", 3);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "SayHelloToLeave", variables);
        assertNotNull(processInstance);
        System.out.println("pid=" + processInstance.getId() + ", pdid="
                + processInstance.getProcessDefinitionId());

        Task taskOfDeptLeader = taskService.createTaskQuery()
                .taskCandidateGroup("deptLeader").singleResult();
        assertNotNull(taskOfDeptLeader);
        assertEquals("领导审批", taskOfDeptLeader.getName());

        taskService.claim(taskOfDeptLeader.getId(), "leaderUser");
        variables = new HashMap<String, Object>();
        variables.put("approved", true);
        taskService.complete(taskOfDeptLeader.getId(), variables);

        taskOfDeptLeader = taskService.createTaskQuery()
                .taskCandidateGroup("deptLeader").singleResult();
        assertNull(taskOfDeptLeader);

        long count = historyService.createHistoricProcessInstanceQuery().finished()
                .count();
        assertEquals(1, count);
    }
	
}
