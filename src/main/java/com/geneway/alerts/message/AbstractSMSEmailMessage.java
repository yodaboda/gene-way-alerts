package com.geneway.alerts.message;

public abstract class AbstractSMSEmailMessage extends AbstractEmailMessage {

	

	private String phoneNumber;
	
	public AbstractSMSEmailMessage(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	protected String getSubject() {
		return phoneNumber;
	}

	@Override
	protected String getRecipient() {
		return "sms.gene.way@gmail.com";
	}
}
