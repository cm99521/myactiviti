package com.us.activiti.test;

import java.util.Timer;
import java.util.TimerTask;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.test.ActivitiRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * base junit test 提供公用或被重复调用的代码 例如，dao/controller/service都需要实例化同一个类的实例
 * 
 * @author Cliff Ma
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class BaseJunitTest extends SpringTransactionalTestCase {

	@Autowired
	@Rule
	public ActivitiRule activitiRule;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected RepositoryService repositoryService;

	@Autowired
	protected FormService formService;

	@Autowired
	protected TaskService taskService;

	@Autowired
	protected HistoryService historyService;

	@Autowired
	protected IdentityService identityService;

	@Autowired
	protected ManagementService managementService;

	@Autowired
	protected ProcessEngineConfigurationImpl processEngineConfiguration;

	/**
	 * 开始测试
	 */
	@BeforeClass
	public static void setUpForClass() throws Exception {
		System.out.println("++++++++ 开始测试 ++++++++");
	}

	/**
	 * 结束测试
	 */
	@AfterClass
	public static void testOverForClass() throws Exception {
		System.out.println("-------- 结束测试 --------");
	}

	public boolean areJobsAvailable() {
		return !managementService.createJobQuery().executable().list().isEmpty();
	}

	private static class InteruptTask extends TimerTask {

		public boolean isTimeLimitExceeded() {
			return timeLimitExceeded;
		}

		public void run() {
			timeLimitExceeded = true;
			thread.interrupt();
		}

		protected boolean timeLimitExceeded;
		protected Thread thread;

		public InteruptTask(Thread thread) {
			timeLimitExceeded = false;
			this.thread = thread;
		}
	}

	public void waitForJobExecutorToProcessAllJobs(long maxMillisToWait, long intervalMillis) {
		JobExecutor jobExecutor;
		jobExecutor = processEngineConfiguration.getJobExecutor();
		jobExecutor.start();
		Timer timer;
		InteruptTask task;
		boolean areJobsAvailable;
		timer = new Timer();
		task = new InteruptTask(Thread.currentThread());
		timer.schedule(task, maxMillisToWait);
		areJobsAvailable = true;
		try {
			while (areJobsAvailable && !task.isTimeLimitExceeded()) {
				Thread.sleep(intervalMillis);
				try {
					areJobsAvailable = areJobsAvailable();
				} catch (Throwable t) {
				}
			}
		} catch (InterruptedException e) {
			timer.cancel();
		} finally {
			timer.cancel();
		}
		if (areJobsAvailable) {
			throw new ActivitiException("time limit of " + maxMillisToWait + " was exceeded");
		}

	}

}
