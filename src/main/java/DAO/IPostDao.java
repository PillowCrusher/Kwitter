package DAO;

import Entity.Post;
import Entity.User;

import java.util.List;

public interface IPostDao {
    Post findById(String id);
    List<Post> findByMessage(String message);
    List<Post> findByUser(String id);
    List<Post> findForUser(User user);
    List<Post> findAll();

    Post create(Post entity);
    void delete(Post entity);
    void deleteById(String id);
}
