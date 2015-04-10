package com.viti.activiti.listener.activity;

import org.activiti.engine.delegate.DelegateExecution;

import com.viti.activiti.util.EventUtil;

public class GossipAboutActivity {

	public void gossipStart(DelegateExecution execution) {
		System.out.println("Oh my the following event took place = " + execution.getEventName());
		EventUtil.addEvent(execution, "activity");
	}

	public void gossipEnd(DelegateExecution execution) {
		System.out.println("I can gossip about process variables and execution id = " + execution.getId());
		EventUtil.addEvent(execution, "activity");
	}
}
