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
 * Guice Module for providing AlertMechanism. This module depends on being provided with an <code>
 *  AlertSpecification </code>.
 *
 * <p>Then it is possible to inject <code> AlertMecanism </code>.
 *
 * @author Firas Swidan
 */
public class AlertsModule extends AbstractModule {

  public static final String SMS_RECIPIENT_EMAIL_ADDRESS = "sms.gene.way@gmail.com";

  /** Configuring the bindings of the Alerts module */
  @Override
  protected void configure() {
    requireBinding(AlertSpecification.class);
  }

  /**
   * Provides an <code> AlertMechanism </code> for sending reminders through email
   *
   * @param transport The <code> Transport </code> used for sending the email
   * @param mimeMessage The content of the email
   * @param alertSpecification Details of the alert
   * @return An instantiated <code> AlertMechaism </code>.
   */
  @Provides
  public AlertMechanism provideAlertMechanism(
      Transport transport, MimeMessage mimeMessage, AlertSpecification alertSpecification) {
    return new EmailAlertMechanism(transport, mimeMessage, alertSpecification.getAlertSender());
  }

  @Provides
  protected Transport providesTransport(Session session) throws MessagingException {
    return session.getTransport("smtp");
  }

  /**
   * Provides properties of the email
   *
   * @return An instantiated <code> Properties </code> with the required email settings.
   */
  @Provides
  protected Properties provideProperties() {
    Properties mailServerProperties = System.getProperties();
    mailServerProperties.put("mail.smtp.port", "587");
    mailServerProperties.put("mail.smtp.auth", "true");
    mailServerProperties.put("mail.smtp.starttls.enable", "true");
    return mailServerProperties;
  }

  /**
   * Provides a <code> Session </code> based on the given properties.
   *
   * @param mailServerProperties The properties of the email <code> Session </code>
   * @return A <code> Session </code> based on the given email <code> Properties </code>
   */
  @Provides
  protected Session provideSession(Properties mailServerProperties) {
    return Session.getDefaultInstance(mailServerProperties, null);
  }

  /**
   * Provides a <code> MimeMessage </code> of the email reminder.
   *
   * @param getMailSession The <code> Session </code> used for sending the email
   * @param recipient The recipient of the email.
   * @param subject The email subject.
   * @param body the email body.
   * @return An instantiated <code> MimeMessage </code> associated with the given <code> Session
   *     </code> based on the given alertSpecification, subject, and body.
   * @throws MessagingException In case the instantiation of the <code> MimeMessage </code> went
   *     wrong.
   */
  @Provides
  protected MimeMessage provideMimeMessage(
      Session getMailSession,
      @Named("alertRecipient") String recipient,
      @Named("alertMechanismSubject") String subject,
      @Named("alertMechanismBody") String body)
      throws MessagingException {
    MimeMessage generateMailMessage = new MimeMessage(getMailSession);
    generateMailMessage.addRecipient(RecipientType.TO, new InternetAddress(recipient));
    generateMailMessage.setSubject(subject);
    generateMailMessage.setText(body);

    return generateMailMessage;
  }

  /**
   * Provides the recipient of the alert.
   *
   * @param alertSpecification Details of the alert
   * @return The recipient email is case of <code> AlertType.E_MAIl </code> alert, or <code>
   *      SMS_RECIPIENT_EMAIL_ADDRESS </code> in case of <code> AlertType.SMS </code>.
   */
  @Provides
  @Named("alertRecipient")
  protected String provideAlertRecipient(AlertSpecification alertSpecification) {
    boolean emailAlert = alertSpecification.getAlertRecipient().getAlertType() == AlertType.E_MAIL;
    return emailAlert
        ? alertSpecification.getAlertRecipient().getRecipient()
        : SMS_RECIPIENT_EMAIL_ADDRESS;
  }
  /**
   * Provides the subject of the alert
   *
   * @param alertSpecification Details of the alert
   * @return A localized subject in case of <code> AlertType.E_MAIL </code> or the recipient phone
   *     number in case of <code> AlertType.SMS </code>.
   */
  @Provides
  @Named("alertMechanismSubject")
  protected String provideSubject(AlertSpecification alertSpecification) {
    boolean emailAlert = alertSpecification.getAlertRecipient().getAlertType() == AlertType.E_MAIL;
    if (!emailAlert) {
      return alertSpecification.getAlertRecipient().getRecipient();
    }

    return alertSpecification
        .getAlertLocalization()
        .localizeSubject(alertSpecification.getAlertMessage().getSubject());
  }

  /**
   * Provides the body of the alert.
   *
   * @param alertSpecification Details of the alert
   * @return A localized body
   */
  @Provides
  @Named("alertMechanismBody")
  protected String provideBody(AlertSpecification alertSpecification) {
    return alertSpecification
        .getAlertLocalization()
        .localizeBody(alertSpecification.getAlertMessage().getBody());
  }
}

/*
 * †Dr Firas Swidan, PhD. frsswdn@gmail.com. firas.swidan@icloud.com.
 * https://www.linkedin.com/in/swidan
 * POBox  8125,  Nazareth  16480, Israel.
 * Public key: AAAAB3NzaC1yc2EAAAADAQABAAACAQD6Lt98LolwuA/aOcK0h91ECdeiyG3QKcUOT/CcMEPV64cpkv3jrLLGoag7YtzESZ3j7TLEd0WHZ/BZ9d+K2kRfzuuCdMMhrBwqP3YObbTbSIM6NjUNwbH403LLb3FuYApUt1EvC//w64UMm7h3fTo0vdyVuMuGnkRZuM6RRAXcODM4tni9ydd3ZQKN4inztkeH/sOoM77FStk8E2VYbljUQdY39zlRoZwUqNdKzwD3T2G00tmROlTZ6K5L8i68Zqt6s0XNS6XQvS3zXe0fI6UwuetnDrcVr1Yb8y2T8lfjMG9+9L2aKPoUOlOMMcyqM+oKVvRUOSdrzmtKOljnYC7TqzvsKrfXHvHlqHxxhPp1K7B/YWrHwCDbqp02dXdIaXkkHCIqKFNaY06HEWt4obDxppVhC8IabSb55LQVCCT7J4TFbwp6rID2+Y1L7NEvR3v3oaWSlQIZ+WSG04mwh9/7gRCt7XUoqmEXCCPoHqZXq5sWv193XA57pD5gKoX7Rf2i6UdbduNTMIhQMqcWIaPMBFwxUv/LRQCHnS+mlW2GnIHIHHGS/S46MurZ6BMvcb7fEz/NorVxvh3DbUaVTteMYcikH0y5sPmGECB1d99ENBBSEX6diI+PneFp2sOouQ6gOBWy6WAt3spGfLTOFMPo3bMV/UpktkQPpXkmfd1esQ==
 */