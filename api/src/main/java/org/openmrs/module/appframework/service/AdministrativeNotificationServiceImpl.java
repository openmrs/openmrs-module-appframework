package org.openmrs.module.appframework.service;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appframework.domain.AdministrativeNotification;
import org.openmrs.module.appframework.factory.AdministrativeNotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AdministrativeNotificationServiceImpl extends BaseOpenmrsService implements AdministrativeNotificationService {

    @Autowired(required = false)
    private List<AdministrativeNotificationProducer> administrativeNotificationProducers;
    
    @Override
    public List<AdministrativeNotification> getAdministrativeNotifications() {
        List<AdministrativeNotification> ret = new ArrayList<AdministrativeNotification>();
        if (administrativeNotificationProducers != null) {
            for (AdministrativeNotificationProducer producer : administrativeNotificationProducers) {
                List<AdministrativeNotification> notifications = producer.generateNotifications();
                if (notifications != null) {
                    ret.addAll(notifications);
                }
            }
        }
        return ret;
    }

    public void setAdministrativeNotificationProducers(List<AdministrativeNotificationProducer> administrativeNotificationProducers) {
        this.administrativeNotificationProducers = administrativeNotificationProducers;
    }
}
