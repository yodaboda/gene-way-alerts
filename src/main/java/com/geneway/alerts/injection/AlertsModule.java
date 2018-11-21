package com.geneway.alerts.injection;

import java.util.Locale;
import java.util.Properties;

import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.geneway.alerts.localization.AlertLocalization;
import com.geneway.alerts.localization.DefaultAlertLocalization;
import com.geneway.alerts.mechanism.AlertMechanism;
import com.geneway.alerts.mechanism.EmailAlertMechanism;
import com.geneway.alerts.message.AlertMessage;
import com.geneway.alerts.message.EmailAlertMessage;
import com.geneway.alerts.recipient.AlertRecipient;
import com.geneway.alerts.recipient.EmailAlertRecipient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class AlertsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AlertMechanism.class).annotatedWith(Names.named("emailAlertMechanism")).to(EmailAlertMechanism.class);
		bind(AlertMessage.class).annotatedWith(Names.named("emailAlertMessage")).to(EmailAlertMessage.class);
		bind(AlertRecipient.class).annotatedWith(Names.named("emailAlertRecipient")).to(EmailAlertRecipient.class);
		bind(AlertLocalization.class).annotatedWith(Names.named("emailAlertLocalization")).to(DefaultAlertLocalization.class);
	}
	
	@Provides
	public Locale provideLocale(){
		return Locale.ENGLISH;
	}
	
	@Provides
	@Named("EmailAlertMechanism")
	public EmailAlertMechanism provideEmailAlertMechanism(Session session, 
															MimeMessage mimeMessage){
		return new EmailAlertMechanism(session, mimeMessage);
	}
	
	@Provides
	@Named("SMSOverEmailAlertMechanism")
	public EmailAlertMechanism provideSMSOverEmailAlertMechanism(Session session,
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
	public MimeMessage provideSMSOverEmailMimeMessage(Session getMailSession, 
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
	public Session provideSession(Properties mailServerProperties){
		return Session.getDefaultInstance(mailServerProperties, null);
	}
	
	@Provides
	public MimeMessage provideMimeMessage(Session getMailSession, 
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
	public String provideSubject(@Named("emailAlertMechanism") AlertLocalization alertLocalization, 
									@Named("emailAlertMessage") AlertMessage alertMessage){
		return alertLocalization.localizeSubject(alertMessage.getSubject());
	}
	
	@Provides
	@Named("emailAlertMechanismRecipient")
	public String provideRecipient(@Named("emailAlertRecipient") AlertRecipient alertRecipient){
		return alertRecipient.getRecipient();
	}
	
	@Provides
	@Named("emailAlertMechanismBody")
	public String provideBody(@Named("emailAlertLocalization") AlertLocalization alertLocalization, 
								@Named("emailAlertMessage") AlertMessage alertMessage){
		return alertLocalization.localizeBody(alertMessage.getBody());		
	}
}
