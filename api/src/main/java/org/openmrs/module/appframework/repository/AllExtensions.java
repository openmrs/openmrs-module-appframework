package org.openmrs.module.appframework.repository;

import org.openmrs.module.appframework.domain.Extension;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AllExtensions {

    private List<Extension> extensions = new ArrayList<Extension>();

    public void add(List<Extension> extensions) {
        for (Extension extension : extensions) {
            add(extension);
        }
    }

    public void add(Extension extension) {
        if (this.extensions.contains(extension)) throw new IllegalArgumentException("Extension already exists");
        synchronized (this.extensions) {
            this.extensions.add(extension);
        }
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void clear() {
        extensions.clear();
    }
}
