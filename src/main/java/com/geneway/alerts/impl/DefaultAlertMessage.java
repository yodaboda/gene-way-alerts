package com.geneway.alerts.impl;

import com.geneway.alerts.AlertMessage;

/**
 * The default content of the alert message
 * @author Firas Swidan
 *
 */
public class DefaultAlertMessage implements AlertMessage {

	@Override
	public String getSubject() {
		return "subject";
	}

	@Override
	public String[] getBody() {
		return new String[]{"body"};
	}

}
