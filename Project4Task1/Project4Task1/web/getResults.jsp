<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- set the appropriate doctype of view, wither mobile or desktop. --%>
<%= request.getAttribute("doctype") %>
<%--
  Created by IntelliJ IDEA.
  User: Shubham
  Date: 9/19/2019
  Time: 6:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%--This is the getResults page, whenever this url is typed in the browser, the summary of answers is displayed
 Whenever an answer is chosen, its count is incremented by 1. All the counts are intialized at 0--%>
<html>
<head>
    <title> Distributed Systems Project 4</title>
</head>
<body>
<%-- The Title of the page --%>
<h1>Yelp Search App Check</h1>

<p>The results from the search are as follows:</p><br>
<p>Name : <%=request.getAttribute("name")%></p><br>
<p>Contact : <%=request.getAttribute("contact")%></p><br>
<p>Rating : <%=request.getAttribute("rating")%></p><br>
<p>Review Count : <%=request.getAttribute("reviews")%></p><br>
<p>URL : <%=request.getAttribute("url")%></p><br>

</body>
</html>