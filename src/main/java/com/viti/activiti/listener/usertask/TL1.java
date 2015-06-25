package com.viti.activiti.listener.usertask;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class TL1 implements ExecutionListener{

	

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Map<String, Object> vs = execution.getVariables();
		for(String key:vs.keySet()){
			System.out.println("got "+key+":"+vs.get(key));
		}
	}

}
