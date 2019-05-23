package DOA;

import DAO.Facade.UserDaoImpl;
import Entity.Account;
import Entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

class UserDaoImplTest {

    private EntityManager em;
    private Account account;
    private User user;

    private UserDaoImpl userDao;
    private Account account1;
    private Account account2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
         EntityManagerFactory entityManager = Persistence.createEntityManagerFactory("kwetterTestsPU");
         em = entityManager.createEntityManager();
         em.getTransaction().begin();

         account = new Account("Jeroen1@peters.nl","wachtw");
         account1 = new Account("Jeroen2@peters.nl","wachtw");
         account2 = new Account("Jeroen3@peters.nl","wachtw");

         user = new User("Jeroen","birb","Maker van dit project yo",account);
        user1 = new User("Henk","birb","Maker van dit project yo",account1);
        user2 = new User("Jero","birb","Maker van dit project yo",account2);

         userDao = new UserDaoImpl();
         userDao.setEntityManager(em);
    }

    @AfterEach
    void tearDown() {
        em.flush();
        em.getTransaction().commit();
    }

    @Test
    void findById() {
        //Arrange
        User created = userDao.create(user);
        //Act
        User returnUser = userDao.findById(created.getId());
        //Assert
        Assert.assertEquals(created,returnUser);
    }

    @Test
    void findById1() {
        //Arrange
        User created = userDao.create(user);
        //Act
        User returnUser = userDao.findById(created.getId()+"sdaf");
        //Assert
        Assert.assertNull(returnUser);
    }


    @Test
    void findByEmail() {
        //Arrange
        User created = userDao.create(user);
        //Act
        User returnUser = userDao.findByEmail(created.getAccount().getUserEmail());
        //Assert
        Assert.assertEquals(created,returnUser);
    }

    @Test
    void findByEmail1() {
        //Arrange
        User created = userDao.create(user);
        //Act
        User returnUser = userDao.findByEmail(created.getAccount().getUserEmail()+"asdfasdfasdf");
        //Assert
        Assert.assertNull(returnUser);
    }


    @Test
    void findByUsername() {
        //Arrange
        User created = userDao.create(user);
        User created1 = userDao.create(user1);
        User created2 = userDao.create(user2);

        List<User> users = new ArrayList<>();
        users.add(created);
        users.add(created1);
        users.add(created2);

        //Act
        List<User> foundUsers = userDao.findByUsername(created.getName());
        //Assert
        Assert.assertNotEquals(users,foundUsers);
    }

    @Test
    void findByUsername1() {
        //Arrange
        User created = userDao.create(user);
        User created1 = userDao.create(user1);
        User created2 = userDao.create(user2);

        List<User> users = new ArrayList<>();
        users.add(created);
        users.add(created2);

        //Act
        List<User> foundUsers = userDao.findByUsername(created2.getName());
        //Assert
        Assert.assertEquals(users,foundUsers);
    }


    @Test
    void findAll() {
        //Arrange
        User created = userDao.create(user);
        User created1 = userDao.create(user1);
        User created2 = userDao.create(user2);

        List<User> users = new ArrayList<>();
        users.add(created);
        users.add(created1);
        users.add(created2);

        //Act
        List<User> foundUsers = userDao.findAll();
        //Assert
        Assert.assertEquals(users,foundUsers);
    }

    @Test
    void create() {
        //Arrange
        String id = user.getId();

        //Act
       User created = userDao.create(user);
        //Assert
       User returnUser = em.createNamedQuery("User.findById",User.class)
               .setParameter("id",created.getId())
               .getSingleResult();
        Assert.assertNotEquals(returnUser.getId(),id);
        user.setId(returnUser.getId());
        Assert.assertEquals(returnUser,user);
    }

    @Test
    void create1() {
        //Arrange
        String id = user.getId();
        user.setAccount(null);
        //Act  //Assert
        Assertions.assertThrows(IllegalArgumentException.class,() -> userDao.create(user));

    }


    @Test
    void update() {
        //Arrange
        User created = userDao.create(user);
        user2.setAccount(account);
        user2.setId(created.getId());
        //Act
        User foundUser =  userDao.update(user2);
        //Assert
        Assert.assertEquals(user2,foundUser);
    }

    @Test
    void delete() {
        //Arrange
        User created = userDao.create(user);
        //Act
        Assert.assertNotNull(userDao.findById(created.getId()));
        //Assert
        userDao.delete(created);
        Assert.assertNull(userDao.findById(created.getId()));
    }



    @Test
    void findByAcc() {
        //Arrange
        User created = userDao.create(user);
        //Act
        Assert.assertNotNull(userDao.findByAcc(created.getAccount()));
        //Assert
    }

    @Test
    void findUserByAcc() {
        //Arrange
        User created = userDao.create(user);
        //Act
        Assert.assertNotNull(userDao.findUserByAcc(created.getAccount()));
        //Assert
    }

    @Test
    void checkUniqueEmail() {
        //Arrange
        Assert.assertEquals(0L,userDao.checkUniqueEmail(user.getAccount().getUserEmail()));
        User created = userDao.create(user);
        //Act
        Assert.assertEquals(1L,userDao.checkUniqueEmail(created.getAccount().getUserEmail()));
        //Assert
    }


    @Test
    void getFollowing() {
        //Arrange
        userDao.create(user);
        userDao.create(user2);
        user.getFollowedUsers().add(user2);
        userDao.update(user);
        List<User> followingUsers = new ArrayList<>();
        followingUsers.add(user2);
        //Act
        Assert.assertEquals(followingUsers,userDao.getFollowing(user));
        //Assert
    }

    @Test
    void getFollowedBy() {
        //Arrange
        userDao.create(user);
        userDao.create(user2);
        userDao.create(user1);
        user1.getFollowedUsers().add(user2);
        user.getFollowedUsers().add(user2);
        userDao.update(user);
        userDao.update(user1);
        List<User> followedUsers = new ArrayList<>();
        followedUsers.add(user);
        followedUsers.add(user1);
        //Act
        Assert.assertEquals(followedUsers,userDao.getFollowedBy(user2));
        //Assert
    }
}