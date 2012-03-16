<%
	def href = ui.pageLink(config.page)
%>
<a href="${ href }">${ ui.message("appframework.backTo", config.title) }</a>