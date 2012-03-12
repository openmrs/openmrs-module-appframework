/**
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
package org.openmrs.module.appframework.api.db.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.module.appframework.api.db.SingleClassDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;


/**
 * Hibernate-based helper implementation of a DAO that manages a single class
 * @param <T>
 */
public abstract class HibernateSingleClassDAO<T> implements SingleClassDAO<T> {
	
	@Autowired
	@Qualifier("sessionFactory")
	protected SessionFactory sessionFactory;
	
	protected Class<T> mappedClass;
	
	/**
	 * Marked private because you *must* provide the class at runtime when instantiating
	 * one of these, using the next constructor
	 */
	@SuppressWarnings("unused")
    private HibernateSingleClassDAO() {
	}
	
	/**
	 * You must call this before using any of the data access methods, since it's not
	 * actually possible to write them all with compile-time class information.
	 * @param mappedClass
	 */
	protected HibernateSingleClassDAO(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.SingleClassDAO#getById(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
    @Override
	@Transactional(readOnly = true)
	public T getById(Integer id) {
		return (T) sessionFactory.getCurrentSession().get(mappedClass, id);
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.SingleClassDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
    @Override
	@Transactional(readOnly = true)
	public List<T> getAll() {
		return (List<T>) sessionFactory.getCurrentSession().createCriteria(mappedClass).list();
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.SingleClassDAO#saveOrUpdate(java.lang.Object)
	 */
	@Override
	@Transactional
	public T saveOrUpdate(T object) {
		sessionFactory.getCurrentSession().saveOrUpdate(object);
		return object;
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.SingleClassDAO#create(java.lang.Object)
	 */
	@Override
	@Transactional
	public T create(T object) {
		sessionFactory.getCurrentSession().save(object);
		return object;
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.SingleClassDAO#update(java.lang.Object)
	 */
	@Override
	@Transactional
	public T update(T object) {
		sessionFactory.getCurrentSession().update(object);
		return object;
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.SingleClassDAO#delete(java.lang.Object)
	 */
	@Override
	@Transactional
	public void delete(T object) {
		sessionFactory.getCurrentSession().delete(object);
	}
	
	/**
	 * Convenience method to get the current session from the sessionFactory
	 * 
	 * @return sessionFactory.getCurrentSession()
	 */
	protected Session session() {
		return sessionFactory.getCurrentSession();
	}

}
