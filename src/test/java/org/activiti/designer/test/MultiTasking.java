package org.activiti.designer.test;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class MultiTasking{

	Logger log = LoggerFactory.getLogger(MultiTasking.class);
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

	@BeforeClass
	public static void routeLoggingToSlf4j() {
		
	}

	@Test
	@Deployment(resources = { "diagrams/multitask.bpmn" })
	public void executeJavaService() throws Exception {

	
	}

}