package com.geneway.alerts.mechanism;

import com.geneway.alerts.localization.AlertLocalization;
import com.geneway.alerts.message.AlertMessage;
import com.geneway.alerts.recipient.AlertRecipient;

abstract public class AbstractAlertMechanism implements AlertMechanism {
	private AlertMessage alertMessage;
	private AlertRecipient alertRecipient;
	private AlertLocalization localizeAlert;
	
	public AbstractAlertMechanism(AlertMessage alertMessage, AlertRecipient alertRecipient,
			AlertLocalization localizeAlert){
		this.alertMessage = alertMessage;
		this.alertRecipient = alertRecipient;
		this.localizeAlert = localizeAlert;
	}
	
	protected AlertMessage getAlertMessage(){
		return this.alertMessage;
	}
	
	protected AlertRecipient getAlertRecipient(){
		return this.alertRecipient;
	}

	public AlertLocalization getLocalizeAlert() {
		return localizeAlert;
	}
}
