<%
    import org.openmrs.ui.framework.extension.LinkExtension 

	ui.setPageTitle(ui.message("appframework.manageMetadata.title"))
	ui.decorateWith("standardPage")
%>

<h2>${ ui.message("appframework.manageMetadata.title") }</h2>

${ ui.includeFragment("widget/linkList",
	[ items: ui.extensionManager.getExtensions(LinkExtension.class, "admin.manageMetadata") ]) }
