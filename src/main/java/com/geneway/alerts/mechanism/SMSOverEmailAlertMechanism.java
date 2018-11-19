package com.geneway.alerts.mechanism;

import javax.inject.Inject;
import javax.inject.Named;

import com.geneway.alerts.localization.AlertLocalization;
import com.geneway.alerts.message.AlertMessage;
import com.geneway.alerts.recipient.AlertRecipient;

/**
 * This class sends SMS to end users by first emailing the message to a pre-fixed email address.
 * @author firas1
 *
 */
public class SMSOverEmailAlertMechanism extends EmailAlertMechanism {
	@Inject
	public SMSOverEmailAlertMechanism(@Named("emailAlertMessage") AlertMessage alertMessage,
			@Named("emailAlertRecipient") AlertRecipient alertRecipient,
			@Named("emailLocalizeAlert") AlertLocalization emailLocalizeAlert) {
		super(alertMessage, alertRecipient, emailLocalizeAlert);
	}

	/**
	 * The subject is the phone number of the recipient
	 * @return The phone number of the recipient
	 */
	@Override
	protected String getSubject(){
		return this.getAlertRecipient().getRecipient();
	}
	
	/**
	 * The recipient is a fixed email from which the message are forwarded through SMS
	 * to the end-user.
	 * @return A fixed email address
	 */
	@Override
	protected String getRecipient(){
		return "sms.gene.way@gmail.com";
	}
}
