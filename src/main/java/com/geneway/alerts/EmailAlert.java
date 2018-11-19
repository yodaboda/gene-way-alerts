package com.geneway.alerts;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.geneway.alerts.mechanism.AlertMechanism;
import com.geneway.alerts.message.AbstractEmailMessage;


public class EmailAlert extends AbstractAlert {
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * 
	 */
	private static final long serialVersionUID = -2307422999927307233L;

	@Inject
	public EmailAlert(@Named("emailAlertMechanism") AlertMechanism alertMechanism) {
		super(alertMechanism);
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
}
