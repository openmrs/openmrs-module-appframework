<% ui.includeJavascript("coreFragments.js") %>

${ ui.includeFragment("widget/autocomplete", [
        id: config.id,
		selected: config.currentValue,
		formFieldName: config.formFieldName,
		options: context.getLocationService().getAllLocations(),
		visibleFieldId: config.visibleFieldId
	]) }
<span id="${ config.id }-error" class="error" style="display: none"></span>
    
<% if (config.parentFormId) { %>
<script>
    FieldUtils.defaultSubscriptions('${ config.parentFormId }', '${ config.formFieldName }', '${ config.id }');
</script>
<% } %>