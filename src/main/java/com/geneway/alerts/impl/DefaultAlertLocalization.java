package com.geneway.alerts.impl;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Default localization implementation that does nothing.
 * @author Firas Swidan
 *
 */
public class DefaultAlertLocalization extends AbstractAlertLocalization {

	public DefaultAlertLocalization(Locale locale) {
		super(locale);
	}

	@Override
	public String localizeSubject(String subject) {
		return subject;
	}

	@Override
	public String localizeBody(String... body) {
		return body[0];
	}

}
