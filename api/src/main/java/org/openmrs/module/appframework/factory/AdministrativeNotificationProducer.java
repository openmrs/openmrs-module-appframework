package org.openmrs.module.appframework.factory;

import org.openmrs.module.appframework.domain.AdministrativeNotification;

import java.util.List;

/**
 * Modules that want to produce {@link AdministrativeNotification}s should register a bean implementing this interface
 */
public interface AdministrativeNotificationProducer {

    List<AdministrativeNotification> generateNotifications();

}
