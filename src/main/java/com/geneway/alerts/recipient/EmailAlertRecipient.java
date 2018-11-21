package com.geneway.alerts.recipient;

import javax.inject.Inject;

public class EmailAlertRecipient implements AlertRecipient {

	private String email;
	
	@Inject
	public EmailAlertRecipient(String email) {
		this.email = email;
	}
	
	@Override
	public String getRecipient() {
		return this.email;
	}

}
