package Beans;

import Entity.Post;
import Service.PostService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named
public class ModeratorPanelBean implements Serializable {

    @Inject
    private PostService postService;

    private List<Post> postsList;

    private Post toEdit;

    public List getGetPostList() {
        postsList = postService.findAll();
        return this.postsList;
    }

    public String editPost(Post post) {
        this.toEdit = post;
        return "edit";
    }

    public String savePost(Post post) {
        postService.update(post);
        return "save";
    }
}

