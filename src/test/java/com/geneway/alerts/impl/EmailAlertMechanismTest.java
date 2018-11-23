package com.geneway.alerts.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EmailAlertMechanismTest {  
	
	private static final String SENDER_EMAIL_PASSWORD = "r8B0iR7M";
	private static final String SENDER_EMAIL_ADDRESS = "sms.gene.way@gmail.com";
	@Rule
    public ExpectedException thrown= ExpectedException.none();
	
	@Test
	public void testSend() throws MessagingException {
		Transport mockedTransport = mock(Transport.class);
		MimeMessage mockedMimeMessage = mock(MimeMessage.class);
		EmailAlertMechanism alertMechanism = new EmailAlertMechanism(mockedTransport, 
																	mockedMimeMessage,
																	SENDER_EMAIL_ADDRESS,
																	SENDER_EMAIL_PASSWORD);
		
		
		Address[] addresses = new Address[1];
		
		when(mockedMimeMessage.getAllRecipients()).thenReturn(addresses);
		doNothing().when(mockedTransport).connect("smtp.gmail.com", SENDER_EMAIL_ADDRESS,
				SENDER_EMAIL_PASSWORD);
		doNothing().when(mockedTransport).sendMessage(mockedMimeMessage, addresses);
		doNothing().when(mockedTransport).close();
		
		alertMechanism.send();
	}

	@Test
	public void testSendException() throws MessagingException {
		Transport mockedTransport = mock(Transport.class);
		MimeMessage mockedMimeMessage = mock(MimeMessage.class);
		EmailAlertMechanism alertMechanism = new EmailAlertMechanism(mockedTransport, 
																	mockedMimeMessage,
																	SENDER_EMAIL_ADDRESS,
																	SENDER_EMAIL_PASSWORD);
		
		Address[] addresses = new Address[1];
		
		doReturn(addresses).when(mockedMimeMessage).getAllRecipients();
		doNothing().when(mockedTransport).connect("smtp.gmail.com", SENDER_EMAIL_ADDRESS,
				SENDER_EMAIL_PASSWORD);
		doNothing().when(mockedTransport).sendMessage(mockedMimeMessage, addresses);
		doThrow(new MessagingException("cannot close")).when(mockedTransport).close();
		
		thrown.expect(MessagingException.class);
		thrown.expectMessage("cannot close");

		alertMechanism.send();
	}

}
