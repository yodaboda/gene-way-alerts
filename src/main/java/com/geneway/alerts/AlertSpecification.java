package com.geneway.alerts;

public interface AlertSpecification {
  public AlertRecipient getAlertRecipient();

  public AlertMessage getAlertMessage();

  public AlertLocalization getAlertLocalization();

  public AlertSender getAlertSender();
}
