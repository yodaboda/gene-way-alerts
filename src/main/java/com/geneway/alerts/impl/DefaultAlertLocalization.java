package com.geneway.alerts.impl;

import java.util.Locale;

import javax.inject.Inject;

public class DefaultAlertLocalization extends AbstractAlertLocalization {

	@Inject
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