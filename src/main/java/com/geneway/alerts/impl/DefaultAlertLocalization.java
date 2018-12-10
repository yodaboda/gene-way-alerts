package com.geneway.alerts.impl;

import java.util.Locale;

import com.geneway.alerts.AlertLocalization;

/**
 * Default localization implementation that does nothing.
 *
 * @author Firas Swidan
 */
public class DefaultAlertLocalization implements AlertLocalization {

  @Override
  public String localizeSubject(String subject) {
    return subject;
  }

  @Override
  public String localizeBody(String... body) {
    return body[0];
  }

  @Override
  public Locale getLocale() {
    return Locale.ENGLISH;
  }
}
