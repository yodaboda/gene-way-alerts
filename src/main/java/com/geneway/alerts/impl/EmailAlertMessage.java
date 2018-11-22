package com.geneway.alerts.impl;

import com.geneway.alerts.AlertMessage;

public class EmailAlertMessage implements AlertMessage {

	@Override
	public String getSubject() {
		return "subject";
	}

	@Override
	public String[] getBody() {
		return new String[]{"body"};
	}

}
