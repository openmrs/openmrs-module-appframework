<%
	def id = config.visibleFieldId ?: ui.randomId('field')
	ui.includeJavascript("coreFragments.js")
%>
<% if (config?.propConfig?.options) { %>
    ${ ui.includeFragment("widgets/autocomplete", [
            id: config.id,
            formFieldName: config.formFieldName,
            options: config.propConfig.options,
            valueProperty: 'value',
            labelProperty: 'label'
        ]) }
<% } else if (config?.config?.type == 'textarea') {
	def rows = config?.config?.rows ?: 5
	def cols = config?.config?.cols ?: 40
%>
	<textarea id="${ id }" name="${ config.formFieldName }" rows="${ rows }" cols="${ cols }">${ config.currentValue ?: "" }</textarea>
<% } else {
	def size = config?.propConfig?.size ?: 40
%>
	<input id="${ id }" type="text" name="${ config.formFieldName }" size="${ size }" value="${ config.currentValue ?: "" }"/>
<% } %>
<span id="${ config.id }-error" class="error" style="display: none"></span>

<% if (config.parentFormId) { %>
<script>
    FieldUtils.defaultSubscriptions('${ config.parentFormId }', '${ config.formFieldName }', '${ config.id }');
</script>
<% } %>