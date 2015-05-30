package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.viti.activiti.pojo.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans-jpa.xml")
public class JPABookTest{

	@PersistenceContext
	private EntityManager entityManager;
	
	Logger log = LoggerFactory.getLogger(JPABookTest.class);
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


	@Test
	public void executeJavaService() throws Exception {
		Deployment de = repositoryService.createDeployment()
				.addClasspathResource("diagrams/jpatest.bpmn").deploy();
		
		Map<String, Object> processVars = new HashMap<String, Object>();
		List<String> authorList = new ArrayList<String>();
		authorList.add("Cliff");
		authorList.add("Bill");
		
		processVars.put("authorList", authorList);
		
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("jpaTask", processVars);
		
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		
		Map<String, String> formProps = new HashMap<String, String>();
		formProps.put("bookTitle", "Action in Action");
		formProps.put("isbn", "123566");
		formService.submitTaskFormData(task.getId(), formProps);
		
		List theList = entityManager.createQuery("from Book b").getResultList();
		Book book = (Book) entityManager.createQuery("from Book b where b.title = ?").setParameter(1, "Action in Action").getSingleResult();
		
		assertNotNull(book);
		assertEquals("Action in Action", book.getTitle());
		assertEquals("Java in Action", book.getSubTitle());
		assertEquals(2, book.getAuthors().size());
		assertEquals("Cliff", book.getAuthors().get(0));
	
	}

}