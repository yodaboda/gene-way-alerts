package com.geneway.alerts.injection;

import java.util.Locale;

import com.geneway.alerts.localization.AlertLocalization;
import com.geneway.alerts.localization.DefaultAlertLocalization;
import com.geneway.alerts.mechanism.AlertMechanism;
import com.geneway.alerts.mechanism.EmailAlertMechanism;
import com.geneway.alerts.mechanism.SMSOverEmailAlertMechanism;
import com.geneway.alerts.message.AlertMessage;
import com.geneway.alerts.message.EmailAlertMessage;
import com.geneway.alerts.recipient.AlertRecipient;
import com.geneway.alerts.recipient.EmailAlertRecipient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class AlertsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AlertMechanism.class).annotatedWith(Names.named("emailAlertMechanism")).to(EmailAlertMechanism.class);
		bind(AlertMessage.class).annotatedWith(Names.named("emailAlertMessage")).to(EmailAlertMessage.class);
		bind(AlertRecipient.class).annotatedWith(Names.named("emailAlertRecipient")).to(EmailAlertRecipient.class);
		bind(AlertLocalization.class).annotatedWith(Names.named("emailAlertLocalization")).to(DefaultAlertLocalization.class);
				
		
		bind(AlertMechanism.class).annotatedWith(Names.named("smsAlertMechanism")).to(SMSOverEmailAlertMechanism.class);
		bind(AlertMessage.class).annotatedWith(Names.named("smsAlertMessage")).to(EmailAlertMessage.class);
		bind(AlertRecipient.class).annotatedWith(Names.named("smsAlertRecipient")).to(EmailAlertRecipient.class);
		bind(AlertLocalization.class).annotatedWith(Names.named("smsAlertLocalization")).to(DefaultAlertLocalization.class);

	}
	
	@Provides
	public Locale provideLocale(){
		return Locale.ENGLISH;
	}
	
}
