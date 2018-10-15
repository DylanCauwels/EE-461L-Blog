package guestbook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;


@Entity
public class BlogPost implements Comparable<BlogPost> {
    @Parent Key<Blog> guestbookName;
    @Id Long id;
    @Index User user;
    @Index String title;
    @Index String content;
    @Index Date date;
    private BlogPost() {}
    public BlogPost(User user,String title, String content, String blogName) {
        this.user = user;
        this.title  = title;
        this.content = content;
        this.guestbookName = Key.create(Blog.class, blogName);
        date = new Date();
    }
    public User getUser() {
        return user;
    }
    public String getTitle() {
    		return title;
    }
    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(BlogPost other) {
        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
     }
    
    public Date getDateObj() {
    		return date;
    }
    
    public String getDate() {
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
    		return dateFormat.format(date);
    }
    
    public String toString() {
    		return ("\n" + title + "\n" + user + "\n" + this.getDate() + "\n" + content + "\n");
    }
}