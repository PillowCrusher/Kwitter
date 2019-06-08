package Service;

import DAO.IUserDao;
import DAO.JPA;
import Entity.Account;
import Entity.User;
import Util.Auth;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Stateless
public class UserService implements Serializable {
    @Inject
    @JPA
    private IUserDao userDao;
    
    public UserService(){
        super();
    }

    private String convertToHashed(String string){
        try {
            return Auth.encodeSHA256(string);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public User create(User user){
        if(user.getName().equals("")||user.getName().length()>14){
            return null;
        }
        if(user.getProfilePicture().length()>100){
            return null;
        }
        if(user.getProfileDetails().length()>150){
            return null;
        }
        long doesUserExist = userDao.checkUniqueEmail(user.getAccount().getUserEmail());
        if(doesUserExist == 0L) {
            user.getAccount().setPass(convertToHashed(user.getAccount().getPass()));
            return userDao.create(user);
        }
        return null;
    }

    public void delete(User user){
        User foundUser = userDao.findById(user.getId());
        if(foundUser!=null&&user==foundUser){
            userDao.delete(foundUser);
        }
    }
    public void deleteById(String id){
        User foundUser = userDao.findById(id);
        if(foundUser!=null){
            userDao.delete(foundUser);
        }
    }
    public void update(User user)  {
        User foundUser = userDao.findById(user.getId());
        if(foundUser!=null){
            if(!foundUser.getAccount().equals(user.getAccount())||!foundUser.equals(user)){
                userDao.update(user);
            }
        }
    }
    public User authenticate(Account account){
        account.setPass(convertToHashed(account.getPass()));
        String a = convertToHashed(account.getPass());
        if(userDao.checkAccount(account)!=0L){
           return userDao.findUserByAcc(account);
        }
        else {
            return null;
        }
    }


    public User removeFollow(User follower, User toFollow){
        boolean alreadyFollowing = false;
        User reference = toFollow;
        if(findById(follower.getId())==null||findById(toFollow.getId())==null)
        {
            return null;
        }
        List<User> followers = getFollows(follower);
        for(User inFollowers: followers){
            if(inFollowers.getId().equals(toFollow.getId())){
               alreadyFollowing = true;
               reference = inFollowers;

               break;
            }
        }
        if(!alreadyFollowing){
            return null;
        }
        follower.getFollowedUsers().remove(reference);
        return userDao.update(follower);
    }

    public User addFollow(User follower, User toFollow){
        if(findById(follower.getId())==null||findById(toFollow.getId())==null)
        {
            return null;
        }
        List<User> followers = getFollows(follower);
        for(User inFollowers: followers){
            if(inFollowers.getId().equals(toFollow.getId())){
                return null;
            }
        }
        follower.getFollowedUsers().add(toFollow);
        return userDao.update(follower);
    }

    public List<User> getFollows(User user){
        if(findById(user.getId())!=null){
            return userDao.getFollowing(user);
        }
        return null;
    }

    public List<User> getFollowers(User user){
        if(findById(user.getId())!=null){
            return userDao.getFollowedBy(user);
        }
        return null;
    }


    public List<User> findByUsername(String username){
        return userDao.findByUsername(username);
    }
    public User findById(String id){
       return userDao.findById(id);
    }
    public User findByUser(User user){
        return userDao.findById(user.getId());
    }
    public User findByEmail(String email){
        return userDao.findByEmail(email);
    }
    public List<User> findAll(){
        return userDao.findAll();
    }
}
