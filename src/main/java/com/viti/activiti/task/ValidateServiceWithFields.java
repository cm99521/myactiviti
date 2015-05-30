package com.viti.activiti.task;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class ValidateServiceWithFields implements JavaDelegate {

	private Expression validatetext;
	private Expression isbn;

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("execution id " + execution.getId());
		System.out.println("received isbn " + (Long) isbn.getValue(execution));
		execution.setVariable("validatetime", new Date());
		System.out.println(validatetext.getValue(execution).toString()
				+ execution.getVariable("validatetime"));

	}

}
