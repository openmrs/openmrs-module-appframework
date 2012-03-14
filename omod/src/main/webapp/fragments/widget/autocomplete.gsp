<%
	def getVal = { it -> it."${ config.valueProperty ?: "id" }" }
	def getLabel =  { it -> config.labelProperty ? it."${ config.labelProperty }" : ui.format(it) }
	def toJsObject = { it -> """{ val: "${ getVal(it) }", label: "${ getLabel(it) }"} """ }
%>

<script>
<% if (!config.source) { %>
	var ${ config.id }_opts = [
		<% config.options.eachWithIndex { obj, i -> %>
			<% if (i != 0) { out << ',' } %>${ toJsObject(obj) }<% } %>
	];
<% } %>
	jq(document).ready(function() {
		jq('#${ config.id }')
			.autocomplete({
				disabled: ${ config.disabled ?: false },
				autoFocus: ${ config.autoFocus ?: false },
				delay: ${ config.delay ?: 100 },
				minLength: ${ config.minLength ?: 0 },
				source:  ${ config.source ? "\"" + config.source + "\"" : config.id + '_opts' },
				select: function(event, ui) {
					jq('#${ config.id }_value').val(ui.item.val);
				},
				change: function(event, ui) {
				    // TODO improve this so: that if they partially typed something,  
                    // * if they left the field blank that's valid, and it sets the hidden value to ""
				    // * if they typed something that matches exactly one option, we select that
				    // * if they typed something that matches 0 or 2+ options, we leave the partial text there, but highlight it as bad
				    // * also change the select event to clear the 'bad' highlight
				    if (!ui.item) {
				        jq('#${ config.id }').val("");
				        jq('#${ config.id }_value').val("");
				    }
				}
			});
	});

</script>

<input type="hidden" name="${ config.formFieldName }" id="${ config.id }_value" value="${ config.selected ? getVal(config.selected) : '' }"/>
<input type="text" id="${ config.id }" value="${ config.selected ? getLabel(config.selected) : '' }"/>
<a href="javascript:jq('#${ config.id }').autocomplete('search', '').focus();" tabindex="-1">
	<img src="${ ui.resourceLink("images/search_16.png") }"/>
</a>