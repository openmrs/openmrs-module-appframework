package org.openmrs.module.appframework.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AppFrameworkController {

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/appframework/apps.json", method = RequestMethod.GET)
    @ResponseBody
    public List<AppDescriptor> getAppList() {
        log.info("Fetching the list of applications");

        AppFrameworkService appFrameworkService = Context.getService(AppFrameworkService.class);
        List<AppDescriptor> appsList = appFrameworkService.getAllApps();

        log.debug("Fetched all apps : " + appsList);

        return appsList;
    }

}
