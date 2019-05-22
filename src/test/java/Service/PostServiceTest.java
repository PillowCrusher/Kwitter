package Service;

import DAO.IPostDao;
import DAO.IUserDao;
import Entity.Account;
import Entity.Post;
import Entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PostServiceTest {


    @InjectMocks
    private PostService postService;
    @Mock
    private IPostDao postDao;
    @Mock
    private IUserDao userDao;
    private Account account;
    private Account account2;
    private User user;
    private User user2;
    private Post post;
    private Post post2;
    private Post post3;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        account = new Account("jeroen@jeroen.jeroen","wachtw","user");
        user = new User("Jan","assets/danger_birb.png","Test test test",account);

        account2 = new Account("jeroen2@jeroen.jeroen","wachtw","user");
        user2 = new User("Henk","assets/danger_birb.png","Test test test",account2);

        post = new Post("Hallo daar!",user);
        post2 = new Post("Generaal kenobi!",user2,post);
        post3 = new Post("You are a bold one",user2);
     }

    @Test
    public void TestMock(){
        assertNotNull(postService);
        assertNotNull(postDao);
        assertNotNull(userDao);
    }

    /**
     * Happy flow create post
     */
    @Test
    void create(){
        when(userDao.findById(post.getUser().getId())).thenReturn(user);
        when(postDao.create(post)).thenReturn(post);

        assertNotNull(postService.create(post));

        verify(userDao,atLeastOnce()).findById(post.getUser().getId());
        verify(postDao,atLeastOnce()).create(post);
        verify(postDao,never()).findById(post.getId());
    }

    /**
     * Happy flow with response
     */
    @Test
    void create1(){
        when(userDao.findById(post.getUser().getId())).thenReturn(user2);
        when(postDao.create(post2)).thenReturn(post2);
        when(postDao.findById(post2.getResponseTo().getId())).thenReturn(post);

        assertNotNull(postService.create(post2));

        verify(userDao,atLeastOnce()).findById(post2.getUser().getId());
        verify(postDao,atLeastOnce()).create(post2);
        verify(postDao,atLeastOnce()).findById(post.getId());
    }

    /**
     * Unhappy flow because of length
     */

    @Test
    void create3(){
        post.setMessage("Lorem Ipsum is slechts een proeftekst uit het drukkerij- en zetterijwezen. Lorem Ipsum is de standaard proeftekst in deze bedrijfstak sinds de 16e eeuw, toen een onbekende drukker een zethaak met letters nam en ze door elkaar husselde om een font-catalogus te maken. Het heeft niet alleen vijf eeuwen overleefd maar is ook, vrijwel onveranderd, overgenomen in elektronische letterzetting. Het is in de jaren '60 populair geworden met de introductie van Letraset vellen met Lorem Ipsum passages en meer recentelijk door desktop publishing software zoals Aldus PageMaker die versies van Lorem Ipsum bevatten.");

        when(userDao.findById(post.getUser().getId())).thenReturn(user);
        when(postDao.create(post)).thenReturn(post);

        assertNull(postService.create(post));

        verify(userDao,never()).findById(post.getUser().getId());
        verify(postDao,never()).create(post);
        verify(postDao,never()).findById(post.getId());
    }

    /**
     * Unhappy flow with response because of lenght
     */
    @Test
    void create4(){
        post2.setMessage("Lorem Ipsum is slechts een proeftekst uit het drukkerij- en zetterijwezen. Lorem Ipsum is de standaard proeftekst in deze bedrijfstak sinds de 16e eeuw, toen een onbekende drukker een zethaak met letters nam en ze door elkaar husselde om een font-catalogus te maken. Het heeft niet alleen vijf eeuwen overleefd maar is ook, vrijwel onveranderd, overgenomen in elektronische letterzetting. Het is in de jaren '60 populair geworden met de introductie van Letraset vellen met Lorem Ipsum passages en meer recentelijk door desktop publishing software zoals Aldus PageMaker die versies van Lorem Ipsum bevatten.");

        when(userDao.findById(post.getUser().getId())).thenReturn(user2);
        when(postDao.create(post2)).thenReturn(post2);
        when(postDao.findById(post2.getResponseTo().getId())).thenReturn(post);

        assertNull(postService.create(post2));

        verify(userDao,never()).findById(post2.getUser().getId());
        verify(postDao,never()).create(post2);
        verify(postDao,atLeastOnce()).findById(post.getId());
    }

    /**
     * UnHappy flow with response because of invalid response message
     */
    @Test
    void create5(){
        when(userDao.findById(post.getUser().getId())).thenReturn(user2);
        when(postDao.create(post2)).thenReturn(post2);
        when(postDao.findById(post2.getResponseTo().getId())).thenReturn(null);

        assertNull(postService.create(post2));

        verify(userDao,never()).findById(post2.getUser().getId());
        verify(postDao,never()).create(post2);
        verify(postDao,atLeastOnce()).findById(post.getId());
    }

    /**
     * Happy flow delete
     */
    @Test
    void delete(){
        when(postDao.findById(post.getId())).thenReturn(post);
        postService.delete(post.getId());
        verify(postDao,atLeastOnce()).delete(post);
    }

    /**
     * unHappy flow delete can't find post
     */
    @Test
    void delete2(){
        when(postDao.findById(post.getId())).thenReturn(null);
        postService.delete(post.getId());
        verify(postDao,never()).delete(post);
    }

    @Test
    void postsFromUser(){
        List<Post> posts = new ArrayList<>();
        posts.add(post2);
        posts.add(post3);

        when(postDao.findByUser(user2.getId())).thenReturn(posts);
        when(userDao.findById(user2.getId())).thenReturn(user2);

        Assert.assertEquals(posts,postService.findByUser(user2.getId()));

        verify(userDao,atLeastOnce()).findById(user2.getId());
        verify(postDao,atLeastOnce()).findByUser(user2.getId());
    }



    @Test
    void postsFromUser1(){
        List<Post> posts = new ArrayList<>();
        posts.add(post2);
        posts.add(post3);

        when(postDao.findByUser(user2.getId())).thenReturn(posts);
        when(userDao.findById(user2.getId())).thenReturn(null);

        Assert.assertNull(postService.findByUser(user2.getId()));

        verify(userDao,atLeastOnce()).findById(user2.getId());
        verify(postDao,never()).findByUser(user2.getId());
    }

    @Test
    void findPostById(){
        when(postDao.findById(post.getId())).thenReturn(post);

        assertEquals(post,postService.findPostById(post.getId()));

        verify(postDao,atLeastOnce()).findById(post.getId());
    }


    @Test
    void findAll(){
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post2);
        posts.add(post3);

        when(postDao.findAll()).thenReturn(posts);

        Assert.assertEquals(posts,postService.findAll());

        verify(postDao,atLeastOnce()).findAll();
    }
}