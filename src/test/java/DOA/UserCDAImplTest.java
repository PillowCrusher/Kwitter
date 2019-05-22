package DOA;

import org.junit.Test;
import DAO.Facade.UserCDAImpl;
import Entity.Account;
import Entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;


import java.util.ArrayList;
import java.util.List;

class UserCDAImplTest {

    private UserCDAImpl userCDA = new UserCDAImpl();

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("test@test.nl","testpassword");
    }

    @Test
    void findById() {
        User user = userCDA.create(new User("Jan","birb","Geweldig persoon ofzo",account));
        Assert.assertEquals(user,userCDA.findById(user.getId()));
    }

    @Test
    void findByEmail() {
        User user = userCDA.create(new User("Jan","birb","Geweldig persoon ofzo",account));
        Assert.assertEquals(user,userCDA.findByEmail("test@test.nl"));
        Assert.assertNull(userCDA.findByEmail("onzin@onzin.nl"));
    }

    @Test
    void findByUsername() {
        User user = userCDA.create(new User("Jan","birb","Geweldig persoon ofzo",account));
        Assert.assertEquals(user,userCDA.findByUsername("Jan"));
        Assert.assertNull(userCDA.findByEmail("Henk"));
    }

    @Test
    void findAll() {
        Assert.assertEquals(0,userCDA.findAll().size());
        Account account1 = new Account("test2@test.nl","testpassword");

        List foundUsers = new ArrayList<>();
        foundUsers.add(userCDA.create(new User("Jan","birb","Geweldig persoon ofzo",account)));
        foundUsers.add(userCDA.create(new User("Henk","birb","Geweldig persoon ofzo",account1)));

        Assert.assertArrayEquals(foundUsers.toArray(),userCDA.findAll().toArray());
    }

    @Test
    void create() {
        User user = new User("Jan","birb","Geweldig persoon ofzo",account);
        Assert.assertNull(user.getId());
        Assert.assertNotNull((userCDA.create(user)).getId());
    }

    @Test
    void update() {
        User user = new User("Jan","birb","Geweldig persoon ofzo",account);
        Assert.assertNull(user.getId());
        Assert.assertNotNull((userCDA.create(user)).getId());
        User user1 = new User("Jan","birb","Geweldig persoon ofzo",account);
        user1.setId(user.getId());
        user1.setProfileDetails("Geweldige jongen!");
        userCDA.update(user1);
        Assert.assertNotEquals(user,userCDA.findById(user1.getId()));
    }

    @Test
    void delete() {
        User user = new User("Tim","birb","Geweldig persoon ofzo",account);
        user = userCDA.create(user);
        Assert.assertNotNull(userCDA.findById(user.getId()));
        userCDA.delete(user);
        Assert.assertNull(userCDA.findById(user.getId()));
    }

    @Test
    void deleteById() {
        User user = new User("Jan","birb","Geweldig persoon ofzo",account);
        user = userCDA.create(user);
        Assert.assertNotNull(userCDA.findById(user.getId()));
        userCDA.deleteById(user.getId());
        Assert.assertNull(userCDA.findById(user.getId()));
    }
}