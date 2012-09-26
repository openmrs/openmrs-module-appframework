<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>

<style>
	.app-button {
		border: 1px black solid;
		background-color: #e0e0e0;
		padding: 0.3em;
		margin: 0.5em;
		float: left;
		text-align: center;
	}
	
	#banner {
		display: none;
	}
</style>
<script>
function enterEditSortOrderMode() {
	jQuery("#editButton").toggle();
	jQuery("#saveButton").toggle();
	jQuery(".app-button").css({border: "1px dashed green"});
	jQuery(".app-button").css('cursor','pointer');
	jQuery("#appList").sortable({cursor: "pointer"});
	jQuery("#appList").disableSelection();
}

function saveSortOrder() {
	jQuery("#editButton").toggle();
	jQuery("#saveButton").toggle();
	jQuery(".app-button").css({border: "1px solid black"});
	jQuery(".app-button").css('cursor','pointer');

	jQuery.ajax({
        type: "GET",
        url: "",
        data: jQuery("#appList").sortable("serialize")});
	
	jQuery("#appList").sortable("disable");
}

</script>

<div id="appList">
<c:forEach var="app" items="${ apps }">
	<div class="app-button" id="app_${app.id}">
		<a href="${ app.homepageUrl }">
			<c:if test="${ not empty app.iconUrl }">
				<img class="app-icon" src="${ app.iconUrl }" height="64"/><br/>
			</c:if>
			<span class="app-label">
				${ app.label }
			</span>
		</a>
	</div>
</c:forEach>
</div>

<div style="clear: both"/>
<button id="editButton" onclick="enterEditSortOrderMode()">Edit</button>
<button id="saveButton" onclick="saveSortOrder()" style="display:none">Save</button>

<%@ include file="/WEB-INF/template/footerMinimal.jsp" %>