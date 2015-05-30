package com.viti.activiti.task;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ValidateService implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("execution id " + execution.getId());
		Long isbn = (Long) execution.getVariable("isbn");
		System.out.println("received isbn " + isbn);
		execution.setVariable("validatetime", new Date());
		
		System.out.println("eh");

	}

}
