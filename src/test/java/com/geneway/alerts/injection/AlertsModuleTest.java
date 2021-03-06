/*
 * Copyright (C) 2019 Firas Swidan†
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.geneway.alerts.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

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
import org.junit.experimental.categories.Category;
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
import com.geneway.alerts.injection.testing.TestAlertsModule;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.inject.util.Modules;
import com.nutrinfomics.geneway.shared.testcategory.FastTest;

@Category(value = {FastTest.class})
public class AlertsModuleTest {

  @Rule public ExpectedException thrown = ExpectedException.none();

  private final String RECIPIENT_EMAIL = "recipient@localhost.com";
  private final String RECIPIENT_PHONE = "24";

  private final String SUBJECT = "subject";
  private final String BODY = "body";

  private static final String USER_NAME = "alertsUser";
  private static final String USER_EMAIL = USER_NAME + "@localhost";

  @Bind @Mock AlertSpecification mockedAlertSpecification;

  @Mock AlertMessage mockedAlertMessage;
  @Mock AlertRecipient mockedAlertRecipient;
  @Mock AlertLocalization mockedAlertLocalization;
  @Mock AlertSender mockedAlertSender;

  @Inject AlertMechanism emailAlertMechanism;
  @Inject MimeMessage mimeMessage;
  @Inject Session session;

  @Inject
  @Named("alertMechanismSubject")
  String subject;

  @Inject
  @Named("alertMechanismBody")
  String body;

  @Inject Transport transport;

  @Inject
  @Named("alertRecipient")
  String recipient;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    String[] bodyStrings = new String[] {BODY};

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
    doReturn("123456".toCharArray()).when(mockedAlertSender).getPassword();
    doReturn(TestAlertsModule.LOCALHOST).when(mockedAlertSender).getHost();
    doReturn(USER_EMAIL).when(mockedAlertSender).getEmail();

    TestAlertsModule.MAIL_SERVER.start();
    TestAlertsModule.MAIL_SERVER.setUser(
        mockedAlertSender.getHost(),
        mockedAlertSender.getUserName(),
        Arrays.toString(mockedAlertSender.getPassword()));
  }

  @After
  public void tearDown() {
    TestAlertsModule.MAIL_SERVER.stop();
  }

  private void setUpInjection() {
    Guice.createInjector(
            Modules.override(new AlertsModule()).with(new TestAlertsModule()),
            BoundFieldModule.of(this))
        .injectMembers(this);
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

    MimeMessage[] messages = TestAlertsModule.MAIL_SERVER.getReceivedMessages();
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

    MimeMessage[] messages = TestAlertsModule.MAIL_SERVER.getReceivedMessages();
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

    assertEquals(TestAlertsModule.LOCALHOST, session.getProperties().getProperty("mail.smtp.host"));
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

/*
 * †Dr Firas Swidan, PhD. frsswdn@gmail.com. firas.swidan@icloud.com.
 * https://www.linkedin.com/in/swidan
 * POBox  8125,  Nazareth  16480, Israel.
 * Public key: AAAAB3NzaC1yc2EAAAADAQABAAACAQD6Lt98LolwuA/aOcK0h91ECdeiyG3QKcUOT/CcMEPV64cpkv3jrLLGoag7YtzESZ3j7TLEd0WHZ/BZ9d+K2kRfzuuCdMMhrBwqP3YObbTbSIM6NjUNwbH403LLb3FuYApUt1EvC//w64UMm7h3fTo0vdyVuMuGnkRZuM6RRAXcODM4tni9ydd3ZQKN4inztkeH/sOoM77FStk8E2VYbljUQdY39zlRoZwUqNdKzwD3T2G00tmROlTZ6K5L8i68Zqt6s0XNS6XQvS3zXe0fI6UwuetnDrcVr1Yb8y2T8lfjMG9+9L2aKPoUOlOMMcyqM+oKVvRUOSdrzmtKOljnYC7TqzvsKrfXHvHlqHxxhPp1K7B/YWrHwCDbqp02dXdIaXkkHCIqKFNaY06HEWt4obDxppVhC8IabSb55LQVCCT7J4TFbwp6rID2+Y1L7NEvR3v3oaWSlQIZ+WSG04mwh9/7gRCt7XUoqmEXCCPoHqZXq5sWv193XA57pD5gKoX7Rf2i6UdbduNTMIhQMqcWIaPMBFwxUv/LRQCHnS+mlW2GnIHIHHGS/S46MurZ6BMvcb7fEz/NorVxvh3DbUaVTteMYcikH0y5sPmGECB1d99ENBBSEX6diI+PneFp2sOouQ6gOBWy6WAt3spGfLTOFMPo3bMV/UpktkQPpXkmfd1esQ==
 */
