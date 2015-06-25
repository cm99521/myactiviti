package com.us.activiti.test.event.intermediateevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import com.us.activiti.test.BaseJunitTest;

/**
 * Event test
 * 
 * @author Cliff Ma
 */
public class EventTest extends BaseJunitTest {

	@Test
	@Deployment(resources = "diagrams/event/intermediateEvent/noneIntermediateEvent.bpmn")
	public void testNoneIntermediateEventStart() throws Exception {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("noneIntermediateEvent");
		assertNotNull(processInstance);
		
		assertEquals(1, historyService.createHistoricProcessInstanceQuery().finished().count());
	}
}
