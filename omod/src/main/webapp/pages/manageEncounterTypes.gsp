<%
	def title = ui.message("appframework.manageEncounterTypes.title")
	ui.setPageTitle(title)
	ui.decorateWith("standardManageMetadataPage", [ title: title ])
%>

${ ui.includeFragment("administerEncounterTypes") }