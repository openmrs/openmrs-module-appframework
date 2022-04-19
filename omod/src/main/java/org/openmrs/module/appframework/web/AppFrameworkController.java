package org.openmrs.module.appframework.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AppFrameworkController {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    AppFrameworkService appFrameworkService;

    @RequestMapping(value = "/module/appframework/apps.json", method = RequestMethod.GET)
    @ResponseBody
    public List<AppDescriptor> getApps() {
        log.info("Fetching the list of apps");

        List<AppDescriptor> appsList = appFrameworkService.getAllApps();

        if (log.isDebugEnabled()) log.debug("Fetched all apps : " + appsList);

        return appsList;
    }

    @RequestMapping(value = "/module/appframework/extensions.json", method = RequestMethod.GET)
    @ResponseBody
    public List<Extension> getExtensions(@RequestParam String extensionPointId) {
        log.info("Fetching the list of extensions");

        List<Extension> extensions = appFrameworkService.getAllExtensions(extensionPointId);

        if (log.isDebugEnabled()) log.debug("Fetched extensions : " + extensions);

        return extensions;
    }

    @RequestMapping(value = "/module/appframework/enabled_apps.json", method = RequestMethod.GET)
    @ResponseBody
    public List<AppDescriptor> getEnabledApps() {
        log.info("Fetching the list of enabled apps");

        List<AppDescriptor> appsList = appFrameworkService.getAllEnabledApps();

        if (log.isDebugEnabled()) log.debug("Fetched all apps : " + appsList);

        return appsList;
    }

    @RequestMapping(value = "/module/appframework/enabled_extensions.json", method = RequestMethod.GET)
    @ResponseBody
    public List<Extension> getEnabledExtensions(@RequestParam String extensionPointId) {
        log.info("Fetching the list of enabled extensions - extensionPointId : " + extensionPointId);

        List<Extension> extensions = appFrameworkService.getAllEnabledExtensions(extensionPointId);

        if (log.isDebugEnabled()) log.debug("Fetched extensions : " + extensions);

        return extensions;
    }

    @RequestMapping(value = "/module/appframework/enable_apps.json", method = RequestMethod.GET)
    @ResponseBody
    public void enableApp(@RequestParam String appId) {
        log.info("Enabling App " + appId);

        appFrameworkService.enableApp(appId);
    }

    @RequestMapping(value = "/module/appframework/disable_apps.json", method = RequestMethod.GET)
    @ResponseBody
    public void disableApp(@RequestParam String appId) {
        log.info("Enabling App " + appId);

        appFrameworkService.disableApp(appId);
    }

    @RequestMapping(value = "/module/appframework/enable_extensions.json", method = RequestMethod.GET)
    @ResponseBody
    public void enableExtension(@RequestParam String extensionId) {
        log.info("Enabling Extension " + extensionId);

        appFrameworkService.enableExtension(extensionId);
    }

    @RequestMapping(value = "/module/appframework/disable_extensions.json", method = RequestMethod.GET)
    @ResponseBody
    public void disableExtension(@RequestParam String extensionId) {
        log.info("Disabling Extension " + extensionId);

        appFrameworkService.disableExtension(extensionId);
    }

    public void setAppFrameworkService(AppFrameworkService appFrameworkService) {
        this.appFrameworkService = appFrameworkService;
    }
}