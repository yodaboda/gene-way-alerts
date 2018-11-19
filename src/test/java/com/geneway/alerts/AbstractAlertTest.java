package com.geneway.alerts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.geneway.alerts.mechanism.AlertMechanism;

public class AbstractAlertTest {
	
	private AbstractAlert buildAbstractAlert(AlertMechanism alertMechanism){
		return new AbstractAlert(alertMechanism) {
			
			@Override
			public void remind() {
				
			}			
		};
	}
	
	@Test
	public void testGetAlertMechanism() {
		AlertMechanism mockedAlertMechanism = mock(AlertMechanism.class);
		AbstractAlert abstractAlert = buildAbstractAlert(mockedAlertMechanism);
		assertEquals(mockedAlertMechanism, abstractAlert.getAlertMechanism());
	}

	@Test
	public void testGetAlertMechanismNull() {
		AbstractAlert abstractAlert = buildAbstractAlert(null);
		assertEquals(null, abstractAlert.getAlertMechanism());
	}
	
	@Test
	public void testCancel() {
		AlertMechanism mockedAlertMechanism = mock(AlertMechanism.class);
		AbstractAlert abstractAlert = buildAbstractAlert(mockedAlertMechanism);
		assertNotNull(abstractAlert.getAlertMechanism());
		abstractAlert.cancel();
		assertNull(abstractAlert.getAlertMechanism());
	}

}
