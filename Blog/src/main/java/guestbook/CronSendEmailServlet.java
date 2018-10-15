package guestbook;

import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



@SuppressWarnings("serial")
public class CronSendEmailServlet extends HttpServlet {
private static final Logger _logger = Logger.getLogger(CronSendEmailServlet.class.getName());
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		ObjectifyService.register(Subscriber.class);
		ObjectifyService.register(BlogPost.class);
		
		List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
		
		List<BlogPost> blogPosts = ObjectifyService.ofy().load().type(BlogPost.class).list();
		
		Collections.sort(blogPosts);
		Collections.reverse(blogPosts);
		
		String emailText = "";
		for(BlogPost bp : blogPosts) {
			if(inLastDay(bp.getDateObj())) {
				emailText += bp.toString();
			}
		}
		
		//If there are blog posts to send within the last 24 hours, then email all subs
		if(!emailText.equals("")) {
			
			for(Subscriber s: subscribers) {
		
				try {
					_logger.info("Email has been sent to " + s.getEmail());
					sendSimpleMail(s.getEmail(), emailText);
				}
				catch (Exception ex) {
					_logger.info("Error in for loop");
				}
			}
		}

		
		
	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//_logger.info("Starting Cron Job");
		doGet(req, resp);
	}
	
	
	private void sendSimpleMail(String email, String emailText) {
	    // [START simple_example]
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    try {
	    	_logger.info("Start simpleMail");
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("puya@puyabrianblog.appspotmail.com", "puya"));
	      msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, "subscriber"));
	      msg.setSubject("You have subscribed to Puya and Brian's Radical Blog");
	      msg.setText("Dear Subscriber, \nHere are the blog posts made in the last 24 hours:\n\n" + emailText);
	      Transport.send(msg);
	    } catch (Exception e) {
	      _logger.info("There was an error in simple mail");
	    }
	    // [END simple_example]
	}
	
	static final long DAY = 24 * 60 * 60 * 1000;
	public boolean inLastDay(Date aDate) {
	    return aDate.getTime() > System.currentTimeMillis() - DAY;
	}
}

