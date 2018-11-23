package com.geneway.alerts;

/**
 * The alert message content
 * @author Firas Swidan
 *
 */
public interface AlertMessage {
	/**
	 * Gets the subject of the alert
	 * @return The message subject
	 */
	public String getSubject();
	/**
	 * Gets the body of the alert. The body could be composed of different parts for 
	 * localization purposes.
	 * @see AlertLocalization
	 * @return The message body.
	 */
	public String[] getBody();
}
