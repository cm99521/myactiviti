package com.viti.activiti.listener.transition;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

import com.viti.activiti.util.EventUtil;

public class GossipAboutTransition {

	public void gossip(ExecutionListenerExecution execution) {
		PvmTransition transition = (PvmTransition) execution.getEventSource();
		System.out.println("Did you here " + transition.getSource().getId() + " transitioned to " + transition.getDestination().getId());
		EventUtil.addEvent(execution, "transition");
	}
}
