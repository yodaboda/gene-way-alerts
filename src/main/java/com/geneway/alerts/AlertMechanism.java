package com.geneway.alerts;

import javax.mail.MessagingException;

/**
 * The mechanism used for sending alerts to end users
 * @author Firas Swidan
 *
 */
public interface AlertMechanism {
	public void send() throws MessagingException;
}
