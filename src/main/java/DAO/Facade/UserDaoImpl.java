    package DAO.Facade;

    import DAO.IUserDao;
import DAO.JPA;
import Entity.Account;
import Entity.Role;
import Entity.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
    import javax.persistence.PersistenceContext;
    import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JPA
@Stateful
public class UserDaoImpl implements IUserDao {


    @PersistenceContext(unitName = "kwetterPU")
    //@PersistenceContext(unitName = "kwetterTestsPU")
    private EntityManager entityManager;

    public UserDaoImpl() {
        super();
    }

    //This is done for testing purposes, I have no idea how to otherwise get this class to use the test persistence.
    public void setEntityManager(EntityManager em){
        entityManager = em;
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }

    public User findById(String id) {
        try {
            return (User) entityManager.createNamedQuery("User.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        try {
            return (User) entityManager.createNamedQuery("User.findByEmail")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<User> findByUsername(String username) {
        if(username.equals(""))
            return null;
        return (List<User>) entityManager.createNamedQuery("User.findByUsername")
                .setParameter("username", username+"%")
                .setMaxResults(5)
                .getResultList();
    }

    public List<User> findAll() {
        return (List<User>) entityManager.createNamedQuery("User.findAll")
                .getResultList();
    }

    public User create(User entity) {
        entityManager.persist(entity.getAccount());
        entity.setId(getId());
        entityManager.persist(entity);
        return entity;
    }

    public User update(User entity) {
        return entityManager.merge(entity);
    }

    public void delete(User entity) {
        entityManager.remove(entity);
    }

    public void deleteById(String id) {
        entityManager.createNamedQuery("User.deleteById")
                .setParameter("id", id);
    }

    public Account findByAcc(Account account) {
        try {
            return (Account) entityManager.createNamedQuery("Account.findByAcc")
                    .setParameter("email",account.getUserEmail())
                    .setParameter("pass",account.getPass())
                    .getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }

    }

    public User findUserByAcc(Account account) {
        try {
            return (User) entityManager.createNamedQuery("User.findByAcc")
                    .setParameter("email", account.getUserEmail())
                    .setParameter("pass", account.getPass())
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public long checkUniqueEmail(String email) {
        try {
            return (long) entityManager.createNamedQuery("User.checkUniqueEmail")
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (NoResultException e) {
            return 0;
        }
    }


    public long checkAccount(Account account) {
        try {
            return (long) entityManager.createNamedQuery("Account.checkUniqueId")
                    .setParameter("email", account.getUserEmail())
                    .setParameter("pass", account.getPass())
                    .getSingleResult();

        } catch (NoResultException e) {
            return 0;
        }
    }


    public List<User> getFollowing(User user) {
        User foundUser = entityManager.find(User.class,user.getId());
        return foundUser.getFollowedUsers();
    }


    public List<User> getFollowedBy(User user) {
        User foundUser = entityManager.find(User.class, user.getId());
        List<User> users = findAll();
        List<User> followedByUsers = new ArrayList<>();
        for(User somethingUser : users){
            User listFoundUser = entityManager.find(User.class, somethingUser.getId());
            for(User a : listFoundUser.getFollowedUsers()){
                if(a.getId().equals(foundUser.getId())){
                    followedByUsers.add(listFoundUser);
                    break;
                }
            }
        }
        return followedByUsers;
    }

    public void createRole(Role role) {
        entityManager.persist(role);
    }

    public List<Role> getRoles(String email) {
        return  entityManager.createNamedQuery("kwetterRole.getRole")
                .setParameter("email",email)
                .getResultList();
    }

    @Override
    public void removeRole(Role role) {
        entityManager.remove(role);
    }
}
