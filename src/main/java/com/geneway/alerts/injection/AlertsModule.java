package com.geneway.alerts.injection;

import java.util.Properties;

import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.geneway.alerts.AlertLocalization;
import com.geneway.alerts.AlertMechanism;
import com.geneway.alerts.AlertMessage;
import com.geneway.alerts.AlertRecipient;
import com.geneway.alerts.impl.EmailAlertMechanism;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Guice Module for providing AlertMechanism. To use this module the following 
 * classes / interfaces need to be bound to an implementation:
 *	<ul> 
 *	<li> <code> AlertMessage </code> </li>
 *	<li> <code> AlertRecipient </code> </li>
 *	<li> <code> AlertLocalization </code> </li>
 *	<li> <code> @Named("phoneNumber") String</code> </li>
 *	<li> <code> Locale </code> </li>
 *  </ul>
 *  
 *  Then it is possible to inject <code> AlertMecanism </code> or 
 *  <code> @Named("SMSOverEmailAlertMechanism") AlertMechanism </code>
 * @author Firas Swidan
 *
 */
public class AlertsModule extends AbstractModule {

	/**
	 * The binding of this module are done in the provides methods below.
	 */
	@Override
	protected void configure() {
	}
	
	/**
	 * Provides an <code> AlertMechanism </code> for sending reminders through email
	 * @param session The <code> Session </code> used for sending the email
	 * @param mimeMessage The content of the email
	 * @return An instantiated <code> AlertMechaism </code>.
	 */
	@Provides
	public AlertMechanism provideEmailAlertMechanism(Session session, 
													MimeMessage mimeMessage){
		return new EmailAlertMechanism(session, mimeMessage);
	}

	/**
	 * Provides an <code> AlertMechanism </code> for sending reminders through SMS over
	 * email. This mechanism sends a specifically formated email to a specific address that
	 * is then forwarded to an SMS message.
	 * @param session The <code> Session </code> used for sending the email that is
	 *  then converted into an SMS
	 * @param mimeMessage The content of the email
	 * @return An instantiated <code> AlertMechaism </code> for sending SMSs.
	 */
	@Provides
	@Named("SMSOverEmailAlertMechanism")
	public AlertMechanism provideSMSOverEmailAlertMechanism(Session session,
															@Named("SMSOverEmailMime") MimeMessage mimeMessage){
		return new EmailAlertMechanism(session, mimeMessage);
	}
	
	/**
	 * Provides properties of the email
	 * @return An instantiated <code> Properties </code> with the required email settings.
	 */
	@Provides
	public Properties provideProperties(){
		Properties mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		return mailServerProperties;
	}
	
	/**
	 * Provides the <code> MimeMessage </code> content of the email that is
	 * to be forwarded to an SMS.
	 * @param getMailSession The <code> Session </code> used for sending the email
	 * @param body The body of the email
	 * @param phoneNumber The SMS phone number
	 * @return An instantiated <code> MimeMessage </code> associated with the 
	 * given <code> Session </code> and based on the given <code> body </code> and 
	 * <code> phoneNumber </code>
	 * @throws MessagingException In case the instantiation of the <code> MimeMessage </code>
	 * went wrong.
	 */
	@Provides
	@Named("SMSOverEmailMime")
	MimeMessage provideSMSOverEmailMimeMessage(Session getMailSession, 
										@Named("emailAlertMechanismBody") String body,
										@Named("phoneNumber") String phoneNumber) 
												throws MessagingException{
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(RecipientType.TO, new InternetAddress("sms.gene.way@gmail.com"));
		generateMailMessage.setSubject(phoneNumber);
		generateMailMessage.setText(body);

		return generateMailMessage;
	}
	
	/**
	 * Provides a <code> Session </code> based on the given properties.
	 * @param mailServerProperties The properties of the email <code> Session </code>
	 * @return A <code> Session </code> based on the given email <code> Properties </code>
	 */
	@Provides
	Session provideSession(Properties mailServerProperties){
		return Session.getDefaultInstance(mailServerProperties, null);
	}
	
	/**
	 * Provides a <code> MimeMessage </code> of the email reminder.
	 * @param getMailSession The <code> Session </code> used for sending the email
	 * @param recipient The address of the email recipient.
	 * @param subject The email subject
	 * @param body the email body
	 * @return An instantiated <code> MimeMessage </code> associated with the given
	 * <code> Session </code> based on the given recipient, subject, and body.
	 * @throws MessagingException In case the instantiation of the <code> MimeMessage </code>
	 * went wrong.
	 */
	@Provides
	MimeMessage provideMimeMessage(Session getMailSession, 
										@Named("emailAlertMechanismRecipient") String recipient,
										@Named("emailAlertMechanismSubject") String subject, 
										@Named("emailAlertMechanismBody") String body) 
												throws MessagingException{
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(RecipientType.TO, new InternetAddress(recipient));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setText(body);

		return generateMailMessage;
	}
	
	/**
	 * Provides the subject of the alert
	 * @param alertLocalization The form used for localizing the subject
	 * @param alertMessage The message containing the subject
	 * @return A localized subject
	 */
	@Provides
	@Named("emailAlertMechanismSubject")
	String provideSubject(AlertLocalization alertLocalization, 
								 AlertMessage alertMessage){
		return alertLocalization.localizeSubject(alertMessage.getSubject());
	}
	
	/**
	 * Provides the recipient address of the alert.
	 * @param alertRecipient The recipient of the alert.
	 * @return The recipient address to send the alert to.
	 */
	@Provides
	@Named("emailAlertMechanismRecipient")
	String provideRecipient(AlertRecipient alertRecipient){
		return alertRecipient.getRecipient();
	}
	
	/**
	 * Provides the body of the alert.
	 * @param alertLocalization The form used for localizing the body
	 * @param alertMessage The message containing the body
	 * @return A localized body
	 */
	@Provides
	@Named("emailAlertMechanismBody")
	String provideBody(AlertLocalization alertLocalization, 
							  AlertMessage alertMessage){
		return alertLocalization.localizeBody(alertMessage.getBody());		
	}
}
