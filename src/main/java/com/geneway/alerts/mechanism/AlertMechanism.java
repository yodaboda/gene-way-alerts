package com.geneway.alerts.mechanism;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface AlertMechanism {
	public void send() throws AddressException, MessagingException;
}
