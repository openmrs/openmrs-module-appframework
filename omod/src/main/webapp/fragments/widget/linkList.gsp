<% config.items.each { %>
    ${ ui.includeFragment("widget/link", [link: it]) }<br/>
<% } %>