package com.geneway.alerts.injection;

import java.util.Properties;

import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.geneway.alerts.AlertMechanism;
import com.geneway.alerts.AlertSpecification;
import com.geneway.alerts.AlertType;
import com.geneway.alerts.impl.EmailAlertMechanism;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Guice Module for providing AlertMechanism. This module depends on being provided
 * with an <code> AlertSpecification </code>.
 *  
 *  Then it is possible to inject <code> AlertMecanism </code>.
 * @author Firas Swidan
 *
 */

public class AlertsModule extends AbstractModule {

	static final String SMS_RECIPIENT_EMAIL_ADDRESS = "sms.gene.way@gmail.com";

	/**
	 * Configuring the bindings of the Alerts module
	 */
	@Override
	protected void configure() {
		requireBinding(AlertSpecification.class);
	}
	
	/**
	 * Provides an <code> AlertMechanism </code> for sending reminders through email
	 * @param transport The <code> Transport </code> used for sending the email
	 * @param mimeMessage The content of the email
	 * @param alertSpecification Details of the alert
	 * @return An instantiated <code> AlertMechaism </code>.
	 */
	@Provides
	public AlertMechanism provideAlertMechanism(Transport transport, 
													MimeMessage mimeMessage,
													AlertSpecification alertSpecification){
		return new EmailAlertMechanism(transport, mimeMessage, alertSpecification.getAlertSender());
	}

	
	@Provides
	protected Transport providesTransport(Session session) 
													throws MessagingException{
		return session.getTransport("smtp");
	}
	
	/**
	 * Provides properties of the email
	 * @return An instantiated <code> Properties </code> with the required email settings.
	 */
	@Provides
	protected Properties provideProperties(){
		Properties mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		return mailServerProperties;
	}
	
	
	/**
	 * Provides a <code> Session </code> based on the given properties.
	 * @param mailServerProperties The properties of the email <code> Session </code>
	 * @return A <code> Session </code> based on the given email <code> Properties </code>
	 */
	@Provides
	protected Session provideSession(Properties mailServerProperties){
		return Session.getDefaultInstance(mailServerProperties, null);
	}
	
	/**
	 * Provides a <code> MimeMessage </code> of the email reminder.
	 * @param getMailSession The <code> Session </code> used for sending the email
	 * @param recipient The recipient of the email.
	 * @param subject The email subject.
	 * @param body the email body.
	 * @return An instantiated <code> MimeMessage </code> associated with the given
	 * <code> Session </code> based on the given alertSpecification, subject, and body.
	 * @throws MessagingException In case the instantiation of the <code> MimeMessage </code>
	 * went wrong.
	 */
	@Provides
	protected MimeMessage provideMimeMessage(Session getMailSession, 
											@Named("alertRecipient") String recipient,
											@Named("alertMechanismSubject") String subject, 
											@Named("alertMechanismBody") String body) 
												throws MessagingException{
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(RecipientType.TO, new InternetAddress(recipient));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setText(body);

		return generateMailMessage;
	}

	/**
	 * Provides the recipient of the alert.
	 * @param alertSpecification Details of the alert
	 * @return The recipient email is case of <code> AlertType.E_MAIl </code> alert, or
	 * 			<code> SMS_RECIPIENT_EMAIL_ADDRESS </code> in case of <code> AlertType.SMS </code>.
	 */
	@Provides
	@Named("alertRecipient")
	protected String provideAlertRecipient(AlertSpecification alertSpecification) {
		boolean emailAlert = alertSpecification.getAlertRecipient().getAlertType() == AlertType.E_MAIL;
		return emailAlert ? alertSpecification.getAlertRecipient().getRecipient() :
										SMS_RECIPIENT_EMAIL_ADDRESS;
		
	}
	/**
	 * Provides the subject of the alert
	 * @param alertSpecification Details of the alert
	 * @return A localized subject in case of <code> AlertType.E_MAIL </code> or the recipient phone
	 * 			number in case of <code> AlertType.SMS </code>.
	 */
	@Provides
	@Named("alertMechanismSubject")
	protected String provideSubject(AlertSpecification alertSpecification) {
		boolean emailAlert = alertSpecification.getAlertRecipient().getAlertType() == AlertType.E_MAIL;
		if(!emailAlert) {
			return alertSpecification.getAlertRecipient().getRecipient();
		}

		return alertSpecification.getAlertLocalization().localizeSubject(alertSpecification.getAlertMessage().getSubject());
	}
	
	/**
	 * Provides the body of the alert.
	 * @param alertSpecification Details of the alert
	 * @return A localized body
	 */
	@Provides
	@Named("alertMechanismBody")
	protected String provideBody(AlertSpecification alertSpecification){
		return alertSpecification.getAlertLocalization().localizeBody(alertSpecification.getAlertMessage().getBody());
	}
}
