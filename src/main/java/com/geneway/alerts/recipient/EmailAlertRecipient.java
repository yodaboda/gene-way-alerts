package com.geneway.alerts.recipient;

public class EmailAlertRecipient extends AbstractAlertRecipient {

	private String email;
	
	public EmailAlertRecipient(String email) {
		this.email = email;
	}
	
	@Override
	public String getRecipient() {
		return this.email;
	}

}
