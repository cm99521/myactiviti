package com.viti.activiti.listener.usertask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class EvaluationListener implements ExecutionListener{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8738275120106592382L;


	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("now is exectuon "+execution);
		System.out.println("listenr is trigger");
	}
}
