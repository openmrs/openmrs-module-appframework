<%
	def url = config.link?.link ?: ""
    def label = config.link?.label ?: ""
    if (url.startsWith("fragment:")) {
        def fragmentName = url.substring("fragment:".length())
		def fragmentParams = ""
		if (url.indexOf('?') > 0) {
			def fragmentAndParams = fragmentName.split("\\?", 2)
			fragmentName = fragmentAndParams[0]
			def params = fragmentAndParams[1].split("\\&")
			params.each {
				def keyAndValue = it.split("\\=")
				fragmentParams += "${ keyAndValue[0] }:'${ keyAndValue[1] }', "
			}
			fragmentParams = fragmentParams.substring(0, fragmentParams.length() - 2)
			fragmentParams = "{" + fragmentParams + "}"
			fragmentParams = ", config: " + fragmentParams
		}
        url = "javascript:showDialog({ title: '${ ui.escapeJs(label) }', fragment: '${ fragmentName }' ${ fragmentParams } })"
    } else if (url.startsWith("page:")) {
    	url = ui.pageLink(url.substring("page:".length()))
    }
%>
<a href="${ url }">
    <% if (config.link.icon) { %>
        <img src="${ ui.resourceLink("images/" + config.link.icon) }"/>
    <% } %>
    ${ label }
</a>