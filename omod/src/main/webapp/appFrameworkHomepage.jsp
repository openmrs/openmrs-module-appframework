<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/headerMinimal.jsp"%>

<style>
	.app-button {
		border: 1px black solid;
		background-color: #e0e0e0;
		padding: 1em;
		margin: 1em;
		float: left;
		text-align: center;
	}
	
	#banner {
		display: none;
	}
</style>

<c:forEach var="app" items="${ apps }">
	<div class="app-button">
		<a href="${ app.homepageUrl }">
			<c:if test="${ not empty app.iconUrl }">
				<img class="app-icon" src="${ app.iconUrl }"/><br/>
			</c:if>
			<span class="app-label">
				${ app.label }
			</span>
		</a>
	</div>
</c:forEach>

<%@ include file="/WEB-INF/template/footerMinimal.jsp"%>