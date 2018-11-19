package com.geneway.alerts;

import javax.inject.Inject;
import javax.inject.Named;

import com.geneway.alerts.mechanism.AlertMechanism;

public class SMSAlert extends EmailAlert {

	@Inject
	public SMSAlert(@Named("smsAlertMechanism") AlertMechanism alertMechanism) {
		super(alertMechanism);
	}


	
}
