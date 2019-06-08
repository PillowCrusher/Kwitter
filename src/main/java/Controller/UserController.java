package Controller;

import Controller.Auth.Secured;
import Entity.User;
import Service.PostService;
import Service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Stateless
@Path("/user")
@Produces({MediaType.APPLICATION_JSON})
public class UserController {
    @Resource
    private SessionContext context;
    @Inject
    private UserService userService;
    @Inject
    private PostService postService;

    private User convertUser(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try{
            user = objectMapper.readValue(json,User.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
    @Secured
    @GET
    public Response get() {
        return Response.ok(userService.findAll()).build();
    }

    @Secured
    @GET
    @Path("/id/post")
    public Response getPostsByUserId(@QueryParam("id") String id) {
       User user = userService.findById(id);
       if(user==null){
           return Response.status(Response.Status.NOT_FOUND).build();
       }
       return Response.ok(postService.findByUser(user.getId())).build();
    }

    @Secured
    @GET
    @Path("/id")
    public Response getById(@QueryParam("id") String id) {
        User user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @Secured
    @GET
    @Path("/username")
    public Response searchByUsername(@QueryParam("username") String username) {
        List<User> users = userService.findByUsername(username);
        return Response.ok(users).build();
    }

    @Secured
    @GET
    @Path("/email")
    public Response getUserByEmail(@QueryParam("email") String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @Secured
    @GET
    @Path("/follows")
    public Response getFollows(@QueryParam("id") String id){
        User user = userService.findById(id);
        if(user==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(userService.getFollows(user)).build();
    }

    @Secured
    @GET
    @Path("/followers")
    public Response getFollowing(@QueryParam("id") String id){
        User user = userService.findById(id);
        if(user==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(userService.getFollowers(user)).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String json) {
        User user = convertUser(json);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User createdUser = userService.create(user);
        if(createdUser!=null){
         return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    @Secured
    @PUT
    @Path("/addFollow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFollow(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        User[] users = null;
        try{
            users = objectMapper.readValue(json,User[].class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(users==null||users.length!=2){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(userService.addFollow(users[0],users[1])).build();
    }

    @SuppressWarnings("Duplicates")
    @Secured
    @PUT
    @Path("/removeFollow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeFollow(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        User[] users = null;
        try{
            users = objectMapper.readValue(json,User[].class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(users==null||users.length!=2){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(userService.removeFollow(users[0],users[1])).build();
    }


    @Secured
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser;
        try {
            newUser = objectMapper.readValue(json,User.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        User existingUser = userService.findById(newUser.getId());
        if(existingUser==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingUser.setProfilePicture(newUser.getProfilePicture());
        existingUser.setProfileDetails(newUser.getProfileDetails());
        existingUser.setName(newUser.getName());
        userService.update(existingUser);
        return Response.ok(getById(existingUser.getId())).build();
    }

    @Secured
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id){
        //TODO geen zin in nu
        userService.deleteById(id);
    }
}
