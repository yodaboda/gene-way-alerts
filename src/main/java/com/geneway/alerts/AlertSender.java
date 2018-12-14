package com.geneway.alerts;

/**
 * Details of the alert sender.
 *
 * @author Firas Swidan
 */
public interface AlertSender {
  /**
   * The user name of the alert sender.
   *
   * @return The login details of the alert sender
   */
  public String getUserName();
  /**
   * The password of the alert sender
   *
   * @return The login password of the alert sender
   */
  public char[] getPassword();
  /**
   * The host of the alert sender
   *
   * @return The host used for sending the alerts
   */
  public String getHost();
  /**
   * The email of the alert sender
   *
   * @return The email address to send the alerts from
   */
  public String getEmail();
}
