package com.geneway.alerts.message;

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
