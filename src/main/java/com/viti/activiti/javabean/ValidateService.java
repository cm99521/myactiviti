package com.viti.activiti.javabean;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class ValidateService implements JavaDelegate {
	
	private Expression validatetext;
	private Expression isbb;

	public void execute(DelegateExecution execution) throws Exception {

		System.out.println("execution id "+execution.getId());
		System.out.println("received validatetext  " + validatetext.getValue(execution));
		System.out.println("received isbb " + isbb.getValue(execution));
		
		
		
		Long isbn = (Long) execution.getVariable("isbn");
		System.out.println("received isbn "+isbn);
		execution.setVariable("validatetime", new Date());
	}

}
