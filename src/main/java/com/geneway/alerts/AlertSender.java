package com.geneway.alerts;

/**
 * Details of the alert sender.
 * @author Firas Swidan
 *
 */
public interface AlertSender {
	public String getUserName();
	public String getPassword();
	public String getHost();
	public String getEmail();
}
