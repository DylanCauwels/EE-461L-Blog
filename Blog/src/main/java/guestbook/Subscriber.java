package guestbook;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Subscriber {
	@Parent Key<Blog> guestbookName;
    @Id Long id;
    @Index String email;

	private Subscriber() {}
    public Subscriber(String email) {
        this.email = email;
    }
    
    public String getEmail() {
    		return this.email;
    }
}