package com.geneway.alerts.injection;

import static org.junit.Assert.*;

import java.util.Locale;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.geneway.alerts.localization.AlertLocalization;
import com.geneway.alerts.mechanism.AlertMechanism;
import com.geneway.alerts.message.AlertMessage;
import com.geneway.alerts.recipient.AlertRecipient;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;

public class AlertsModuleTest {

	@Bind @Mock AlertMechanism mockedAlertMechanism;
	@Bind @Mock AlertMessage mockedAlertMessage;
	@Bind @Mock AlertRecipient mockedAlertRecipient;
	@Bind @Mock AlertLocalization mockedAlertLocalization;
	
	@Inject Locale locale;
	
	 @Before 
	 public void setUp() {
		 MockitoAnnotations.initMocks(this);
		 Guice.createInjector(new AlertsModule(), 
				 				BoundFieldModule.of(this)).injectMembers(this);
	}
	 
	@Test
	public void testProvideLocale() {
		assertEquals(Locale.ENGLISH, locale);
	}

	@Test
	public void testProvideEmailAlertMechanism() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideSMSOverEmailAlertMechanism() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideProperties() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideSMSOverEmailMimeMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideSession() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideMimeMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideSubject() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideRecipient() {
		fail("Not yet implemented");
	}

	@Test
	public void testProvideBody() {
		fail("Not yet implemented");
	}

}
