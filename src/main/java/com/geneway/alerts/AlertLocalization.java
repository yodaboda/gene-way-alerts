package com.geneway.alerts;

import java.util.Locale;

/**
 * Provides localization of message body and subjects.
 * @author Firas Swidan
 *
 */
public interface AlertLocalization {
	/**
	 * Localize the subject of the message
	 * @param subject The original message subject
	 * @return A localized version of the message subject
	 */
	public String localizeSubject(String subject);
	/**
	 * Localize the body of the message
	 * @param body The original message body
	 * @return The localized message body
	 */
	public String localizeBody(String... body);
	/**
	 * The current locale
	 * @return The locale to use for the localizations.
	 */
	public Locale getLocale();
}
