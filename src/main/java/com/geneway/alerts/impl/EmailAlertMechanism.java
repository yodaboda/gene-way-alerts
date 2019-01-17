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

package com.geneway.alerts.impl;

import java.util.Arrays;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.geneway.alerts.AlertMechanism;
import com.geneway.alerts.AlertSender;

/**
 * Sends alerts through email.
 *
 * @author Firas Swidan
 */
public class EmailAlertMechanism implements AlertMechanism {

  private Transport transport;
  private MimeMessage generateMailMessage;
  private AlertSender emailAlertSender;

  /**
   * Constructs an <code> EmailAlertMechanism </code> from the given <code> Transport </code> and
   * <code> MimeMessage </code>
   *
   * @param transport For sending the email
   * @param mimeMessage The message content and specifications
   * @param emailAlertSender The details of the alert sender
   */
  @Inject
  public EmailAlertMechanism(
      Transport transport, MimeMessage mimeMessage, AlertSender emailAlertSender) {
    this.transport = transport;
    this.generateMailMessage = mimeMessage;
    this.emailAlertSender = emailAlertSender;
  }

  /**
   * Sends an email alert from a pre-defined email account based on the specification receives in
   * the constructor.
   *
   * @throws MessagingException in case the alert send was not successful.
   */
  @Override
  public void send() throws MessagingException {
    transport.connect(
        emailAlertSender.getHost(),
        emailAlertSender.getUserName(),
        Arrays.toString(emailAlertSender.getPassword()));
    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
    transport.close();
  }

  public MimeMessage getMimeMessage() {
    return generateMailMessage;
  }
}

/*
 * †Dr Firas Swidan, PhD. frsswdn@gmail.com. firas.swidan@icloud.com.
 * https://www.linkedin.com/in/swidan
 * POBox  8125,  Nazareth  16480, Israel.
 * Public key: AAAAB3NzaC1yc2EAAAADAQABAAACAQD6Lt98LolwuA/aOcK0h91ECdeiyG3QKcUOT/CcMEPV64cpkv3jrLLGoag7YtzESZ3j7TLEd0WHZ/BZ9d+K2kRfzuuCdMMhrBwqP3YObbTbSIM6NjUNwbH403LLb3FuYApUt1EvC//w64UMm7h3fTo0vdyVuMuGnkRZuM6RRAXcODM4tni9ydd3ZQKN4inztkeH/sOoM77FStk8E2VYbljUQdY39zlRoZwUqNdKzwD3T2G00tmROlTZ6K5L8i68Zqt6s0XNS6XQvS3zXe0fI6UwuetnDrcVr1Yb8y2T8lfjMG9+9L2aKPoUOlOMMcyqM+oKVvRUOSdrzmtKOljnYC7TqzvsKrfXHvHlqHxxhPp1K7B/YWrHwCDbqp02dXdIaXkkHCIqKFNaY06HEWt4obDxppVhC8IabSb55LQVCCT7J4TFbwp6rID2+Y1L7NEvR3v3oaWSlQIZ+WSG04mwh9/7gRCt7XUoqmEXCCPoHqZXq5sWv193XA57pD5gKoX7Rf2i6UdbduNTMIhQMqcWIaPMBFwxUv/LRQCHnS+mlW2GnIHIHHGS/S46MurZ6BMvcb7fEz/NorVxvh3DbUaVTteMYcikH0y5sPmGECB1d99ENBBSEX6diI+PneFp2sOouQ6gOBWy6WAt3spGfLTOFMPo3bMV/UpktkQPpXkmfd1esQ==
 */