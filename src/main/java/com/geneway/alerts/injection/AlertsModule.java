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
	 * The binding of this module are done in the provides methods.
	 */
	@Override
	protected void configure() {
	}
	
	@Provides
	public AlertMechanism provideEmailAlertMechanism(Session session, 
															MimeMessage mimeMessage){
		return new EmailAlertMechanism(session, mimeMessage);
	}
	
	@Provides
	@Named("SMSOverEmailAlertMechanism")
	public AlertMechanism provideSMSOverEmailAlertMechanism(Session session,
															@Named("SMSOverEmailMime") MimeMessage mimeMessage){
		return new EmailAlertMechanism(session, mimeMessage);
	}
	
	@Provides
	public Properties provideProperties(){
		Properties mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		return mailServerProperties;
	}
	
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
	
	@Provides
	Session provideSession(Properties mailServerProperties){
		return Session.getDefaultInstance(mailServerProperties, null);
	}
	
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
	
	@Provides
	@Named("emailAlertMechanismSubject")
	String provideSubject(AlertLocalization alertLocalization, 
								 AlertMessage alertMessage){
		return alertLocalization.localizeSubject(alertMessage.getSubject());
	}
	
	@Provides
	@Named("emailAlertMechanismRecipient")
	String provideRecipient(AlertRecipient alertRecipient){
		return alertRecipient.getRecipient();
	}
	
	@Provides
	@Named("emailAlertMechanismBody")
	String provideBody(AlertLocalization alertLocalization, 
							  AlertMessage alertMessage){
		return alertLocalization.localizeBody(alertMessage.getBody());		
	}
}
