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
import org.junit.rules.ExpectedException;

import com.geneway.alerts.AlertSender;

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
