package com.geneway.alerts.localization;

import java.util.Locale;

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
