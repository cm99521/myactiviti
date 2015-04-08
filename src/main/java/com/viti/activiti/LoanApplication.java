package com.viti.activiti;

import java.io.Serializable;

public class LoanApplication implements Serializable{

	private static final long serialVersionUID = 6264082823564459533L;
	private Boolean creditCheckOK;
	private String customerName;
	private Long income;
	private Long requestedAmount;
	private String emailAddress;
	public Boolean getCreditCheckOK() {
		return creditCheckOK;
	}
	public void setCreditCheckOK(Boolean creditCheckOK) {
		this.creditCheckOK = creditCheckOK;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getIncome() {
		return income;
	}
	public void setIncome(Long income) {
		this.income = income;
	}
	public Long getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(Long requestedAmount) {
		this.requestedAmount = requestedAmount;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
