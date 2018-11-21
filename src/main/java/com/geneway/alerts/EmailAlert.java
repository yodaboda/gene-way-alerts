package com.geneway.alerts;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.geneway.alerts.mechanism.AlertMechanism;


public class EmailAlert implements Alert{
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * 
	 */
	private static final long serialVersionUID = -2307422999927307233L;

	private AlertMechanism alertMechanism;
	
	@Inject
	public EmailAlert(@Named("emailAlertMechanism") AlertMechanism alertMechanism) {
		this.setAlertMechanism(alertMechanism);
	}

	@Override
	public void remind() {
		try {
			this.getAlertMechanism().send();
		} catch (AddressException e) {
			LOGGER.log(Level.FATAL, e.toString(), e);
		} catch (MessagingException e) {
			LOGGER.log(Level.FATAL, e.toString(), e);
		}
	}

	private AlertMechanism getAlertMechanism() {
		return alertMechanism;
	}

	private void setAlertMechanism(AlertMechanism alertMechanism) {
		this.alertMechanism = alertMechanism;
	}
}
