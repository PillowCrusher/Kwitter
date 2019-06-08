package DOA;

import DAO.Facade.PostDaoJPAImpl;
import DAO.Facade.UserDaoImpl;
import Entity.Account;
import Entity.Post;
import Entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

class PostDaoJPAImplTest {

    private UserDaoImpl userDao;
    private EntityManager em;
    private PostDaoJPAImpl postDoa;
    private Account account;
    private Account account1;
    private Account account2;
    private User user;
    private User user1;
    private User user2;
    private Post post;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        EntityManagerFactory entityManager = Persistence.createEntityManagerFactory("kwetterTestsPU");
        em = entityManager.createEntityManager();
        em.getTransaction().begin();

        userDao = new UserDaoImpl();
        userDao.setEntityManager(em);

        postDoa = new PostDaoJPAImpl();
        postDoa.setEntityManager(em);

        account = new Account("Jeroen1@peters.nl","wachtw");
        account1 = new Account("Jeroen2@peters.nl","wachtw");
        account2 = new Account("Jeroen3@peters.nl","wachtw");

        user = new User("Jeroen","birb","Maker van dit project yo",account);
        user1 = new User("Henk","birb","Maker van dit project yo",account1);
        user2 = new User("Jero","birb","Maker van dit project yo",account2);

        userDao.create(user);
        userDao.create(user1);
        userDao.create(user2);
        }

    @AfterEach
    void tearDown() {
        em.flush();
        em.getTransaction().commit();
    }

    private void createPosts(){
        post = new Post("Hallo!",user);
        postDoa.create(post);
        post1 = new Post("Hallo daar",user1,post);
        postDoa.create(post1);
        post2 = new Post("Hoe gaat het?",user2);
        postDoa.create(post2);
        post3 = new Post("Best wel goed!",user1,post2);
        postDoa.create(post3);
    }

    @Test
    void findByMessage() {
        //Arrange
        createPosts();

        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post1);
        //Act
        List<Post> returnPost = postDoa.findByMessage("Hoe");
        //Assert
        Assert.assertEquals(posts,returnPost);
    }

    @Test
    void findByUser() {
        //Arrange
        createPosts();

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post3);
        //Act
        List<Post> returnedPosts = postDoa.findByUser(user1.getId());
        //Assert
        Assert.assertEquals(posts,returnedPosts);
    }

    @Test
    void findAll() {
        //Arrange
        createPosts();

        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        //Act
        List<Post> returnList = postDoa.findAll();
        //Assert
        Assert.assertEquals(posts,returnList);
    }

    @Test
    void create() {
        //Arrange
        String id = post.getId();
        //Act
       Post returnPost = postDoa.create(post);
        //Assert
        Assert.assertNotEquals(id,returnPost.getId());
        Assert.assertEquals(post,returnPost);
    }

    @Test
    void delete() {
        //Arrange
        createPosts();

        List<Post> a = postDoa.findAll();

        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post2);
        posts.add(post3);
        //Act
        postDoa.delete(post1);
        //Assert
        Assert.assertEquals(posts,postDoa.findAll());
    }

    @Test
    void delete1() {
        //Arrange
        createPosts();

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        //Act
        postDoa.delete(post);
        //Assert
        Assert.assertEquals(posts,postDoa.findAll());
    }

    @Test
    void findForUser() {
        //Arrange
        createPosts();

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post3);
        //Act
       List<Post> foundPosts = postDoa.findForUser(user1);
        //Assert
        Assert.assertEquals(posts,foundPosts);
    }

    @Test
    void findForUser1(){
        //Arrange
        createPosts();

        user1.getFollowedUsers().add(user);
        userDao.update(user1);

        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post1);
        posts.add(post3);
        //Act
        List<Post> foundPosts = postDoa.findForUser(user1);
        //Assert
        Assert.assertEquals(posts,foundPosts);
    }
}