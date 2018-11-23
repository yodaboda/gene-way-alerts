package com.geneway.alerts.impl;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.geneway.alerts.AlertMechanism;

/**
 * Sends alerts through email.
 * @author Firas Swidan
 *
 */
public class EmailAlertMechanism implements AlertMechanism{

	private Transport transport;
	private MimeMessage generateMailMessage;
	private String senderEmailAddress;
	private String senderEmailPassword;
	
	/**
	 * Constructs an <code> EmailAlertMechanism </code> from the given <code> Transport </code>
	 * and <code> MimeMessage </code>
	 * @param session The email session
	 * @param mimeMessage The message content and specifications
	 */
	@Inject
	public EmailAlertMechanism(Transport transport, MimeMessage mimeMessage,
								String senderEmailAddress, String senderEmailPassword){
		this.transport = transport;
		this.generateMailMessage = mimeMessage;
		this.senderEmailAddress = senderEmailAddress;
		this.senderEmailPassword = senderEmailPassword;
	}

	/**
	 * Sends an email alert from a pre-defined email account
	 * based on the specification receives in the constructor.
	 * @throws A <code> MessagingException </code> in case the alert send was not successful.
	 */
	@Override
	public void send() throws MessagingException {
		transport.connect("smtp.gmail.com", senderEmailAddress, senderEmailPassword);
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();

	}

}
