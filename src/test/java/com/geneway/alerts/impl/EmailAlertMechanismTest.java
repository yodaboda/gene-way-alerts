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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.geneway.alerts.AlertSender;
import com.nutrinfomics.geneway.shared.testcategory.FastTest;

@Category(value = {FastTest.class})
public class EmailAlertMechanismTest {

  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSend() throws MessagingException {
    Transport mockedTransport = mock(Transport.class);
    MimeMessage mockedMimeMessage = mock(MimeMessage.class);
    AlertSender emailAlertSender = new DefaultEmailAlertSender();
    EmailAlertMechanism alertMechanism =
        new EmailAlertMechanism(mockedTransport, mockedMimeMessage, emailAlertSender);

    Address[] addresses = new Address[1];

    when(mockedMimeMessage.getAllRecipients()).thenReturn(addresses);
    doNothing()
        .when(mockedTransport)
        .connect(
            emailAlertSender.getHost(),
            emailAlertSender.getUserName(),
            Arrays.toString(emailAlertSender.getPassword()));
    doNothing().when(mockedTransport).sendMessage(mockedMimeMessage, addresses);
    doNothing().when(mockedTransport).close();

    alertMechanism.send();
  }

  @Test
  public void testSendException() throws MessagingException {
    Transport mockedTransport = mock(Transport.class);
    MimeMessage mockedMimeMessage = mock(MimeMessage.class);
    AlertSender emailAlertSender = new DefaultEmailAlertSender();
    EmailAlertMechanism alertMechanism =
        new EmailAlertMechanism(mockedTransport, mockedMimeMessage, emailAlertSender);

    Address[] addresses = new Address[1];

    doReturn(addresses).when(mockedMimeMessage).getAllRecipients();
    doNothing()
        .when(mockedTransport)
        .connect(
            emailAlertSender.getHost(),
            emailAlertSender.getUserName(),
            Arrays.toString(emailAlertSender.getPassword()));
    doNothing().when(mockedTransport).sendMessage(mockedMimeMessage, addresses);
    doThrow(new MessagingException("cannot close")).when(mockedTransport).close();

    thrown.expect(MessagingException.class);
    thrown.expectMessage("cannot close");

    alertMechanism.send();
  }
}

/*
 * †Dr Firas Swidan, PhD. frsswdn@gmail.com. firas.swidan@icloud.com.
 * https://www.linkedin.com/in/swidan
 * POBox  8125,  Nazareth  16480, Israel.
 * Public key: AAAAB3NzaC1yc2EAAAADAQABAAACAQD6Lt98LolwuA/aOcK0h91ECdeiyG3QKcUOT/CcMEPV64cpkv3jrLLGoag7YtzESZ3j7TLEd0WHZ/BZ9d+K2kRfzuuCdMMhrBwqP3YObbTbSIM6NjUNwbH403LLb3FuYApUt1EvC//w64UMm7h3fTo0vdyVuMuGnkRZuM6RRAXcODM4tni9ydd3ZQKN4inztkeH/sOoM77FStk8E2VYbljUQdY39zlRoZwUqNdKzwD3T2G00tmROlTZ6K5L8i68Zqt6s0XNS6XQvS3zXe0fI6UwuetnDrcVr1Yb8y2T8lfjMG9+9L2aKPoUOlOMMcyqM+oKVvRUOSdrzmtKOljnYC7TqzvsKrfXHvHlqHxxhPp1K7B/YWrHwCDbqp02dXdIaXkkHCIqKFNaY06HEWt4obDxppVhC8IabSb55LQVCCT7J4TFbwp6rID2+Y1L7NEvR3v3oaWSlQIZ+WSG04mwh9/7gRCt7XUoqmEXCCPoHqZXq5sWv193XA57pD5gKoX7Rf2i6UdbduNTMIhQMqcWIaPMBFwxUv/LRQCHnS+mlW2GnIHIHHGS/S46MurZ6BMvcb7fEz/NorVxvh3DbUaVTteMYcikH0y5sPmGECB1d99ENBBSEX6diI+PneFp2sOouQ6gOBWy6WAt3spGfLTOFMPo3bMV/UpktkQPpXkmfd1esQ==
 */
