package org.openmrs.module.appframework.domain;

import org.openmrs.module.appframework.context.AppContextModel;

/**
 * An interface for objects which can be required or excluded by evaluating a Javascript expression using {@link org.openmrs.module.appframework.service.AppFrameworkService#checkRequireExpression(Requireable, AppContextModel)}
 */
public interface Requireable {

	String getId();

	String getRequire();
}
