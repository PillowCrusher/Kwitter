package DAO.Facade;

import DAO.IPostDao;
import Entity.Post;
import Entity.User;

import javax.ejb.Stateful;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Default
@Stateful
public class PostCDAImpl implements IPostDao {

    private final CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();

    private String getId()
    {
       return UUID.randomUUID().toString();
    }

    public Post findById(String id) {
        for(Post post : posts)
        {
            if(post.getId().equals(id))
            {
                return post;
            }
        }
        return null;
    }

    public List<Post> findByMessage(String message) {
       List<Post> sameMessagePosts = new ArrayList<>();
        for(Post post : posts)
       {
           if(post.getMessage().equals(message))
           {
               sameMessagePosts.add(post);
           }
       }
        return sameMessagePosts;
    }

    public List<Post> findByUser(String id) {
        List<Post> postsFound = new ArrayList<>();
        for(Post post : posts)
        {
           // if(post.getUser().getId().equals(id))
            //{
              //  postsFound.add(post);
            //}
        }
    return postsFound;
    }

    public List<Post> findForUser(User user) {
        List<Post> postsFound = new ArrayList<>();
        for (Post post : posts) {
            if (post.getUser().equals(user)) {
                postsFound.add(post);
            }
        }
        return postsFound;
    }

    public List<Post> findAll() {
       return posts;
    }

    public Post create(Post entity) {
        entity.setId(getId());
        posts.add(entity);
        return entity;
    }

    public void delete(Post entity) {
       Post toDelete= null;
        for(Post post : posts) {
            if (post.equals(entity)) {
                toDelete = post;
                break;
            }
        }
        if(toDelete!=null)
        {
            posts.remove(toDelete);
        }
    }

    public void deleteById(String id) {
        Post toDelete= null;
        for(Post post : posts) {
            if (post.getId().equals(id)) {
                toDelete = post;
                break;
            }
        }
        if(toDelete!=null)
        {
            posts.remove(toDelete);
        }
    }
}
