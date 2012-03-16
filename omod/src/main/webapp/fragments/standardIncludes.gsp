<% ui.includeCss("jquery-ui.css") %>
<% ui.includeCss("uiframework.css") %>
<% ui.includeCss("tipTip.css") %>
<% ui.includeCss("toastmessage/css/jquery.toastmessage.css") %>
<% ui.includeJavascript("jquery.js") %>
<% ui.includeJavascript("jquery-ui.js") %>
<% ui.includeJavascript("jquery.tipTip.minified.js") %>
<% ui.includeJavascript("jquery.toastmessage.js") %>
<% ui.includeJavascript("pagebus/simple/pagebus.js") %>
<% ui.includeJavascript("uiframework.js") %>
${ ui.includeFragment("maybeRequireLogin") }
<script>
    var jq = jQuery;
    
	jq(function() {
		standardUiDecorations();
	});
</script>