package com.geneway.alerts.impl;

import java.util.Locale;

import com.geneway.alerts.AlertLocalization;

public abstract class AbstractAlertLocalization implements AlertLocalization {
	private Locale locale;

	public AbstractAlertLocalization(Locale locale){
		this.setLocale(locale);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
