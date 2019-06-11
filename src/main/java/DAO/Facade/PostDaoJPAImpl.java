package DAO.Facade;

import DAO.IPostDao;
import DAO.JPA;
import Entity.Post;
import Entity.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateful
@JPA
public class PostDaoJPAImpl implements IPostDao {

    @PersistenceContext(unitName = "kwetterPU")
    private EntityManager entityManager;


    /**
     * Required to being able to test this this.
     *
     * @param em
     */
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }

    public Post findById(String id) {
        try {
            return (Post) entityManager.createNamedQuery("Post.getById")
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }


    public List<Post> findByMessage(String message) {
        return (List<Post>) entityManager.createNamedQuery("Post.findByMessage")
                .setParameter("message", message)
                .getResultList();
    }

    public List<Post> findByUser(String id) {
        return (List<Post>) entityManager.createNamedQuery("Post.findByUser")
                .setParameter("userId", id)
                .getResultList();
    }


    public List<Post> findAll() {
        return (List<Post>) entityManager.createNamedQuery("Post.findAll")
                .getResultList();
    }

    public Post create(Post entity) {
        entityManager.merge(entity.getUser());
        entity.setId(getId());
        entityManager.persist(entity);
        return entity;
    }

    public void delete(Post entity) {
        List<Post> posts = findAll();
        for (Post post : posts) {
            if (post.getResponseTo() == entity) {
                post.setResponseTo(null);
            }
        }
        entityManager.merge(entity);
        entityManager.remove(entity);
    }

    @Override
    public void update(Post entity) {
        entityManager.merge(entity);
    }

    public void deleteById(String id) {
        Post post = findById(id);
        delete(post);
    }

    public List<Post> findForUser(User user) {
        List<Post> returnPosts = new ArrayList<>();
        List<Post> posts = findAll();
        List<User> followedUsers = user.getFollowedUsers();
        for (Post post : posts) {
            if (post.getUser().getId().equals(user.getId())) {
                returnPosts.add(post);
            } else {
                for (User followedUser : followedUsers) {
                    if (post.getUser().getId().equals(followedUser.getId())) {
                        returnPosts.add(post);
                        break;
                    }
                }
            }

        }
        return returnPosts;
    }
}
