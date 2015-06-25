package com.us.activiti.test.identity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Ignore;
import org.junit.Test;

import com.us.activiti.test.BaseJunitTest;

/**
 * Identity test
 *
 * @author Cliff Ma
 */
public class IdentityTest extends BaseJunitTest {

	@Test
	@Deployment(resources = { "diagrams/deployment/candidateUserInUserTask.bpmn" })
	public void testCandidateUserInUser() throws Exception {

		// 添加用户jackchen
		User userJackChen = identityService.newUser("jackchen");
		userJackChen.setFirstName("Jack");
		userJackChen.setLastName("Chen");
		userJackChen.setEmail("jackchen@gmail.com");
		identityService.saveUser(userJackChen);

		// 添加用户henryyan
		User userHenryyan = identityService.newUser("henryyan");
		userHenryyan.setFirstName("Henry");
		userHenryyan.setLastName("Yan");
		userHenryyan.setEmail("yanhonglei@gmail.com");
		identityService.saveUser(userHenryyan);

		// 启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("candidateUserInUserTask");
		assertNotNull(processInstance);

		// jackchen作为候选人的任务
		Task jackchenTask = taskService.createTaskQuery().taskCandidateUser("jackchen").singleResult();
		assertNotNull(jackchenTask);

		// henryyan作为候选人的任务
		Task henryyanTask = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult();
		assertNotNull(henryyanTask);

		// jackchen签收任务
		taskService.claim(jackchenTask.getId(), "jackchen");

		// 再次查询用户henryyan是否拥有刚刚的候选任务
		henryyanTask = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult();
		assertNull(henryyanTask);
	}

	@Test
	public void testGroup() throws Exception {
		// 创建一个组对象
		Group group = identityService.newGroup("deptLeader");
		group.setName("部门领导");
		group.setType("assignment");

		// 保存组
		identityService.saveGroup(group);

		// 验证组是否已保存成功，首先需要创建组查询对象
		List<Group> groupList = identityService.createGroupQuery().groupId("deptLeader").list();
		assertEquals(1, groupList.size());

		// 删除组
		identityService.deleteGroup("deptLeader");

		// 验证是否删除成功
		groupList = identityService.createGroupQuery().groupId("deptLeader").list();
		assertEquals(0, groupList.size());
	}

	@Test
	public void testUser() throws Exception {
		// 创建一个用户
		User user = identityService.newUser("henryyan");
		user.setFirstName("Henry");
		user.setLastName("Yan");
		user.setEmail("yanhonglei@gmail.com");

		// 保存用户到数据库
		identityService.saveUser(user);

		// 验证用户是否保存成功
		User userInDb = identityService.createUserQuery().userId("henryyan").singleResult();
		assertNotNull(userInDb);

		// 删除用户
		identityService.deleteUser("henryyan");

		// 验证是否删除成功
		userInDb = identityService.createUserQuery().userId("henryyan").singleResult();
		assertNull(userInDb);
	}

	/**
	 * 组和用户关联关系API演示
	 */
	@Test
	public void testUserAndGroupMemership() throws Exception {
		// 创建并保存组对象
		Group group = identityService.newGroup("deptLeader");
		group.setName("部门领导");
		group.setType("assignment");
		identityService.saveGroup(group);

		// 创建并保存用户对象
		User user = identityService.newUser("henryyan");
		user.setFirstName("Henry");
		user.setLastName("Yan");
		user.setEmail("yanhonglei@gmail.com");
		identityService.saveUser(user);

		// 把用户henryyan加入到组deptLeader中
		identityService.createMembership("henryyan", "deptLeader");

		// 查询属于组deptLeader的用户
		User userInGroup = identityService.createUserQuery().memberOfGroup("deptLeader").singleResult();
		assertNotNull(userInGroup);
		assertEquals("henryyan", userInGroup.getId());

		// 查询henryyan所属组
		Group groupContainsHenryyan = identityService.createGroupQuery().groupMember("henryyan").singleResult();
		assertNotNull(groupContainsHenryyan);
		assertEquals("deptLeader", groupContainsHenryyan.getId());
	}
}
