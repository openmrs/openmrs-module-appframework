package org.openmrs.module.appframework.api.db.hibernate;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppEnabled;
import org.openmrs.module.appframework.api.db.AppEnabledDAO;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class HibernateAppEnabledDAOTest extends BaseModuleContextSensitiveTest {
	
	AppEnabledDAO dao;
	
	@Before
	public void before() throws Exception {
		executeDataSet("org/openmrs/module/appframework/include/EnabledApps.xml");
		dao = (AppEnabledDAO) applicationContext.getBean("appEnabledDao");
	}
	
	/**
	 * @see HibernateAppEnabledDAO#getEnabledAppsForUser(User)
	 * @verifies get apps enabled directly for user
	 */
	@Test
	public void getEnabledAppsForUser_shouldGetAppsEnabledDirectlyForUser() throws Exception {
		User forUser = Context.getUserService().getUserByUsername("qa");
		
		Assert.assertTrue(dao.getEnabledAppsForUser(forUser).contains("dataEntry"));
	}
	
	/**
	 * @see HibernateAppEnabledDAO#getEnabledAppsForUser(User)
	 * @verifies get apps enabled for role
	 */
	@Test
	public void getEnabledAppsForUser_shouldGetAppsEnabledForRole() throws Exception {
		User forUser = Context.getUserService().getUserByUsername("dataclerk");
		Assert.assertNull(dao.getByUserAndApp(forUser, "dataEntry")); // verify not directly assigned

		for (AppEnabled e : dao.getAll())
			System.out.println(e.getAppName() + " for " + (e.getUser() != null ? e.getUser().getUsername() : "") + " / " + (e.getRole() != null ? e.getRole().getRole() : ""));
		
		Assert.assertTrue(dao.getEnabledAppsForUser(forUser).contains("dataEntry"));
	}
	
	/**
	 * @see HibernateAppEnabledDAO#getEnabledAppsForUser(User)
	 * @verifies get apps enabled for parent role
	 */
	@Test
	public void getEnabledAppsForUser_shouldGetAppsEnabledForParentRole() throws Exception {
		User forUser = Context.getUserService().getUserByUsername("nurse");
		Assert.assertNull(dao.getByUserAndApp(forUser, "charting")); // verify not directly assigned
		for (Role role : forUser.getRoles())
			Assert.assertNull(dao.getByRoleAndApp(role, "charting")); // verify not directly assigned to this user's roles
		
		Assert.assertTrue(dao.getEnabledAppsForUser(forUser).contains("charting"));
	}
}