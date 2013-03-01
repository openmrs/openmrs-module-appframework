package org.openmrs.module.appframework.repository;

import org.openmrs.module.appframework.domain.AppDescriptor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AllAppDescriptors {

    private List<AppDescriptor> appDescriptors = new ArrayList<AppDescriptor>();

    public void add(List<AppDescriptor> appDescriptors) {
        for (AppDescriptor appDescriptor : appDescriptors) {
            add(appDescriptor);
        }
    }

    public void add(AppDescriptor appDescriptor) {
        if (this.appDescriptors.contains(appDescriptor)) throw new IllegalArgumentException("App already exists.");

        // Since the repository is an in-memory list, it has to be
        // protected against multiple threads.
        synchronized (appDescriptors) {
            this.appDescriptors.add(appDescriptor);
            Collections.sort(this.appDescriptors);
        }
    }

    public List<AppDescriptor> getAppDescriptors() {
        return appDescriptors;
    }

    public void clear() {
        appDescriptors.clear();
    }

}
