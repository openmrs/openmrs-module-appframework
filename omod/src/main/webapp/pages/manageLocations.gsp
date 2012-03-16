<%
	def title = ui.message("appframework.manageLocations.title")
	ui.setPageTitle(title)
	ui.decorateWith("standardManageMetadataPage", [ title: title ])
%>

${ ui.includeFragment("administerLocations") }