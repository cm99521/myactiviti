package com.viti.activiti.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CheckCredit implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		String customerNumber = (String) execution.getVariable("customerNumber");
		String contractType = (String) execution.getVariable("contractType");
		
		System.out.println("---customerNumber =" + customerNumber);
		System.out.println("---contractType =" + contractType);
		if("11".equalsIgnoreCase(customerNumber)){
			execution.setVariable("creditCheckApproved"	, true);	
		}else{
			execution.setVariable("creditCheckApproved"	, false);
		}
		
		System.out.println("done of service task CheckCredit");
		
		
	}

}
