package org.openmrs.module.appframework.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.appframework.domain.ComponentState;
import org.openmrs.module.appframework.domain.ComponentType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AllComponentsState {

    private static Object lockObject = new Object();

    protected final Log log = LogFactory.getLog(getClass());

	private DbSessionFactory sessionFactory;

    public void setSessionFactory(DbSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void setComponentState(String componentId, ComponentType type, boolean enabled) {
        log.debug("Setting Component State : componentId = " + componentId + " type = " + type + " enabled " + enabled);

        ComponentState componentState;
        synchronized (lockObject) {
            componentState = getComponentState(componentId, type);

            if (componentState == null) {
                componentState = new ComponentState(componentId, type, enabled);
            } else {
                componentState.setEnabled(enabled);
            }

            sessionFactory.getCurrentSession().saveOrUpdate(componentState);
        }
    }

    public ComponentState getComponentState(String componentId, ComponentType type) {
        log.debug("Fetching Component State : componentId = " + componentId + " type = " + type);

        return getComponentStateFromDB(componentId, type);
    }

    private ComponentState getComponentStateFromDB(String componentId, ComponentType type) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComponentState.class);
        criteria.add(Restrictions.eq("componentId", componentId));
        criteria.add(Restrictions.eq("componentType", type));
        return (ComponentState) criteria.uniqueResult();
    }

    public Map<String, ComponentState> getComponentStatesFromDB(ComponentType type) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComponentState.class);
        criteria.add(Restrictions.eq("componentType", type));
        List<ComponentState> rows = criteria.list();
        Map<String, ComponentState> map = new HashMap<>(rows.size());
        for (ComponentState cs : rows) {
            map.put(cs.getComponentId(), cs);
        }
        return map;
    }
}
