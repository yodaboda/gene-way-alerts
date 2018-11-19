package com.geneway.alerts.mechanism;

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.geneway.alerts.localization.AlertLocalization;
import com.geneway.alerts.message.AlertMessage;
import com.geneway.alerts.recipient.AlertRecipient;

public class EmailAlertMechanism extends AbstractAlertMechanism{

	@Inject
	public EmailAlertMechanism(@Named("emailAlertMessage") AlertMessage alertMessage,
			@Named("emailAlertRecipient") AlertRecipient alertRecipient,
			@Named("emailLocalizeAlert") AlertLocalization emailLocalizeAlert) {
		super(alertMessage, alertRecipient, emailLocalizeAlert);
	}

	protected String getSubject(){
		return this.getLocalizeAlert().localizeSubject(this.getAlertMessage().getSubject());
	}
	
	protected String getRecipient(){
		return this.getLocalizeAlert().localizeBody(this.getAlertRecipient().getRecipient());
	}
	
	@Override
	public void send() throws AddressException, MessagingException {

		String subject = getSubject();
		String body = this.getAlertMessage().getBody();
		String recipient = getRecipient();
		//Step1		
		Properties mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		//Step2		
		Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(RecipientType.TO, new InternetAddress(recipient));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setText(body);
		
		//Step3		
		Transport transport = getMailSession.getTransport("smtp");

		transport.connect("smtp.gmail.com", "sms.gene.way@gmail.com", "r8B0iR7M");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}

}
