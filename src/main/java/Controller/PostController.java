package Controller;

import Controller.Auth.Secured;
import Entity.Post;
import Entity.User;
import Service.PostService;
import Service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Stateless
@Path("/post")
@Produces({MediaType.APPLICATION_JSON})
public class PostController {
    @Resource
    private SessionContext context;
    @Inject
    private UserService userService;
    @Inject
    private PostService postService;

    @Secured
    @GET
    public Response getAllPosts(){
       List<Post> posts = postService.findAll();
        Collections.reverse(posts);
        return Response.ok(posts).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getSingularPostById(@PathParam("id") String id) {
        Post post = postService.findPostById(id);
        if (post != null) {
            return Response.ok(post).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Secured
    @GET
    @Path("/message")
    public Response searchByPost(@QueryParam("message") String message){
        List<Post> posts = postService.findPostByMessage(message);
        return Response.ok(posts).build();
    }

    @Secured
    @GET
    @Path("/user")
    public Response getPostsFromUser(@QueryParam("id") String id) {
        User user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(postService.findByUser(user.getId())).build();
    }

    @Secured
    @GET
    @Path("/for")
    public Response getPostsForUser(@QueryParam("id") String id){
        User user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(postService.findForUser(user)).build();
    }

    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPost(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Post post;
        try {
            post = objectMapper.readValue(json, Post.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User user = userService.findById(post.getUser().getId());
        if (user == null||post == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post createdPost = postService.create(post);
        return Response.ok(createdPost).build();
    }

    @Secured
    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("id") String id,String json){
        //TODO really check this, this is spicy but I like it
        ObjectMapper objectMapper = new ObjectMapper();
        User user =null;
        try{
            user = objectMapper.readValue(json,User.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if(user==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = postService.findPostById(id);
        if(post==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        postService.delete(id);
        return Response.ok().build();
    }
}
