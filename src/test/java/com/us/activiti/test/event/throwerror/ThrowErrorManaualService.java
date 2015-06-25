package com.us.activiti.test.event.throwerror;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 代码方式抛出异常
 *
 * @author Cliff Ma
 */
public class ThrowErrorManaualService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if (execution.getVariable("pass") == null) {
            throw new BpmnError("AIA_ERROR_99");
        }
        System.out.println("!!!!!!!!!!!success");
    }

}
