package org.openmrs.module.appframework.service;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AdministrativeNotification;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AdministrativeNotificationServiceImplTest {

    private AdministrativeNotificationServiceImpl service;

    @Before
    public void setUp() {
        service = new AdministrativeNotificationServiceImpl();
    }

    @Test
    public void testGetAdministrativeNotifications() {
        final AdministrativeNotification notification = new AdministrativeNotification();
        service.setAdministrativeNotificationProducers(Arrays.asList(() -> Arrays.asList(notification)));
        List<AdministrativeNotification> notifications = service.getAdministrativeNotifications();
        assertThat(notifications.size(), is(1));
        assertThat(notifications.get(0), is(notification));
    }
}