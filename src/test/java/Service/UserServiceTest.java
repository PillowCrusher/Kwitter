package Service;

import DAO.Facade.UserCDAImpl;
import Entity.Account;
import Entity.User;
import Util.Auth;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    public UserService userService;
    @Mock
    public UserCDAImpl userDao;

    private Account account;
    private User user;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        account = new Account("jeroen@jeroen.jeroen","wachtw","user");
        user = new User("Jan","assets/danger_birb.png","Test test test",account);
    }

    @Test
    public void TestMock(){
        assertNotNull(userService);
        assertNotNull(userDao);
    }

    private String convertToHashed(String string){
        try {
            return Auth.encodeSHA256(string);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Happy flow authenticating User
     */
    @Test
    void login1(){
        when(userDao.checkAccount(account)).thenReturn(1L);
        when(userDao.findUserByAcc(account)).thenReturn(user);

        Assert.assertEquals(user,userService.authenticate(account));

        verify(userDao,atLeastOnce()).checkAccount(account);
        verify(userDao,atLeastOnce()).findUserByAcc(account);
    }

    /**
     * Unhappy flow authenticating User
     * Fail on acc not account not existing
     */
    @Test
    void login2(){
        when(userDao.checkAccount(account)).thenReturn(0L);
        when(userDao.findUserByAcc(account)).thenReturn(user);

        Assert.assertNull(userService.authenticate(account));

        verify(userDao,atLeastOnce()).checkAccount(account);
        verify(userDao,never()).findUserByAcc(account);


    }

    /**
     * Unhappy flow authenticating User
     * Fail on acc not account not being linked to user (somehow)
     */
    @Test
    void login3(){
        when(userDao.checkAccount(account)).thenReturn(1L);
        when(userDao.findUserByAcc(account)).thenReturn(null);

        Assert.assertNull(userService.authenticate(account));

        verify(userDao,atLeastOnce()).checkAccount(account);
        verify(userDao,atLeastOnce()).findUserByAcc(account);
    }

    /**
     * Happy flow registering User
     * Succes
     */
    @Test
    void register(){
        when(userDao.checkUniqueEmail(account.getUserEmail())).thenReturn(0L);
        when(userDao.create(user)).thenReturn(user);

        Assert.assertEquals(convertToHashed("wachtw")
                ,userService.create(user).getAccount().getPass());

        verify(userDao,atLeastOnce()).checkUniqueEmail(user.getAccount().getUserEmail());
        verify(userDao,atLeastOnce()).create(user);
    }

    /***
     * Unhappy flow registering user
     * Email already exsists
     */
    @Test
    void register1(){
        when(userDao.checkUniqueEmail(account.getUserEmail())).thenReturn(1L);
        when(userDao.create(user)).thenReturn(user);

        Assert.assertNull(userService.create(user));

        verify(userDao,atLeastOnce()).checkUniqueEmail(user.getAccount().getUserEmail());
        verify(userDao,never()).create(user);
    }

    /***
     * Unhappy flow registering user
     * Name too long
     */
    @Test
    void register2(){
        user.setName("morecharsthan14test");
        when(userDao.checkUniqueEmail(account.getUserEmail())).thenReturn(0L);
        when(userDao.create(user)).thenReturn(user);

        Assert.assertNull(userService.create(user));

        verify(userDao,never()).checkUniqueEmail(user.getAccount().getUserEmail());
        verify(userDao,never()).create(user);
    }

    /***
     * Unhappy flow registering user
     * Name too long
     */
    @Test
    void register3(){
        user.setProfilePicture("morethan100chars Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla condimentum tempus congue. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Phasellus lobortis, nulla ac mollis ornare, erat libero tincidunt quam, quis varius nulla metus id sem. Nullam non ipsum hendrerit, dapibus nibh sit amet, pretium.");
        when(userDao.checkUniqueEmail(account.getUserEmail())).thenReturn(0L);
        when(userDao.create(user)).thenReturn(user);

        Assert.assertNull(userService.create(user));

        verify(userDao,never()).checkUniqueEmail(user.getAccount().getUserEmail());
        verify(userDao,never()).create(user);
    }
    /***
     * Unhappy flow registering user
     * Profile details too long
     */
    @Test
    void register4(){
        user.setProfileDetails("morethan100chars Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla condimentum tempus congue. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Phasellus lobortis, nulla ac mollis ornare, erat libero tincidunt quam, quis varius nulla metus id sem. Nullam non ipsum hendrerit, dapibus nibh sit amet, pretium.");

        when(userDao.checkUniqueEmail(account.getUserEmail())).thenReturn(0L);
        when(userDao.create(user)).thenReturn(user);

        Assert.assertNull(userService.create(user));

        verify(userDao,never()).checkUniqueEmail(user.getAccount().getUserEmail());
        verify(userDao,never()).create(user);
    }

    /**
     * Happy flow delete user
     *
     */
    @Test
    void delete(){
        when(userDao.findById(user.getId())).thenReturn(user);

        userService.delete(user);

        verify(userDao,atLeastOnce()).delete(user);
    }

    /**
     * Happy flow delete user alternative
     *
     */
    @Test
    void deleteAlternative(){
        when(userDao.findById(user.getId())).thenReturn(user);

        userService.deleteById(user.getId());

        verify(userDao,atLeastOnce()).delete(user);
    }


    /**
     * Unhappy flow delete user
     * User not found
     */
    @Test
    void delete1(){
        when(userDao.findById(user.getId())).thenReturn(null);

        userService.delete(user);

        verify(userDao,never()).delete(user);
    }

    /**
     * Happy flow update user profledetails
     */
    @Test
    void update() {
        User user1 = new User();
        user1.setId(user.getId());
        user1.setName(user.getName());
        user1.setProfileDetails(user.getProfileDetails());
        user1.setProfilePicture(user.getProfilePicture());
        user1.setAccount(user1.getAccount());
        user1.setFollowedUsers(user1.getFollowedUsers());

        when(userDao.findById(user.getId())).thenReturn(user);

        user1.setProfileDetails(user.getProfileDetails() + "updated");

        userService.update(user1);

        verify(userDao,atLeastOnce()).update(user1);
    }

    /**
     * Happy flow update user profile picture
     */
    @Test
    void update1() {
        User user1 = new User();
        user1.setId(user.getId());
        user1.setName(user.getName());
        user1.setProfileDetails(user.getProfileDetails());
        user1.setProfilePicture(user.getProfilePicture());
        user1.setAccount(user.getAccount());
        user1.setFollowedUsers(user1.getFollowedUsers());

        when(userDao.findById(user.getId())).thenReturn(user);

        user1.setProfilePicture(user.getProfilePicture() + "updated");

        userService.update(user1);

        verify(userDao,atLeastOnce()).update(user1);
    }

    /**
     * Happy flow update user naam
     */
    @Test
    void update3() {
        User user1 = new User();
        user1.setId(user.getId());
        user1.setName(user.getName());
        user1.setProfileDetails(user.getProfileDetails());
        user1.setProfilePicture(user.getProfilePicture());
        user1.setAccount(user.getAccount());
        user1.setFollowedUsers(user1.getFollowedUsers());

        when(userDao.findById(user.getId())).thenReturn(user);

        user1.setName(user.getName() + "updated");

        userService.update(user1);

        verify(userDao,atLeastOnce()).update(user1);
    }

    /**
     * Unhappy flow update user
     * No updates
     */
    @Test
    void update4() {
        User user1 = new User();
        user1.setId(user.getId());
        user1.setName(user.getName());
        user1.setProfileDetails(user.getProfileDetails());
        user1.setProfilePicture(user.getProfilePicture());
        user1.setAccount(user.getAccount());
        user1.setFollowedUsers(user1.getFollowedUsers());

        when(userDao.findById(user.getId())).thenReturn(user);

        userService.update(user1);

        verify(userDao,never()).update(user1);
    }

    /**
     * Happy flow get following
     */
    @Test
    void getFollowing(){
        Account acc1 = new Account("test@test.test","testtest");
        User user2 = new User("test","","",acc1);

        userService.addFollow(user,user2);

        List<User> userList = new ArrayList<>();
        userList.add(user2);

        when(userDao.findById(user.getId())).thenReturn(user);
        when(userDao.getFollowing(user)).thenReturn(userList);

        Assert.assertEquals(userList,userService.getFollows(user));

        verify(userDao,atLeastOnce()).findById(user.getId());
        verify(userDao,atLeastOnce()).getFollowing(user);

    }

    /**
     * Unhappy flow get following
     * Can't find user
     */
    @Test
    void getFollowing1(){
        Account acc1 = new Account("test@test.test","testtest");
        User user2 = new User("test","","",acc1);

        userService.addFollow(user,user2);

        List<User> userList = new ArrayList<>();
        userList.add(user2);

        when(userDao.findById(user.getId())).thenReturn(null);
        when(userDao.getFollowing(user)).thenReturn(userList);

        Assert.assertNull(userService.getFollows(user));

        verify(userDao,atLeastOnce()).findById(user.getId());
        verify(userDao,never()).getFollowing(user);
    }

    /**
     * Happy flow get followers
     */
    @Test
    void getFollowers(){
        Account acc1 = new Account("test@test.test","testtest");
        User user2 = new User("test","","",acc1);

        userService.addFollow(user2,user);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(userDao.findById(user.getId())).thenReturn(user);
        when(userDao.getFollowedBy(user)).thenReturn(userList);

        Assert.assertEquals(userList,userService.getFollowers(user));

        verify(userDao,atLeastOnce()).findById(user.getId());
        verify(userDao,atLeastOnce()).getFollowedBy(user);
    }

    /**
     * Happy flow get followers
     */
    @Test
    void getFollowers1(){
        Account acc1 = new Account("test@test.test","testtest");
        User user2 = new User("test","","",acc1);

        userService.addFollow(user2,user);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(userDao.findById(user.getId())).thenReturn(null);
        when(userDao.getFollowedBy(user)).thenReturn(userList);

        Assert.assertNull(userService.getFollowers(user));

        verify(userDao,atLeastOnce()).findById(user.getId());
        verify(userDao,never()).getFollowedBy(user);
    }
}