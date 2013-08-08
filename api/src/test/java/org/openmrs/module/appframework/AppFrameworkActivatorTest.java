package org.openmrs.module.appframework;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class AppFrameworkActivatorTest {

    @Test
    public void testCreatingLocationTagIfItDoesNotExist() throws Exception {
        LocationService locationService = mock(LocationService.class);

        new AppFrameworkActivator().setupLoginLocationTag(locationService);

        verify(locationService).saveLocationTag(argThat(new ArgumentMatcher<LocationTag>() {
            @Override
            public boolean matches(Object argument) {
                LocationTag actual = (LocationTag) argument;
                return actual.getName().equals(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN);
            }
        }));
    }

    @Test
    public void testCreatingLocationTagDoesNothingIfItAlreadyExist() throws Exception {
        LocationService locationService = mock(LocationService.class);
        when(locationService.getLocationTagByName(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN)).thenReturn(new LocationTag());

        new AppFrameworkActivator().setupLoginLocationTag(locationService);

        verify(locationService, never()).saveLocationTag(any(LocationTag.class));
    }

}
