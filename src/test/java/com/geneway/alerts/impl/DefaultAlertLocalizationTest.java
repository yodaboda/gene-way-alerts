package com.geneway.alerts.impl;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

import com.geneway.alerts.impl.DefaultAlertLocalization;

public class DefaultAlertLocalizationTest {

	@Test
	public void testLocalizeSubject() {
		DefaultAlertLocalization alertLocalization = new DefaultAlertLocalization(Locale.getDefault());
		String subject = "subject";
		assertEquals(subject, alertLocalization.localizeSubject(subject));
	}

	@Test
	public void testLocalizeSubjectNullLocale() {
		DefaultAlertLocalization alertLocalization = new DefaultAlertLocalization(null);
		String subject = "subject";
		assertEquals(subject, alertLocalization.localizeSubject(subject));
	}

	@Test
	public void testLocalizeBody() {
		DefaultAlertLocalization alertLocalization = new DefaultAlertLocalization(Locale.forLanguageTag("ar"));
		String body = "body";
		assertEquals(body, alertLocalization.localizeSubject(body));
	}

}
