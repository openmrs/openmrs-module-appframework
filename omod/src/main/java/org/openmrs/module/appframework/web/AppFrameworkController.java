package org.openmrs.module.appframework.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppFrameworkController {

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/appframework/apps.json", method = RequestMethod.GET)
    @ResponseBody
    public List<AppDescriptor> getAppList() {
        log.info("Fetching the list of applications");

        AppFrameworkService appFrameworkService = Context.getService(AppFrameworkService.class);
        List<AppDescriptor> appsList;

        User currentUser = Context.getAuthenticatedUser();
        appsList = (currentUser != null) ? appFrameworkService.getAppsForUser(currentUser) : appFrameworkService.getAllApps();

        log.debug("Fetched all apps : " + appsList);

        return appsList;
    }

}
