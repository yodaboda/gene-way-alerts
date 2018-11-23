package com.geneway.alerts;

import javax.mail.MessagingException;

/**
 * The mechanism used for sending alerts to end users
 * @author Firas Swidan
 *
 */
public interface AlertMechanism {
	/**
	 * Send the alert.
	 * @throws MessagingException In case of communication issues.
	 */
	public void send() throws MessagingException;
}
