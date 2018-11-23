package com.geneway.alerts.impl;

import com.geneway.alerts.AlertSender;

/**
 * Default alert sender implementation
 * @author Firas Swidan
 *
 */
public class DefaultEmailAlertSender implements AlertSender {

	public static final String PASSWORD = "r8B0iR7M";
	public static final String USER_NAME = "sms.gene.way";
	public static final String EMAIL_ADDRESS = USER_NAME + "@gmail.com";
	public static final String HOST = "smtp.gmail.com";
	@Override
	public String getUserName() {
		return USER_NAME;
	}

	@Override
	public String getPassword() {
		return PASSWORD;
	}

	@Override
	public String getHost() {
		return HOST;
	}

	@Override
	public String getEmail() {
		return EMAIL_ADDRESS;
	}

}
