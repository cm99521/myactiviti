package com.viti.activiti;

import org.activiti.engine.delegate.DelegateExecution;

public class OrderService {

	public void validate(DelegateExecution execution) {
		System.out.println("validate orde for isbn " + execution.getVariable("isbn"));
	}
}
