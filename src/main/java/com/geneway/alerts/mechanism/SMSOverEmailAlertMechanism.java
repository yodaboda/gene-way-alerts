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
	
	private String phoneNumber;
	
	@Inject
	public SMSOverEmailAlertMechanism(@Named("emailAlertMessage") AlertMessage alertMessage,
			@Named("emailAlertRecipient") AlertRecipient alertRecipient,
			@Named("emailLocalizeAlert") AlertLocalization emailLocalizeAlert,
			@Named("smsOverEmailPhoneNumber") String phoneNumber) {
		super(alertMessage, alertRecipient, emailLocalizeAlert);
		this.phoneNumber = phoneNumber;
	}

	/**
	 * The subject is the phone number of the recipient
	 * @return The phone number of the recipient
	 */
	@Override
	protected String getSubject(){
		return phoneNumber;
	}
}
