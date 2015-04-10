package com.viti.activiti.listener.usertask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.viti.activiti.util.EventUtil;

public class GossipAboutUserTask{

	public void gossipTask (DelegateTask task, String eventName){
		if(TaskListener.EVENTNAME_CREATE.equals(eventName)){
			System.out.println("Drink user task is created and assigned to John");
			task.setAssignee("John");
		}else if(TaskListener.EVENTNAME_ASSIGNMENT.equals(eventName)){
			System.out.println("Drink user task is assigned to " + task.getAssignee());
		}
		EventUtil.addEvent(task, eventName);
	}
}
