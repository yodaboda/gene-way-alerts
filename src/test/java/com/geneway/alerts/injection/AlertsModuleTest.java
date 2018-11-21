package com.geneway.alerts.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.geneway.alerts.localization.AlertLocalization;
import com.geneway.alerts.mechanism.AlertMechanism;
import com.geneway.alerts.mechanism.EmailAlertMechanism;
import com.geneway.alerts.message.AlertMessage;
import com.geneway.alerts.recipient.AlertRecipient;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;

public class AlertsModuleTest {

	@Rule
    public ExpectedException thrown= ExpectedException.none();
	
	private final String RECIPIENT = "recipient";
	private final String SUBJECT = "subject";
	private final String BODY = "body";
	
	@Bind @Mock AlertMechanism mockedAlertMechanism;
	@Bind @Mock AlertMessage mockedAlertMessage;
	@Bind @Mock AlertRecipient mockedAlertRecipient;
	@Bind @Mock AlertLocalization mockedAlertLocalization;
	@Bind @Named("phoneNumber") String phoneNumber = "24";
	@Bind Locale locale = Locale.forLanguageTag("ar");
	
	@Inject EmailAlertMechanism emailAlertMechanism;
	@Inject @Named("SMSOverEmailAlertMechanism") EmailAlertMechanism smsOverEmailAlertMechanism;
	@Inject Properties properties;
	@Inject @Named("SMSOverEmailMime") MimeMessage smsOverEmailMime;
	@Inject MimeMessage mimeMessage;
	@Inject Session session;
	@Inject @Named("emailAlertMechanismSubject") String subject;
	@Inject @Named("emailAlertMechanismRecipient") String recipient;
	@Inject @Named("emailAlertMechanismBody") String body;
	
	 @Before 
	 public void setUp() {
		 MockitoAnnotations.initMocks(this);
		 String[] bodyStrings = new String[]{BODY};
		 when(mockedAlertRecipient.getRecipient()).thenReturn(RECIPIENT);
		 when(mockedAlertMessage.getBody()).thenReturn(bodyStrings);
		 when(mockedAlertMessage.getSubject()).thenReturn(SUBJECT);
		 when(mockedAlertLocalization.localizeSubject(SUBJECT)).thenReturn("subject");
		 when(mockedAlertLocalization.localizeBody(bodyStrings)).thenReturn("body");

		 Guice.createInjector(new AlertsModule(), 
				 				BoundFieldModule.of(this)).injectMembers(this);
	}
	 
	@Test
	public void testProvideEmailAlertMechanism() throws MessagingException {
		thrown.expect(MessagingException.class);
		emailAlertMechanism.send();
	}

	@Test
	public void testProvideSMSOverEmailAlertMechanism() throws MessagingException {
		thrown.expect(MessagingException.class);
		smsOverEmailAlertMechanism.send();
	}

	@Test
	public void testProvideProperties() {
		assertEquals("587", properties.get("mail.smtp.port"));
		assertEquals("true", properties.get("mail.smtp.auth"));
		assertEquals("true", properties.get("mail.smtp.starttls.enable"));
	}

	@Test
	public void testProvideSMSOverEmailMimeMessage() {
		try {
			Address address = new InternetAddress("sms.gene.way@gmail.com");
			assertEquals(address, smsOverEmailMime.getRecipients(RecipientType.TO)[0]);
			assertEquals(phoneNumber, smsOverEmailMime.getSubject());
		} catch (MessagingException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testProvideSession() {
		assertEquals(properties, session.getProperties());
	}

	@Test
	public void testProvideMimeMessage() {
		try {
			Address address = new InternetAddress(RECIPIENT);
			assertEquals(address, mimeMessage.getRecipients(RecipientType.TO)[0]);
			assertEquals(SUBJECT, mimeMessage.getSubject());
		} catch (MessagingException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testProvideSubject() {
		assertEquals(SUBJECT, subject);
	}

	@Test
	public void testProvideRecipient() {
		assertEquals(RECIPIENT, recipient);
	}

	@Test
	public void testProvideBody() {
		assertEquals(BODY, body);
	}

}
