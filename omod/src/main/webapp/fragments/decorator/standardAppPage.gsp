<%
	config.require("standardPageProvider")

	def before = ui.includeFragment("runningApp", config)
	if (config.afterAppHeader) {
		before += config.afterAppHeader
	}
	ui.decorateWith(config.standardPageProvider, "standardPage", [ beforeContent: before ])
%>

<%= config.content %>