package com.geneway.alerts.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.geneway.alerts.AlertLocalization;
import com.geneway.alerts.AlertMechanism;
import com.geneway.alerts.AlertMessage;
import com.geneway.alerts.AlertRecipient;
import com.geneway.alerts.AlertSender;
import com.geneway.alerts.AlertSpecification;
import com.geneway.alerts.AlertType;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.inject.util.Modules;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

public class AlertsModuleTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final String RECIPIENT_EMAIL = "recipient@localhost.com";
	private final String RECIPIENT_PHONE = "24";

	private final String SUBJECT = "subject";
	private final String BODY = "body";

	private static final String LOCALHOST = "127.0.0.1";
	private static final String USER_NAME = "alertsUser";
	private static final String USER_EMAIL = USER_NAME + "@localhost";
	private final GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);

	@Bind
	@Mock
	AlertSpecification mockedAlertSpecification;

	@Mock
	AlertMessage mockedAlertMessage;
	@Mock
	AlertRecipient mockedAlertRecipient;
	@Mock
	AlertLocalization mockedAlertLocalization;
	@Mock
	AlertSender mockedAlertSender;

	@Inject
	AlertMechanism emailAlertMechanism;
	@Inject
	Properties properties;
	@Inject
	MimeMessage mimeMessage;
	@Inject
	Session session;
	@Inject
	@Named("alertMechanismSubject")
	String subject;
	@Inject
	@Named("alertMechanismBody")
	String body;
	@Inject
	Transport transport;
	@Inject
	@Named("alertRecipient")
	String recipient;

	public class TestAlertsModule extends AbstractModule {
		@Provides
		protected Properties provideProperties() {
			Properties mailSessionProperties = new Properties();
			mailSessionProperties.put("mail.smtp.host", LOCALHOST);
			mailSessionProperties.put("mail.smtp.auth", "true");
			mailSessionProperties.put("mail.smtp.port", ServerSetupTest.SMTP.getPort());
			return mailSessionProperties;
		}

		@Override
		protected void configure() {

		}
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		String[] bodyStrings = new String[] { BODY };

		when(mockedAlertSpecification.getAlertLocalization()).thenReturn(mockedAlertLocalization);
		when(mockedAlertSpecification.getAlertMessage()).thenReturn(mockedAlertMessage);
		when(mockedAlertSpecification.getAlertRecipient()).thenReturn(mockedAlertRecipient);
		when(mockedAlertSpecification.getAlertSender()).thenReturn(mockedAlertSender);

		when(mockedAlertRecipient.getAlertType()).thenReturn(AlertType.E_MAIL);
		when(mockedAlertRecipient.getRecipient()).thenReturn(RECIPIENT_EMAIL);
		when(mockedAlertMessage.getBody()).thenReturn(bodyStrings);
		when(mockedAlertMessage.getSubject()).thenReturn(SUBJECT);
		when(mockedAlertLocalization.localizeSubject(SUBJECT)).thenReturn("subject");
		when(mockedAlertLocalization.localizeBody(bodyStrings)).thenReturn("body");
		when(mockedAlertLocalization.getLocale()).thenReturn(Locale.forLanguageTag("ar"));
		doReturn(USER_NAME).when(mockedAlertSender).getUserName();
		doReturn("123456").when(mockedAlertSender).getPassword();
		doReturn(LOCALHOST).when(mockedAlertSender).getHost();
		doReturn(USER_EMAIL).when(mockedAlertSender).getEmail();

		mailServer.start();
		mailServer.setUser(mockedAlertSender.getHost(), mockedAlertSender.getUserName(),
				mockedAlertSender.getPassword());
	}

	@After
	public void tearDown() {
		mailServer.stop();
	}

	private void setUpInjection() {
		Guice.createInjector(Modules.override(new AlertsModule()).with(new TestAlertsModule()),
				BoundFieldModule.of(this)).injectMembers(this);
	}
	
	private void setUpEmailAlert() {
		when(mockedAlertRecipient.getAlertType()).thenReturn(AlertType.E_MAIL);
		when(mockedAlertRecipient.getRecipient()).thenReturn(RECIPIENT_EMAIL);

		setUpInjection();
	}

	private void setUpSMSAlert() {
		when(mockedAlertRecipient.getAlertType()).thenReturn(AlertType.SMS);
		when(mockedAlertRecipient.getRecipient()).thenReturn(RECIPIENT_PHONE);

		setUpInjection();
	}

	
	@Test
	public void testProvideEmailAlertMechanism() throws MessagingException, IOException {
		setUpEmailAlert();

		emailAlertMechanism.send();

		MimeMessage[] messages = mailServer.getReceivedMessages();
		assertNotNull(messages);
		assertEquals(1, messages.length);
		MimeMessage m = messages[0];
		assertEquals(subject, m.getSubject());
		assertTrue(String.valueOf(m.getContent()).contains(body));
		assertEquals(RECIPIENT_EMAIL, m.getAllRecipients()[0].toString());
	}

	@Test
	public void testProvideSMSOverEmailAlertMechanism() throws MessagingException, IOException {
		setUpSMSAlert();

		emailAlertMechanism.send();

		MimeMessage[] messages = mailServer.getReceivedMessages();
		assertNotNull(messages);
		assertEquals(1, messages.length);
		MimeMessage m = messages[0];
		assertEquals(RECIPIENT_PHONE, m.getSubject());
		assertTrue(String.valueOf(m.getContent()).contains(body));
		assertEquals(AlertsModule.SMS_RECIPIENT_EMAIL_ADDRESS, m.getAllRecipients()[0].toString());
	}


	@Test
	public void testProvidesTransport() throws MessagingException {
		setUpInjection();

		assertEquals("smtp:", transport.getURLName().toString());
	}

	@Test
	public void testProvideProperties() {
		setUpInjection();

		assertEquals(ServerSetupTest.SMTP.getPort(), properties.get("mail.smtp.port"));
		assertEquals("true", properties.get("mail.smtp.auth"));
		assertEquals(LOCALHOST, properties.get("mail.smtp.host"));
	}

	@Test
	public void testProvideSMSOverEmailMimeMessage() {
		setUpSMSAlert();

		try {
			Address address = new InternetAddress(AlertsModule.SMS_RECIPIENT_EMAIL_ADDRESS);
			assertEquals(address, mimeMessage.getRecipients(RecipientType.TO)[0]);
			assertEquals(RECIPIENT_PHONE, mimeMessage.getSubject());
		} catch (MessagingException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testProvideSession() {
		setUpInjection();

		assertEquals(properties, session.getProperties());
	}

	@Test
	public void testProvideMimeMessage() {
		setUpEmailAlert();

		try {
			Address address = new InternetAddress(RECIPIENT_EMAIL);
			assertEquals(address, mimeMessage.getRecipients(RecipientType.TO)[0]);
			assertEquals(SUBJECT, mimeMessage.getSubject());
		} catch (MessagingException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testProvideSubject() {
		setUpEmailAlert();

		assertEquals(SUBJECT, subject);
	}

	@Test
	public void testProvideSubjectSMS() {
		setUpSMSAlert();

		assertEquals(RECIPIENT_PHONE, subject);
	}

	
	@Test
	public void testProvideBody() {
		setUpInjection();

		assertEquals(BODY, body);
	}

	@Test
	public void testProvideAlertRecipient() {
		setUpEmailAlert();
		
		assertEquals(RECIPIENT_EMAIL, recipient);
	}

	@Test
	public void testProvideAlertRecipientSMS() {
		setUpSMSAlert();
		
		assertEquals(AlertsModule.SMS_RECIPIENT_EMAIL_ADDRESS, recipient);
	}

}
