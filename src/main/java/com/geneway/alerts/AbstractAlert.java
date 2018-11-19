package com.geneway.alerts;

import com.geneway.alerts.mechanism.AlertMechanism;

abstract public class AbstractAlert implements UserAlert {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8382739679682102881L;

	private AlertMechanism alertMechanism;
	
	public AbstractAlert(AlertMechanism alertMechanism){
		this.alertMechanism = alertMechanism;
	}

	public AlertMechanism getAlertMechanism(){
		return alertMechanism;
	}
	
	@Override
	public void cancel(){
		alertMechanism = null;
	}
}
