package org.openmrs.module.appframework.service;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AdministrativeNotification;
import org.openmrs.module.appframework.factory.AdministrativeNotificationProducer;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AdministrativeNotificationServiceImplTest {

    private AdministrativeNotificationServiceImpl service;

    @Before
    public void setUp() throws Exception {
        service = new AdministrativeNotificationServiceImpl();
    }

    @Test
    public void testGetAdministrativeNotifications() throws Exception {
        final AdministrativeNotification notification = new AdministrativeNotification();

        service.setAdministrativeNotificationProducers(Arrays.<AdministrativeNotificationProducer>asList(
                new AdministrativeNotificationProducer() {
                    @Override
                    public List<AdministrativeNotification> generateNotifications() {
                        return Arrays.asList(notification);
                    }
                }));

        List<AdministrativeNotification> notifications = service.getAdministrativeNotifications();
        assertThat(notifications.size(), is(1));
        assertThat(notifications.get(0), is(notification));
    }
}