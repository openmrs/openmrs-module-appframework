package org.openmrs.module.appframework.test;

import org.openmrs.module.appframework.domain.AdministrativeNotification;
import org.openmrs.module.appframework.domain.AdministrativeNotificationAction;
import org.openmrs.module.appframework.factory.AdministrativeNotificationProducer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ExampleAdministrativeNotificationProducer implements AdministrativeNotificationProducer {

    @Override
    public List<AdministrativeNotification> generateNotifications() {
        AdministrativeNotificationAction action = new AdministrativeNotificationAction();
        action.setId("confirm");
        action.setLabel("Confirm");
        action.setScript("confirm()");

        AdministrativeNotification notification = new AdministrativeNotification();
        notification.setId("id");
        notification.setLabel("label");
        notification.setActions(Arrays.asList(action));

        return Arrays.asList(notification);
    }
}
