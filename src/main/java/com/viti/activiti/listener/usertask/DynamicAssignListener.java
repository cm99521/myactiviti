package com.viti.activiti.listener.usertask;

import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.FixedValue;

public class DynamicAssignListener implements TaskListener {
	
	FixedValue p1;
	FixedValue p2;

	@Override
	public void notify(DelegateTask delegateTask) {

		System.out.println("enter dynamic assign listenr");
		
		
		System.out.println("p1="+delegateTask.getVariableLocal("p1"));
		System.out.println("p2="+delegateTask.getVariableLocal("p1"));
		Map<String, Object> vs = delegateTask.getVariables();

		for (String k : vs.keySet()) {
			System.out.println(k + "=" + vs.get(k));
		}
		delegateTask.addCandidateGroup("management");

	}

}
