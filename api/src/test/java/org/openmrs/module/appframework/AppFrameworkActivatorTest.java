package org.openmrs.module.appframework;

import org.junit.Test;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppFrameworkActivatorTest {

    @Test
    public void testCreatingLocationTagIfItDoesNotExist() {
        LocationService locationService = mock(LocationService.class);
        new AppFrameworkActivator().setupLoginLocationTag(locationService);
        verify(locationService).saveLocationTag(argThat(actual ->
                actual.getName().equals(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN)
        ));
    }

    @Test
    public void testCreatingLocationTagDoesNothingIfItAlreadyExist() {
        LocationService locationService = mock(LocationService.class);
        when(locationService.getLocationTagByName(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN)).thenReturn(new LocationTag());
        new AppFrameworkActivator().setupLoginLocationTag(locationService);
        verify(locationService, never()).saveLocationTag(any(LocationTag.class));
    }

}
