package com.geneway.alerts.impl;

import com.geneway.alerts.AlertRecipient;
import com.geneway.alerts.AlertType;

/**
 * Default implementation for the alert recipient.
 *
 * @author Firas Swidan
 */
public class DefaultAlertRecipient implements AlertRecipient {

  private String recipient;
  private AlertType alertType;

  public DefaultAlertRecipient(String recipient, AlertType alertType) {
    this.recipient = recipient;
    this.alertType = alertType;
  }

  @Override
  public String getRecipient() {
    return this.recipient;
  }

  @Override
  public AlertType getAlertType() {
    return this.alertType;
  }
}
