package com.geneway.alerts;

import java.util.Locale;

/**
 * Provides localization of message body and subjects.
 * @author Firas Swidan
 *
 */
public interface AlertLocalization {
	public String localizeSubject(String subject);
	public String localizeBody(String... body);
	public Locale getLocale();
}
