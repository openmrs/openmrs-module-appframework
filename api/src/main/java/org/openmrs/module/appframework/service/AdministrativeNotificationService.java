package org.openmrs.module.appframework.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.appframework.domain.AdministrativeNotification;

import java.util.List;

/**
 * Used to get {@link AdministrativeNotification}s
 */
public interface AdministrativeNotificationService extends OpenmrsService {

    List<AdministrativeNotification> getAdministrativeNotifications();

}
