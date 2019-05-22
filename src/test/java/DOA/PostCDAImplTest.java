package DOA;

import DAO.Facade.PostCDAImpl;
import DAO.Facade.UserCDAImpl;
import Entity.Account;
import Entity.Post;
import Entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

class PostCDAImplTest {

    private PostCDAImpl postCDA = new PostCDAImpl();
    private UserCDAImpl userCDA = new UserCDAImpl();
    private User user;

    @BeforeEach
    public void setup()
    {
        user = new User("Jason","","",new Account("jason@json.nl","Gaston"));
        user = userCDA.create(user);
    }

    @Test
    void findById() {
        Post post = new Post("Testpost",user);
        Post createdPost = postCDA.create(post);

        Post foundPost = postCDA.findById(createdPost.getId());
        Assert.assertEquals(createdPost,foundPost);
        Post shouldNotBeFound = postCDA.findById(createdPost.getId()+"asdf");
        Assert.assertNull(shouldNotBeFound);
    }

    @Test
    void findByMessage() {
        Post post = new Post("Testpost",user);
        Post post2 = new Post("Testpost2",user);

        Post createdPost = postCDA.create(post);
        postCDA.create(post2);

        List<Post> foundPosts = postCDA.findByMessage(createdPost.getMessage());
        Assert.assertEquals(1,foundPosts.size());
        Assert.assertEquals(createdPost,foundPosts.get(0));

        List<Post> notFoundPosts = postCDA.findByMessage("Onzin");
        Assert.assertEquals(0,notFoundPosts.size());
    }

    @Test
    void findByUser() {
        User user2 = new User("Jan","","",new Account("jan@jansen.nl","testpass"));
        user2 = userCDA.create(user2);
        User user3 = new User("Jaap","","",new Account("jaap@jaap.nl","testasdfd"));
        user3 = userCDA.create(user3);

        Post post1 = new Post("testpost1",user);
        Post post2 = new Post("testpost2",user);
        Post post3 = new Post("testpost3",user2);

        List<Post> createdPosts = new ArrayList<>();
        createdPosts.add(postCDA.create(post1));
        createdPosts.add(postCDA.create(post2));
        postCDA.create(post3);

        List<Post> foundPosts = postCDA.findByUser(user.getId());

        List<Post> notFoundPosts = postCDA.findByUser(user3.getId());

        Assert.assertArrayEquals(createdPosts.toArray(),foundPosts.toArray());
        Assert.assertEquals(0,notFoundPosts.size());
    }

    @Test
    void findForUser() {
        User user2 = new User("Jan","","",new Account("jan@jansen.nl","testpass"));
        user2 = userCDA.create(user2);
        User user3 = new User("Jaap","","",new Account("jaap@jaap.nl","testasdfd"));
        user3 = userCDA.create(user3);

        Post post1 = new Post("testpost1",user);
        Post post2 = new Post("testpost2",user);
        Post post3 = new Post("testpost3",user2);

        List<Post> createdPosts = new ArrayList<>();
        createdPosts.add(postCDA.create(post1));
        createdPosts.add(postCDA.create(post2));
        postCDA.create(post3);

        List<Post> foundPosts = postCDA.findForUser(user);

        List<Post> notFoundPosts = postCDA.findForUser(user3);

        Assert.assertArrayEquals(createdPosts.toArray(),foundPosts.toArray());
        Assert.assertEquals(0,notFoundPosts.size());
    }

    @Test
    void findAll() {
        Assert.assertEquals(0,postCDA.findAll().size());
        User user2 = new User("Jan","","",new Account("jan@jansen.nl","testpass"));
        user2 = userCDA.create(user2);

        Post post1 = new Post("testpost1",user);
        Post post2 = new Post("testpost2",user);
        Post post3 = new Post("testpost3",user2);

        List<Post> createdPosts = new ArrayList<>();
        createdPosts.add(postCDA.create(post1));
        createdPosts.add(postCDA.create(post2));
        createdPosts.add(postCDA.create(post3));

        Assert.assertArrayEquals(createdPosts.toArray(),postCDA.findAll().toArray());
    }

    @Test
    void create() {
        Post post = postCDA.create(new Post("test",user));
        Assert.assertNotNull(post);

        Post post1 = postCDA.create(new Post("test2",user));
        Assert.assertNotEquals(post.getId(),post1.getId());
        //todo create exceptions that check message length
    }

    @Test
    void delete() {
        Post post = postCDA.create(new Post("test",user));
        Assert.assertEquals(1,postCDA.findAll().size());
        postCDA.delete(post);
        Assert.assertEquals(0,postCDA.findAll().size());
    }

    @Test
    void deleteById() {
        Post post = postCDA.create(new Post("test",user));
        Assert.assertEquals(1,postCDA.findAll().size());
        postCDA.deleteById(post.getId());
        Assert.assertEquals(0,postCDA.findAll().size());
    }
}