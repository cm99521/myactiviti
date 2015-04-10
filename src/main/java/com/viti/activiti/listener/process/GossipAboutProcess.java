package com.viti.activiti.listener.process;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.viti.activiti.util.EventUtil;

@SuppressWarnings("serial")
public class GossipAboutProcess implements ExecutionListener{

	public void notify(DelegateExecution execution) throws Exception {

		System.out.println("Did you know the following process event occurred = " +execution.getEventName());
		EventUtil.addEvent(execution, "process");
	}

}
