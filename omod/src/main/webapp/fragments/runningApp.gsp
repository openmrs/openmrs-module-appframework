<%
	def running = appStatus != null;
	def appLabel = running ? (appStatus?.app?.label ?: ui.message("appFramework.runningApp.unknownApp")) : null
%>

<% if (running) { %>
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
		#running-app-icon {
			height: 1em;
		}
		#running-app-header a:link, #running-app-header a:visited {
			color: white;
			text-decoration: none;
		}
		#running-app-header a:hover {
			text-decoration: underline;
		}
	</style>
	
	<div id="running-app-header">
		<span id="running-app-home" style="float: left">
			<a href="/${ contextPath }/index.htm">
				<img id="running-app-icon" src="${ ui.resourceLink("images/openmrs_logo_tiny.png") }"/>
			</a>
			&nbsp;&nbsp;
			&#187;
		</span>
		<a href="/${ contextPath }/${ appStatus.app.homepageUrl }" id="running-app" style="float: left">
			<% if (appStatus?.app?.tinyIconUrl) { %>
				<img src="${ appStatus.app.tinyIconUrl }"/>
			<% } %>
			<span id="running-app-label">${ appLabel }</span>
		</a>
		<span id="running-app-user" style="float: right">
			${ context.authenticatedUser.personName }
		</span>
		<span style="clear: both">&nbsp;</span>
	</div>
<% } %>