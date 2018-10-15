<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.*" %>

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="guestbook.BlogPost" %>

<%@ page import="com.googlecode.objectify.*" %>


<%
	String filteredSearchTerm = request.getParameter("filteredSearchTerm");
	if(filteredSearchTerm==null){
		filteredSearchTerm = "";
	}
%>


<html>


  	<head>
		<title>Dylan and Jack's Foreign Forum</title>
  		<link rel="stylesheet" href="style.css">
  	</head>



  	<body align="center">
		<a name="top"></a>
  		<div id="title_tab" align="center">
  			<img src="logo.png" alt="globe" height=42px width=42px align="center">
			<h1>
				<a href="index.jsp" id="main_title">Dylan and Jack's Foreign Forum</a>
			</h1>
	  	</div>

  <%
  if(filteredSearchTerm.equals("")){
  %>
	<h2 align="center" class="post_title">
		All Blog Posts ever
	</h2>
  <%
  }else{
  %>
	<h2 align="center" class="post_title">
		Filtered Blog Posts
	</h2>

  <%
  }
  %>


<%


	String guestbookName = request.getParameter("guestbookName");

	if (guestbookName == null) {

	    guestbookName = "default";

	}

	pageContext.setAttribute("guestbookName", guestbookName);

    UserService userService = UserServiceFactory.getUserService();

    User user = userService.getCurrentUser();


%>





<%

    // Run an ancestor query to ensure we see the most up-to-date

    // view of the Greetings belonging to the selected Guestbook.

   	ObjectifyService.register(BlogPost.class);

	List<BlogPost> greetings = ObjectifyService.ofy().load().type(BlogPost.class).list();

	Collections.sort(greetings);

	Collections.reverse(greetings);

    if (greetings.isEmpty()) {

        %>

        <p>Guestbook '${fn:escapeXml(guestbookName)}' has no messages.</p>

        <%

    } else {

        %>


        <p>BlogPosts in Page '${fn:escapeXml(guestbookName)}'.</p>
		<hr>


        <%

        if(greetings.size()>0){
	        for (int i=0; i<greetings.size(); i++) {

	       	 	BlogPost blogpost = greetings.get(i);

	            pageContext.setAttribute("blogpost_title", blogpost.getTitle());

	            pageContext.setAttribute("blogpost_date", blogpost.getDate());

	        		pageContext.setAttribute("blogpost_content", blogpost.getContent());

	        		System.out.println(blogpost.getTitle());
	        		System.out.println(filteredSearchTerm);
				if(!(filteredSearchTerm.equals(""))){
					if(!(blogpost.getTitle().toLowerCase().contains(filteredSearchTerm.toLowerCase()))){
	        				continue;
					}
				}




	        	%>
	        		<%
	        		if(blogpost.getTitle() == null || blogpost.getTitle().equals("")){

	        		%>
	        			<h5><b>Untitled</b></h5>

	        		<% }else{
	        		%>
	        			<h5 class="post_title"><b>${fn:escapeXml(blogpost_title)}</b></h5>

	        		<%
	        		}


	        		if (blogpost.getUser() == null) {

	                    %>

	                    <p><i>By: Anonymous</i></p>

	                    <%

	                } else {

	                    pageContext.setAttribute("blogpost_user", blogpost.getUser());

	                    %>

	                    <p><i>By: ${fn:escapeXml(blogpost_user.nickname)}</i></p>

	                    <%

	                }

	                %>

	               	<p><i>Posted: ${fn:escapeXml(blogpost_date)}</i></p>

	                <blockquote>${fn:escapeXml(blogpost_content)}</blockquote>
	                <br>
	                <hr>

	            <%

	        }
        }
    }

%>
		<a href="#top" class="post_title" style="text-decoration: underline">Back to top</a>
		<hr>
		<p></p>


		
  	</body>

</html>
