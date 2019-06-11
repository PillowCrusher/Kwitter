package Service;

import DAO.IUserDao;
import DAO.JPA;
import Entity.Account;
import Entity.Role;
import Entity.User;
import Util.Auth;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
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

    public List<String> getPictures() {
        File folder = new File("C:\\Users\\Jeroen\\IdeaProjects\\Kwitter\\web\\resources\\images");
        File[] listOfFiles = folder.listFiles();
        List<String> links = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            links.add("assets/"+listOfFiles[i].getName());
        }
       return links;
    }

    public void addRole(Role role) {
        User user = userDao.findByEmail(role.getEmail());
        for(Role exsistingRole : user.getAccount().getRoles()) {
            if(exsistingRole.getRolename().equals(role.getRolename())){
                return;
            }
        }
        userDao.createRole(role);
        List<Role> currentRoles = userDao.getRoles(user.getAccount().getUserEmail());
        user.getAccount().setRoles(currentRoles);
        userDao.update(user);
    }

    public void setRoles(List<Role> roles, String email) {
        List<Role> toDelete = new ArrayList<>();
        List<Role> toAdd = new ArrayList<>();
        boolean alreadyAdded = false;
        List<Role> existingRoles = userDao.getRoles(email);
        for (Role role : existingRoles) {
            for (Role role1 : roles) {
                if (role.getRolename().equals(role1.getRolename())) {
                    alreadyAdded = true;
                    break;
                }
            }
            if (!alreadyAdded) {
                toDelete.add(role);
                alreadyAdded = false;
            }
        }
        alreadyAdded = false;
        for (Role role1 : roles) {
            for (Role role : existingRoles) {
                if (role.getRolename().equals(role1.getRolename())) {
                    alreadyAdded = true;
                    break;
                }
            }
            if (!alreadyAdded) {
                toAdd.add(role1);
                alreadyAdded = false;
            }
        }

        for(Role toDeleteRole : toDelete) {
            existingRoles.remove(toDeleteRole);
            userDao.removeRole(toDeleteRole);
        }

        for(Role toAddRole : toAdd) {
            existingRoles.add(toAddRole);
            userDao.createRole(toAddRole);
        }
        User user = userDao.findByEmail(email);
        user.getAccount().setRoles(existingRoles);
        userDao.update(user);
    }

    public List<Role> getRoles(String email) {
        return userDao.getRoles(email);
    }
}
