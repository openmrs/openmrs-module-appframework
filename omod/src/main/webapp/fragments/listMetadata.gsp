<a class="button" href="${ ui.pageLink("fragment", [fragment: ui.thisFragmentId(), mode: "edit", returnUrl: ui.thisUrl() ]) }">
	<img style="vertical-align: text-top" src="${ ui.resourceLink("images/add-24.png") }"/>
	Add another
</a>
<br/><br/>

<table class="decorated">
	<thead>
		<tr>
			<th></th>
<% propertiesToDisplay.each { prop, label -> %>
			<th>${ label }</th>
<% } %>
		</tr>
	</thead>
	<tbody>
<% allMetadata.each {
	def uniqueId = it."${ uniqueIdProperty }"
	def fragmentConfig = [fragment: ui.thisFragmentId(), mode: "edit", uniqueId: "${uniqueId}", returnUrl: ui.thisUrl() ];
%>
		<tr <% if (it.retired) { %> class="retired" <% } %>>
			<td>
				<a href="${ ui.pageLink("fragment", fragmentConfig) }">
					<img src="${ ui.resourceLink("images/edit-24.png") }"/>
				</a>
			</td>
<% propertiesToDisplay.each { prop, label ->
	def propVal = it."${prop}";
%>
			<td class="metadata-${ prop }">
				<%= ui.format(propVal) %>
			</td>
<% } %>
		</tr>
<% } %>
	</tbody>
</table>