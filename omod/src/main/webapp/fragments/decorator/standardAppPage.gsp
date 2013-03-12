<%
	def standardPageProvider = config.standardPageProvider ?: "uilibrary"

	def before = ui.includeFragment("runningApp", config)
	if (config.afterAppHeader) {
		before += config.afterAppHeader
	}
	ui.decorateWith(standardPageProvider, "standardPage", [ beforeContent: before ])
%>

<%= config.content %>