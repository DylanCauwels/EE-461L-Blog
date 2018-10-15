package guestbook;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Blog {
    @Id long id;
    String name;

    public Blog(String name) {
        this.name = name;
    }
}