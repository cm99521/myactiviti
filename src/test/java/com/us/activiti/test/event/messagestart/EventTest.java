package com.us.activiti.test.event.messagestart;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.impl.EventSubscriptionQueryImpl;
import org.activiti.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import com.us.activiti.test.BaseJunitTest;

/**
 * Event test
 * @author Cliff Ma
 */
public class EventTest extends BaseJunitTest {

	 /**
     * 通过消息名称启动
     */
    @Deployment(resources = "diagrams/event/messageEvent/messageStartEvent.bpmn")
    @Test
    public void testStartMessageEvent() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("启动XXX流程");
        assertNotNull(processInstance);
    }

    /**
     * 通过流程ID启动
     */
    @Test
    @Deployment(resources = "diagrams/event/messageEvent/messageStartEvent.bpmn")
    public void testStartMessageEventByKey() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("messageStartEvent");
        assertNotNull(processInstance);
    }

    /**
     * 启动前检查对应的消息是否已注册到引擎
     */
    @Test
    @Deployment(resources = "diagrams/event/messageEvent/messageStartEvent.bpmn")
    public void testMessageSubcription() throws Exception {
        EventSubscriptionQueryImpl eventSubscriptionQuery = new EventSubscriptionQueryImpl(processEngineConfiguration.getCommandExecutor());
        EventSubscriptionEntity subscriptionEntity = eventSubscriptionQuery.eventName("启动XXX流程").singleResult();
        assertNotNull(subscriptionEntity);
        
        TablePage listPage = managementService.createTablePageQuery().tableName("ACT_RU_EVENT_SUBSCR").listPage(0, 10);
		List<Map<String, Object>> rows = listPage.getRows();
		for (Map<String, Object> map : rows) {
			Set<Entry<String, Object>> entrySet = map.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				System.out.println("!!!!!" + entry.getKey() + " = " + entry.getValue());
			}
		}
    }
    
    @Test
    @Deployment(resources = "diagrams/event/intermediateEvent/noneIntermediateEvent.bpmn")
    public void testNoneIntermediateEventStart() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("noneIntermediateEvent");
        assertNotNull(processInstance);
    }
}
