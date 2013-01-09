/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.appframework;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.openmrs.messagesource.MessageSourceService;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.Mock;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 */
public class SimpleAppDescriptorTest {

    public static final String LABEL_CODE = "moduleid.app.name";
    public static final String LOCALIZED_LABEL = "App Name";

    @Mock
    MessageSourceService messageSourceService;

    @InjectMocks
    SimpleAppDescriptor simpleAppDescriptor;

    @Before
    public void before() {
        simpleAppDescriptor = new SimpleAppDescriptor();
        initMocks(this);
    }

    @Test
    public void testSettingLabelViaCode() {
        when(messageSourceService.getMessage(LABEL_CODE)).thenReturn(LOCALIZED_LABEL);

        simpleAppDescriptor.setLabelCode(LABEL_CODE);
        assertThat(simpleAppDescriptor.getLabel(), is(LOCALIZED_LABEL));
    }

}
