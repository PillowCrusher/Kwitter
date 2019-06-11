package Service;

import DAO.IPostDao;
import DAO.IUserDao;
import DAO.JPA;
import Entity.Post;
import Entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Stateless
public class PostService  implements Serializable {

    @Inject @JPA
    private IPostDao postDao;

    @Inject @JPA
    private IUserDao userDao;

    public PostService(){
        super();
    }

    public Post create(Post post) {
        if(post.getResponseTo()!=null){
            Post responseTo = findPostById(post.getResponseTo().getId());
            if(responseTo==null){
                return null;
            }
        }
        if(post.getMessage().length()>250){
            return null;
        }
        User user = userDao.findById(post.getUser().getId());
        post.setUser(user);
        if(user!=null) {
            post = postDao.create(post);
            return post;
        }
        return null;
    }
    public void delete(String id) {
        Post post = postDao.findById(id);
        if (post != null) {
            postDao.delete(post);
        }
    }
    public void update(Post post) {

    }
    public List<Post> findByUser(String id){
        User user = userDao.findById(id);
        if(user!=null){
           return postDao.findByUser(user.getId());
        }
        else {
            return null;
        }
    }
    public List<Post> findForUser(User user){
        return postDao.findForUser(user);
    }


    public Post findPostById(String id){
        return postDao.findById(id);
    }
    public List<Post> findAll(){
        return postDao.findAll();
    }
    public List<Post> findPostByMessage(String message) {
       return postDao.findByMessage(message);
    }
}
