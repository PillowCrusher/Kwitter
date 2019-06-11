package DAO.Facade;

import DAO.IUserDao;
import Entity.Account;
import Entity.Role;
import Entity.User;

import javax.ejb.Stateful;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Default
@Stateful
public class UserCDAImpl implements IUserDao {

    private final CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();

    private String getId()
    {
        return UUID.randomUUID().toString();
    }

    public User findById(String id) {
        User foundUser = null;
        for (User user : users) {
            if (user.getId().equals(id)) {
                foundUser = user;
                break;
            }
        }
        if (foundUser != null) {
            return foundUser;
        } else {
            return null;
        }
    }

    private User findByUser(User entity){
        for(User user : users){
            if(user.getId().equals(entity.getId()))
            {
                return user;
            }
        }
        return null;
    }

    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getAccount().getUserEmail().equals(email)) {
               return user;
            }
        }
        return null;
    }

    public List<User> findByUsername(String username) {
        ArrayList<User> foundUsers = null;
        for (User user : users) {
            if (user.getName().equals(username)) {
                foundUsers.add(user);
            }
        }
      return foundUsers;
    }

    public List<User> findAll() {
       return users;
    }

    public User create(User entity) {
        entity.setId(getId());
        users.add(entity);
        return entity;
    }

    public User update(User entity) {
        User foundUser = findById(entity.getId());
        if (foundUser != null) {
            users.remove(foundUser);
            users.add(entity);
            return entity;
        } else {
            return null;
        }
    }

    public void delete(User entity) {
        User foundUser = findByUser(entity);
        if (foundUser != null) {
            users.remove(foundUser);
        }
    }

    public void deleteById(String id) {
        User foundUser = findById(id);
        if (foundUser != null) {
            users.remove(foundUser);
        }
    }

    public Account findByAcc(Account account) {
            for(User user : users){
                if(user.getAccount().equals(account)){
                    return user.getAccount();
                }
            }
        return null;
    }


    public User findUserByAcc(Account account) {
        for(User user : users){
            if(user.getAccount().equals(account)){
                return user;
            }
        }
        return null;
    }

    public long checkUniqueEmail(String email) {
        if(findByEmail(email)==null){
            return 0L;
        }
        return 1L;
    }


    public long checkAccount(Account account) {
        long count = 0L;

        for(User user : users){
            if(user.getAccount().equals(account)){
                count++;
            }
        }
        return count;
    }

    @Override
    public List<User> getFollowing(User user) {
        for(User user1 : users){
            if(user1.getId().equals(user.getId())){
                return user1.getFollowedUsers();
            }
        }
        return null;
    }

    @Override
    public List<User> getFollowedBy(User user) {
       List<User> followedBy = new ArrayList<>();
        for(User user1 : users){
            if(!user1.getId().equals(user.getId())){
                for(User user2 : user1.getFollowedUsers()){
                    if(user2.getId().equals(user.getId())){
                        followedBy.add(user2);
                    }
                }
            }
        }
       return followedBy;
    }

    @Override
    public void createRole(Role role) {

    }

    @Override
    public List<Role> getRoles(String email) {
        return null;
    }

    @Override
    public void removeRole(Role role) {

    }
}
