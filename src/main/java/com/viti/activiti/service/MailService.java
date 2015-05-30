package com.viti.activiti.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;

public class MailService {

	String from = "gitlab@unistacks.com";
	String mailHost = "smtp.ym.163.com";
	String mailUser = "gitlab@unistacks.com";
	String mailPwd = "Welcome1!";
	int mailPort = 25;
	public void sendMail(DelegateExecution execution) {
		try {

			String subject = "Hello "
					+ execution.getVariable("name").toString();
			String body = "hello activiti";
			String receiver = execution.getVariable("email").toString();

			send(receiver, subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(String receiver, String subject, String body){
		try{
		
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		Session session = Session.getInstance(properties);
		session.setDebug(false);

		Transport tran = session.getTransport();
		tran.connect(mailHost, mailPort, mailUser, mailPwd);

		Message messgae = new MimeMessage(session);
		messgae.setFrom(new InternetAddress(from));
		messgae.setText(body);
		messgae.setSubject(subject);
		tran.sendMessage(messgae, new Address[] { new InternetAddress(
				receiver) });

		tran.close();
		System.out.println("sent mail to "+ receiver + ": " + subject);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	public void sendSuccessMail(DelegateExecution execution, String other) {
		try {

			System.out.println("this is activity " + other);

			String subject = "Hello "
					+ execution.getVariable("name").toString();

			String requestApproved =  execution.getVariable("requestApproved").toString();
			String approvedBy =  execution.getVariable("approvedBy").toString();
			String body = "your loan request is "
					+ ("true".equals(requestApproved)? "successfull" : "failed" + " by "+approvedBy);
			String receiver = execution.getVariable("emailAddress").toString();

			send(receiver, subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendFailureMail(DelegateExecution execution, String other) {
		try {
			
			System.out.println("this is activity " + other);
			
			String subject = "Hello "
					+ execution.getVariable("name").toString();
			
			String requestApproved =  execution.getVariable("requestApproved").toString();
			String approvedBy =  execution.getVariable("approvedBy").toString();
			String body = "your loan request is "
					+ ("true".equals(requestApproved)? "successfull" : "failed" + " by "+approvedBy);
			String receiver = execution.getVariable("emailAddress").toString();
			send(receiver, subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
