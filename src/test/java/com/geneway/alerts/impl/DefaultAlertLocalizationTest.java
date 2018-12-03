package com.geneway.alerts.impl;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.junit.Test;

public class DefaultAlertLocalizationTest {

  @Test
  public void testLocalizeSubject() {
    DefaultAlertLocalization alertLocalization = new DefaultAlertLocalization();
    String subject = "subject";
    assertEquals(subject, alertLocalization.localizeSubject(subject));
  }

  @Test
  public void testLocalizeBody() {
    DefaultAlertLocalization alertLocalization = new DefaultAlertLocalization();
    String body = "body";
    assertEquals(body, alertLocalization.localizeSubject(body));
  }

  @Test
  public void testGetLocale() {
    DefaultAlertLocalization alertLocalization = new DefaultAlertLocalization();
    assertEquals(Locale.ENGLISH, alertLocalization.getLocale());
  }
}
