<% ui.includeCss("jquery-ui.css") %>

<div class="ui-widget"<% if (config.style) { %> style="${ config.style }"<% } %>>
	<div class="ui-widget-header ui-corner-top" style="padding: 0.1em 0.5em">
        <% if (config.refreshable && config.contentFragmentId) { %>
            <div style="float: right">
                <a href="javascript:publish('${ config.contentFragmentId }.refresh')">
                    <img width="24" height="24" src="${ ui.resourceLink("images/refresh.png") }"/>
                </a>
            </div>
        <% } %>
		${config.title}
	</div>
	<div class="ui-widget-content ui-corner-bottom" style="padding: 0.5em">
		${config.content}
	</div>
</div>