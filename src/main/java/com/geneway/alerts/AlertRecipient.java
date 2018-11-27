package com.geneway.alerts;

/**
 * The alert recipient
 * @author Firas Swidan
 *
 */
public interface AlertRecipient {
	/**
	 * The recipient of the alert.
	 * @return The details of the alert recipient
	 */
	public String getRecipient();
	
	/**
	 * The type of the alert to be sent to the recipient.
	 * @return The alert type.
	 */
	public AlertType getAlertType();
}
