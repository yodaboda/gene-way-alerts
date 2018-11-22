package com.geneway.alerts.impl;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.geneway.alerts.AlertMechanism;

public class EmailAlertMechanism implements AlertMechanism{

	private Session getMailSession;
	private MimeMessage generateMailMessage;

	public EmailAlertMechanism(Session session, MimeMessage mimeMessage){
		this.getMailSession = session;
		this.generateMailMessage = mimeMessage;
	}

	@Override
	public void send() throws MessagingException {
		Transport transport = getMailSession.getTransport("smtp");

		transport.connect("smtp.gmail.com", "sms.gene.way@gmail.com",
				"r8B0iR7M");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();

	}

}
