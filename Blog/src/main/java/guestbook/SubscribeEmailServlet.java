package guestbook;


import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

 

public class SubscribeEmailServlet extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(SubscribeEmailServlet.class.getName());
    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {

        UserService userService = UserServiceFactory.getUserService();

        User user = userService.getCurrentUser();
        
        ObjectifyService.register(Subscriber.class);

        String subOrUnsub = req.getParameter("subOrUnsub");
        
        _logger.info("Adding Subscriber " + user.getEmail());
        Subscriber subscriber = new Subscriber(user.getEmail());
        
        List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
        
        if(subOrUnsub.equals("sub")) {
        		int subAlreadyExists = 0;
        		for(Subscriber s:subscribers) {
        			if(s.getEmail().equals(subscriber.getEmail())) {
        				subAlreadyExists = 1;
        			}
        		}
        		if(subAlreadyExists==0){
        			ofy().save().entity(subscriber).now();
        		}
        		
        }else {
        		for(Subscriber s:subscribers) {
        			if(s.getEmail().equals(subscriber.getEmail())) {
        				ofy().delete().entity(s);
        			}
        		}
        }
 
        resp.sendRedirect("/index.jsp");

    }

}
