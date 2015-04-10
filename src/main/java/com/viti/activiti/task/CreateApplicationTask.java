package com.viti.activiti.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.viti.activiti.LoanApplication;

public class CreateApplicationTask implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		LoanApplication la = new LoanApplication();
		la.setCreditCheckOK((Boolean) execution.getVariable("creditCheckOK"));
		la.setCustomerName((String) execution.getVariable("name"));
		la.setRequestedAmount((Long) execution.getVariable("loanAmount"));
		la.setIncome((Long) execution.getVariable("income"));
		la.setEmailAddress((String) execution.getVariable("emailAddress"));
		execution.setVariable("loanApplication"	, la);
		
	}

}
