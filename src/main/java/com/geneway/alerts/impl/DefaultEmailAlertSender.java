package com.geneway.alerts.impl;

import com.geneway.alerts.AlertSender;

/**
 * Default alert sender implementation
 *
 * @author Firas Swidan
 */
public class DefaultEmailAlertSender implements AlertSender {

	//TODO: Password should not be saved in source code.
	//see: https://stackoverflow.com/questions/12937641/handling-passwords-used-for-auth-in-source-code
  public static final char[] PASSWORD = "r8B0iR7M".toCharArray();
  public static final String USER_NAME = "sms.gene.way";
  public static final String EMAIL_ADDRESS = USER_NAME + "@gmail.com";
  public static final String HOST = "smtp.gmail.com";

  @Override
  public String getUserName() {
    return USER_NAME;
  }

  @Override
  public char[] getPassword() {
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
