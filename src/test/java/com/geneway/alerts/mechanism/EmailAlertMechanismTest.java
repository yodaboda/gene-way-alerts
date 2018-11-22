package com.geneway.alerts.mechanism;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.geneway.alerts.impl.EmailAlertMechanism;

public class EmailAlertMechanismTest {  
	
	@Rule
    public ExpectedException thrown= ExpectedException.none();
	
	@Test
	public void testSend() throws MessagingException {
		Session mockedSession = mock(Session.class);
		MimeMessage mockedMimeMessage = mock(MimeMessage.class);
		EmailAlertMechanism alertMechanism = new EmailAlertMechanism(mockedSession, mockedMimeMessage);
		
		Transport mockedTransport = mock(Transport.class);
		when(mockedSession.getTransport("smtp")).thenReturn(mockedTransport);
		
		Address[] addresses = new Address[1];
		
		when(mockedMimeMessage.getAllRecipients()).thenReturn(addresses);
		doNothing().when(mockedTransport).connect("smtp.gmail.com", "sms.gene.way@gmail.com",
				"r8B0iR7M");
		doNothing().when(mockedTransport).sendMessage(mockedMimeMessage, addresses);
		doNothing().when(mockedTransport).close();
		
		alertMechanism.send();
	}

	@Test
	public void testSendException() throws MessagingException {
		Session mockedSession = mock(Session.class);
		MimeMessage mockedMimeMessage = mock(MimeMessage.class);
		EmailAlertMechanism alertMechanism = new EmailAlertMechanism(mockedSession, mockedMimeMessage);
		
		Transport mockedTransport = mock(Transport.class);
		when(mockedSession.getTransport("smtp")).thenReturn(mockedTransport);
		
		Address[] addresses = new Address[1];
		
		doReturn(addresses).when(mockedMimeMessage).getAllRecipients();
		doNothing().when(mockedTransport).connect("smtp.gmail.com", "sms.gene.way@gmail.com",
				"r8B0iR7M");
		doNothing().when(mockedTransport).sendMessage(mockedMimeMessage, addresses);
		doThrow(new MessagingException("cannot close")).when(mockedTransport).close();
		
		thrown.expect(MessagingException.class);
		thrown.expectMessage("cannot close");

		alertMechanism.send();
	}

}
