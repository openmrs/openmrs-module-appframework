<%
def edit = metadata != null && metadata."${ uniqueIdProperty }"
def uniqueId = edit ? metadata."${ uniqueIdProperty }" : null
%>

<h3><%= edit ? "Edit ${ ui.message(titleCode) }" : "Create ${ ui.message(titleCode) }" %></h3>

<%= ui.startForm("save", [successUrl: config.returnUrl]) %>
	<% if (edit) { %>
		<input type="hidden" name="id" value="${ uniqueId }"/>
	<% } %>
	
	<% propertiesToEdit.each { propName, label ->
		def visibleFieldId = ui.randomId("field")
	%>
		<label for="${ visibleFieldId }" class="field-label">
			${label}
		</label>
		<span class="field-content">
			${ ui.includeFragment("widget/editProperty", [bean: metadata, property: propName, config: propertyConfiguration[propName], visibleFieldId: visibleFieldId]) }
		</span>
	<% } %>
	
	<% if (edit && metadata.retired) { %>
		<div class="retired-info">
			Retired by ${ ui.format(metadata.retiredBy) } on ${ ui.format(metadata.dateRetired) }
			<a class="button" href="${ ui.actionLink("unretire", [id: uniqueId, successUrl: config.returnUrl]) }">Unretire</a>
		</div>
	<% } %>
	
	${ ui.includeFragment("saveOrCancel", [cancelUrl: config.returnUrl]) }
	
	<% if (edit && !metadata.retired) { %>
		<a style="margin-left: 1em" class="button" href="${ ui.actionLink("retire", [id: uniqueId, successUrl: config.returnUrl]) }">Retire</a>
	<% } %>
	
<%= ui.endForm() %>