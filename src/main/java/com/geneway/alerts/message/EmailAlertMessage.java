package com.geneway.alerts.message;

public class EmailAlertMessage extends AbstractAlertMessage {

	@Override
	public String getSubject() {
		return "subject";
	}

	@Override
	public String getBody() {
		return "body";
	}

}
