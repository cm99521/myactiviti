package com.us.activiti.test.deployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.identity.User;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Ignore;
import org.junit.Test;

import com.us.activiti.test.BaseJunitTest;

/**
 * all test cases
 *
 * @author Cliff Ma
 */
public class DeploymentTest extends BaseJunitTest {



	@Test
	public void testClasspathDeployment() throws Exception {

		String bpmnClasspath = "diagrams/deployment/candidateUserInUserTask.bpmn";
		String pngClasspath = "diagrams/deployment/candidateUserInUserTask.png";

		// 创建部署构建器
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

		// 添加资源
		deploymentBuilder.addClasspathResource(bpmnClasspath);
		deploymentBuilder.addClasspathResource(pngClasspath);

		// 执行部署
		deploymentBuilder.deploy();

		// 验证流程定义是否部署成功
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		long count = processDefinitionQuery.processDefinitionKey("candidateUserInUserTask").count();
		assertEquals(1, count);

		// 读取图片文件
		ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
		String diagramResourceName = processDefinition.getDiagramResourceName();
		assertEquals(pngClasspath, diagramResourceName);

		Map<String, Object> vars = new HashMap<String, Object>();
		ArrayList<Date> objs = new ArrayList<Date>();
		objs.add(new Date());
		vars.put("list", objs);

		runtimeService.startProcessInstanceByKey("candidateUserInUserTask", vars);

		List<Task> list = taskService.createTaskQuery().includeProcessVariables().list();
		System.out.println("list=" + list);

		Task task = taskService.createTaskQuery().taskId(list.get(0).getId()).includeProcessVariables().includeTaskLocalVariables().singleResult();
		CommandContext commandContext = Context.getCommandContext();
		System.out.println("task=" + task);
		System.out.println("commandContext=" + commandContext);

		System.out.println("variables=" + task.getProcessVariables());
	}

	@Test
	public void testInputStreamFromAbsoluteFilePath() throws Exception {

		String filePath = "/Users/cliff/git/myactiviti/src/main/resources/diagrams/deployment/userAndGroupInUserTask.bpmn";

		FileInputStream fileInputStream = new FileInputStream(filePath);
		repositoryService.createDeployment().addInputStream("userAndGroupInUserTask.bpmn", fileInputStream).deploy();

		// 验证是否部署成功
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		long count = pdq.processDefinitionKey("userAndGroupInUserTask").count();
		assertEquals(1, count);
	}

	@Test
	public void testReadXmlResourceTest() throws Exception {

		// 定义classpath
		String bpmnClasspath = "diagrams/deployment/candidateUserInUserTask.bpmn";

		// 添加资源
		repositoryService.createDeployment().addClasspathResource(bpmnClasspath).deploy();

		// 获取流程定义对象
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().singleResult();
		String resourceName = pd.getResourceName();
		System.out.println(String.format("资源名称：%s", resourceName));

		// 读取资源字节流
		InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

		// 输出流的内容
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = resourceAsStream.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		System.out.println("资源内容：" + out);
	}

	@Test
	public void testCharsDeployment() {

		// XML字符串
		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.kafeitu.me/activiti-in-action\">"
				+ "  <process id=\"candidateUserInUserTask\" name=\"candidateUserInUserTask\">" + "    <startEvent id=\"startevent1\" name=\"Start\"></startEvent>"
				+ "    <userTask id=\"usertask1\" name=\"用户任务包含多个直接候选人\" activiti:candidateUsers=\"jackchen, henryyan\"></userTask>"
				+ "    <sequenceFlow id=\"flow1\" name=\"\" sourceRef=\"startevent1\" targetRef=\"usertask1\"></sequenceFlow>" + "    <endEvent id=\"endevent1\" name=\"End\"></endEvent>"
				+ "    <sequenceFlow id=\"flow2\" name=\"\" sourceRef=\"usertask1\" targetRef=\"endevent1\"></sequenceFlow>" + "  </process>" + "</definitions>";

		// 以candidateUserInUserTask.bpmn为资源名称，以text的内容为资源内容部署到引擎
		repositoryService.createDeployment().addString("candidateUserInUserTask.bpmn", text).deploy();

		// 验证流程定义是否部署成功
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		long count = processDefinitionQuery.processDefinitionKey("candidateUserInUserTask").count();
		assertEquals(1, count);
	}

	@Test
	public void testZipStreamFromAbsoluteFilePath() throws Exception {
		// 从classpath读取资源并部署到引擎中
		InputStream zipStream = getClass().getClassLoader().getResourceAsStream("diagrams/deployment/chapter5-deployment.bar");
		repositoryService.createDeployment().addZipInputStream(new ZipInputStream(zipStream)).deploy();

		// 统计已部署流程定义的数量
		long count = repositoryService.createProcessDefinitionQuery().count();
		assertEquals(2, count);

		// 查询部署记录
		org.activiti.engine.repository.Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
		assertNotNull(deployment);
		String deploymentId = deployment.getId();

		// 验证4个资源文件是否都已成功部署
		assertNotNull(repositoryService.getResourceAsStream(deploymentId, "candidateUserInUserTask.bpmn"));
		assertNotNull(repositoryService.getResourceAsStream(deploymentId, "candidateUserInUserTask.png"));
		assertNotNull(repositoryService.getResourceAsStream(deploymentId, "userAndGroupInUserTask.bpmn"));
		assertNotNull(repositoryService.getResourceAsStream(deploymentId, "userAndGroupInUserTask.png"));
	}

	
}
