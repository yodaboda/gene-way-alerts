package com.geneway.alerts;

import java.util.Locale;

public interface AlertLocalization {
	public String localizeSubject(String subject);
	public String localizeBody(String... body);
	public Locale getLocale();
}
