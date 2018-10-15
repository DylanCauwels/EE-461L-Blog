package guestbook;


import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;

import java.util.Date;

 

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

 

public class OfyBlogPostServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {

        UserService userService = UserServiceFactory.getUserService();

        User user = userService.getCurrentUser();
        
        ObjectifyService.register(BlogPost.class);

        // We have one entity group per Guestbook with all BlogPosts residing

        // in the same entity group as the Guestbook to which they belong.

        // This lets us run a transactional ancestor query to retrieve all

        // BlogPosts for a given Guestbook.  However, the write rate to each

        // Guestbook should be limited to ~1/second.

        String guestbookName = req.getParameter("guestbookName");
        
        String title = req.getParameter("title");
        
        if(title == null || title.equals("")){
        		title = "Untitled";
        }

        String content = req.getParameter("content");

        BlogPost blogpost = new BlogPost(user, title, content, guestbookName);

        ofy().save().entity(blogpost).now();
 
        resp.sendRedirect("/index.jsp?guestbookName=" + guestbookName);

    }

}

