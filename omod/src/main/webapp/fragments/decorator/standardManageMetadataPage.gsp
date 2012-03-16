<%
	ui.decorateWith("standardPage")
%>

${ ui.includeFragment("backButton", [ page: "manageMetadata", title: ui.message("appframework.manageMetadata.title") ]) }

<h3>${ config.title }</h3>

${ config.content }