package com.us.activiti.test;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 抽象测试基类
 *
 * @author Cliff Ma
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans-jpa.xml")
public abstract class AbstractTest {

	@Autowired
	@Rule
	public ActivitiRule activitiRule;
	
	
	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected RepositoryService repositoryService;
	
	@Autowired
	protected FormService formService;

	@Autowired
	protected TaskService taskService;

	

	@Autowired
	protected HistoryService historyService;

	@Autowired
	protected IdentityService identityService;

	/**
	 * 开始测试
	 */
	@BeforeClass
	public static void setUpForClass() throws Exception {
		System.out.println("++++++++ 开始测试 ++++++++");
	}

	/**
	 * 结束测试
	 */
	@AfterClass
	public static void testOverForClass() throws Exception {
		System.out.println("-------- 结束测试 --------");
	}

}
