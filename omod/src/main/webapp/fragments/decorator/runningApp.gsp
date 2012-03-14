<%
	def appLabel = app?.label ?: ui.message("appFramework.runningApp.unknownApp") 
%>

<style>
	#running-app-header {
		font-size: 0.9em;
		color: white;
		background-color: #404040;
		border-bottom: 1px black solid;
	}
	#running-app-home {
		padding-right: 1em;
	}
</style>

<div id="running-app-header">
	<span id="running-app-home" style="float: left">
		<a href="../index.htm">
			<img src="${ ui.resourceLink("images/openmrs_logo_tiny.png") }"/>
		</a>
	</span>
	<span id="running-app" style="float: left">
		<% if (app?.tinyIconUrl) { %>
			<img src="${ app.tinyIconUrl }"/>
		<% } %>
		<span id="running-app-label">${ appLabel }</span>
	</span>
	<span id="running-app-user" style="float: right">
		${ context.authenticatedUser.personName }
	</span>
	<span style="clear: both">&nbsp;</span>
</div>

<div id="content">
	${config.content}
</div>