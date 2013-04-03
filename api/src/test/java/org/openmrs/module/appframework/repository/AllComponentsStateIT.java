package org.openmrs.module.appframework.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.appframework.domain.ComponentState;
import org.openmrs.module.appframework.domain.ComponentType;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class AllComponentsStateIT extends BaseModuleContextSensitiveTest {

    @Autowired
    private AllComponentsState allComponentsState;

    @Test
    public void test() {
        allComponentsState.setComponentState("appId", ComponentType.APP, true);
        ComponentState appId = allComponentsState.getComponentState("appId", ComponentType.APP);
        assertEquals("appId", appId.getComponentId());
    }

}
