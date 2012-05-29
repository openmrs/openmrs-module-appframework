<%
	def before = ui.includeFragment("runningApp", config)
	if (config.afterAppHeader) {
		before += config.afterAppHeader
	}
	ui.decorateWith("standardPage", [ beforeContent: before ])
%>

<%= config.content %>