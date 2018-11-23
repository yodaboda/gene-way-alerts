package com.geneway.alerts.impl;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.geneway.alerts.AlertMechanism;
import com.geneway.alerts.AlertSender;

/**
 * Sends alerts through email.
 * @author Firas Swidan
 *
 */
public class EmailAlertMechanism implements AlertMechanism{

	private Transport transport;
	private MimeMessage generateMailMessage;
	private AlertSender emailAlertSender;
	
	/**
	 * Constructs an <code> EmailAlertMechanism </code> from the given <code> Transport </code>
	 * and <code> MimeMessage </code>
	 * @param session The email session
	 * @param mimeMessage The message content and specifications
	 */
	@Inject
	public EmailAlertMechanism(Transport transport, MimeMessage mimeMessage,
								AlertSender emailAlertSender){
		this.transport = transport;
		this.generateMailMessage = mimeMessage;
		this.emailAlertSender = emailAlertSender;
	}

	/**
	 * Sends an email alert from a pre-defined email account
	 * based on the specification receives in the constructor.
	 * @throws A <code> MessagingException </code> in case the alert send was not successful.
	 */
	@Override
	public void send() throws MessagingException {
		transport.connect(emailAlertSender.getHost(), emailAlertSender.getUserName(), 
							emailAlertSender.getPassword());
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();

	}

}
