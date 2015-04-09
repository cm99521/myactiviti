package org.activiti.designer.test;

import static org.junit.Assert.*;

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
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.viti.activiti.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-jpa-beans.xml")
public class JPATest{

	@PersistenceContext
	private EntityManager entityManager;
	
	
	Logger log = LoggerFactory.getLogger(JPATest.class);
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
	@Deployment(resources = { "diagrams/jpatest.bpmn" })
	public void executeJavaService() throws Exception {
		Map<String, Object> processVars = new HashMap<String, Object>();
		List<String> authorList = new ArrayList<String>();
		authorList.add("Cliff");
		authorList.add("Bill");
		
		processVars.put("authorList", authorList);
		
		runtimeService.startProcessInstanceByKey("jpaTask", processVars);
		
		Task task = taskService.createTaskQuery().singleResult();
		
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