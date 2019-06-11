package DAO;

import Entity.Account;
import Entity.Role;
import Entity.User;

import java.util.List;

public interface IUserDao {
    User findById(String id);

    User findByEmail(String email);

    List<User> findByUsername(String username);

    List<User> findAll();

    User create(User entity);

    User update(User entity);

    void delete(User entity);

    Account findByAcc(Account account);

    User findUserByAcc(Account account);

    long checkUniqueEmail(String email);

    long checkAccount(Account account);

    List<User> getFollowing(User user);

    List<User> getFollowedBy(User user);

    void createRole(Role role);

    List<Role> getRoles(String email);

    void removeRole(Role role);
}
