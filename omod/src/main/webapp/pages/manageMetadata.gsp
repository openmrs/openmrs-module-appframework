<%
    import org.openmrs.ui.framework.extension.LinkExtension 

	ui.setPageTitle(ui.message("manageMetadata.title"))
	ui.decorateWith("runningApp", [ appId: "manageMetadata" ])
%>

${ ui.includeFragment("standardIncludes") }
${ ui.includeFragment("dialogSupport") }

<h2>${ ui.message("manageMetadata.title") }</h2>

${ ui.includeFragment("widget/linkList",
	[ items: ui.extensionManager.getExtensions(LinkExtension.class, "admin.manageMetadata") ]) }
