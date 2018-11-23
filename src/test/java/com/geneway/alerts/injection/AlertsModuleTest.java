package com.geneway.alerts.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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
    public ExpectedException thrown= ExpectedException.none();
	
	private final String RECIPIENT = "recipient@localhost.com";
	private final String SUBJECT = "subject";
	private final String BODY = "body";

	private static final String LOCALHOST = "127.0.0.1";
	private static final String USER_NAME = "alertsUser";
	private static final String USER_EMAIL = USER_NAME + "@localhost";
	private final GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);

	
	@Bind @Mock AlertMessage mockedAlertMessage;
	@Bind @Mock AlertRecipient mockedAlertRecipient;
	@Bind @Mock AlertLocalization mockedAlertLocalization;
	@Bind @Named("phoneNumber") String phoneNumber = "24";
	@Bind Locale locale = Locale.forLanguageTag("ar");
	@Bind AlertSender mockedEmailAlertSender;
	
	@Inject AlertMechanism emailAlertMechanism;
	@Inject @Named("SMSOverEmailAlertMechanism") AlertMechanism smsOverEmailAlertMechanism;
	@Inject Properties properties;
	@Inject @Named("SMSOverEmailMime") MimeMessage smsOverEmailMime;
	@Inject MimeMessage mimeMessage;
	@Inject Session session;
	@Inject @Named("emailAlertMechanismSubject") String subject;
	@Inject @Named("emailAlertMechanismRecipient") String recipient;
	@Inject @Named("emailAlertMechanismBody") String body;
	@Inject Transport transport;
	
	public class TestAlertsModule extends AbstractModule{
		@Provides
		protected Properties provideProperties(){
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
		 String[] bodyStrings = new String[]{BODY};
		 when(mockedAlertRecipient.getRecipient()).thenReturn(RECIPIENT);
		 when(mockedAlertMessage.getBody()).thenReturn(bodyStrings);
		 when(mockedAlertMessage.getSubject()).thenReturn(SUBJECT);
		 when(mockedAlertLocalization.localizeSubject(SUBJECT)).thenReturn("subject");
		 when(mockedAlertLocalization.localizeBody(bodyStrings)).thenReturn("body");
		 doReturn(USER_NAME).when(mockedEmailAlertSender).getUserName();
		 doReturn("123456").when(mockedEmailAlertSender).getPassword();
		 doReturn(LOCALHOST).when(mockedEmailAlertSender).getHost();
		 doReturn(USER_EMAIL).when(mockedEmailAlertSender).getEmail();

//		 SMTP.setServerStartupTimeout(2400);
		 mailServer.start();
		 mailServer.setUser(mockedEmailAlertSender.getHost(), 
				 			mockedEmailAlertSender.getUserName(), 
				 			mockedEmailAlertSender.getPassword());
		 
		 Guice.createInjector(Modules.override(new AlertsModule()).with(new TestAlertsModule()), 
				 				BoundFieldModule.of(this)).injectMembers(this);
	}
	 
	 @After
	 public void tearDown(){
		 mailServer.stop();
	 }
	 
	@Test
	public void testProvideEmailAlertMechanism() throws MessagingException, IOException {
		emailAlertMechanism.send();
        
        MimeMessage[] messages = mailServer.getReceivedMessages();
        assertNotNull(messages);
        assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        assertEquals(subject, m.getSubject());
        assertTrue(String.valueOf(m.getContent()).contains(body));
        assertEquals(recipient, m.getAllRecipients()[0].toString());
	}

	@Test
	public void testProvideSMSOverEmailAlertMechanism() throws MessagingException, IOException {
		smsOverEmailAlertMechanism.send();

        MimeMessage[] messages = mailServer.getReceivedMessages();
        assertNotNull(messages);
        assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        assertEquals(phoneNumber, m.getSubject());
        assertTrue(String.valueOf(m.getContent()).contains(body));
        assertEquals(recipient, m.getAllRecipients()[0].toString());
	}

	@Test
	public void testProvidesTransport() throws MessagingException{
		assertEquals("smtp:", transport.getURLName().toString());
	}
	@Test
	public void testProvideProperties() {
		assertEquals(ServerSetupTest.SMTP.getPort(), properties.get("mail.smtp.port"));
		assertEquals("true", properties.get("mail.smtp.auth"));
		assertEquals(LOCALHOST, properties.get("mail.smtp.host"));
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
