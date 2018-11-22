package com.geneway.alerts;

public interface AlertLocalization {
	public String localizeSubject(String subject);
	public String localizeBody(String... body);
}
