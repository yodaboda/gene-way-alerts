package com.geneway.alerts.impl;

import javax.inject.Inject;

import com.geneway.alerts.AlertRecipient;

/**
 * Default implementation for the alert recipient.
 * @author Firas Swidan
 *
 */
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
