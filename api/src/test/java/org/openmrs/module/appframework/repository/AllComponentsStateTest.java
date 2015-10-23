package org.openmrs.module.appframework.repository;

import org.hibernate.Criteria;
import org.openmrs.api.db.hibernate.DbSessionFactory;  
import org.openmrs.api.db.hibernate.DbSession;  
import org.hibernate.criterion.Criterion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.openmrs.module.appframework.domain.ComponentState;
import org.openmrs.module.appframework.domain.ComponentType;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AllComponentsStateTest {

    @Mock
    private DbSessionFactory sessionFactory;

    private AllComponentsState allComponentsState;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        allComponentsState = new AllComponentsState();
        allComponentsState.setSessionFactory(sessionFactory);
    }

    @Test
    public void testCreateNewComponentState() {
        String componentId = "componentId";
        ComponentType componentType = ComponentType.APP;
        Criteria mockCriteria = mock(Criteria.class);
        DbSession mockSession = mock(DbSession.class);
        when(sessionFactory.getCurrentSession()).thenReturn(mockSession);
        when(mockSession.createCriteria(ComponentState.class)).thenReturn(mockCriteria);
        when(mockCriteria.uniqueResult()).thenReturn(null);

        allComponentsState.setComponentState(componentId, componentType, true);

        ArgumentCaptor<ComponentState> componentStateArgumentCaptor = ArgumentCaptor.forClass(ComponentState.class);
        verify(mockSession).saveOrUpdate(componentStateArgumentCaptor.capture());
        ComponentState componentState = componentStateArgumentCaptor.getValue();
        assertEquals(null, componentState.getId());
        assertEquals(componentId, componentState.getComponentId());
        assertEquals(componentType, componentState.getComponentType());
        assertEquals(true, (boolean) componentState.getEnabled());
    }

    @Test
    public void testModifyExistingComponentState() {
        String componentId = "componentId";
        ComponentType componentType = ComponentType.EXTENSION;
        int componentStateId = 100;
        ComponentState existingComponentState = new ComponentState(componentId, componentType, false);
        existingComponentState.setId(componentStateId);
        Criteria mockCriteria = mock(Criteria.class);
        DbSession mockSession = mock(DbSession.class);
        when(sessionFactory.getCurrentSession()).thenReturn(mockSession);
        when(mockSession.createCriteria(ComponentState.class)).thenReturn(mockCriteria);
        when(mockCriteria.uniqueResult()).thenReturn(existingComponentState);

        allComponentsState.setComponentState(componentId, componentType, true);

        ArgumentCaptor<ComponentState> componentStateArgumentCaptor = ArgumentCaptor.forClass(ComponentState.class);
        verify(mockSession).saveOrUpdate(componentStateArgumentCaptor.capture());
        assertEquals(componentStateId, (int) componentStateArgumentCaptor.getValue().getId());
        assertEquals(true, (boolean)componentStateArgumentCaptor.getValue().getEnabled());
    }
}
