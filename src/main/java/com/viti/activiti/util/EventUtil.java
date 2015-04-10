package com.viti.activiti.util;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

public class EventUtil {

	public static void addEvent(DelegateExecution execution, String source) {
		@SuppressWarnings("unchecked")
		List<String> eventList = (List<String>) execution.getVariable("eventList");
		if (eventList == null) {
			eventList = new ArrayList<String>();
		}
		eventList.add(source + ":" + execution.getEventName());
		execution.setVariable("eventList", eventList);
	}

	public static void addEvent(DelegateTask task, String event) {
		@SuppressWarnings("unchecked")
		List<String> eventList = (List<String>) task.getExecution().getVariable("eventList");
		if (eventList == null) {
			eventList = new ArrayList<String>();
		}
		eventList.add("usertask:" + event);
		task.getExecution().setVariable("eventList", eventList);
	}
}
