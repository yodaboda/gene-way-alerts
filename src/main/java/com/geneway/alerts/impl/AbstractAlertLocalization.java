package com.geneway.alerts.impl;

import java.util.Locale;

import com.geneway.alerts.AlertLocalization;

/**
 * Implements the <code> AlertLocalization </code> interface with basic getters / setters
 * methods for the <code> Locale </code>
 * @author Firas Swidan
 *
 */
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
