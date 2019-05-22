package Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@NamedQueries(
        {
                @NamedQuery(
                        name = "Post.findAll",
                        query = "SELECT p FROM Post p"),
                @NamedQuery(
                        name = "Post.findByMessage",
                        query = "SELECT p FROM Post p WHERE p.message LIKE CONCAT('%',:message,'%')"
                ),
                @NamedQuery(
                        name = "Post.findByUser",
                        query = "SELECT p From Post p WHERE p.user.id = :userId"
                ),
                @NamedQuery(
                        name = "Post.delete",
                        query = "DELETE FROM Post p WHERE p.id=:id"
                ),
                @NamedQuery(
                        name = "Post.getById",
                        query = "SELECT p FROM Post p WHERE p.id = :id"
                )

        }
)
public class Post implements Serializable {
    @Id
    @Size(max = 50)
    private String id;
    @Size(max = 150)
    private String message;
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private User user;
    @OneToOne(cascade = CascadeType.ALL)
    private Post responseTo;

    public Post(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public Post(String message, User user, Post responseTo) {
        this.message = message;
        this.user = user;
        this.responseTo = responseTo;
    }

    public Post() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Post getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(Post responseTo) {
        this.responseTo = responseTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
