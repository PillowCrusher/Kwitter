package Service;

import Entity.Account;
import Entity.Post;
import Entity.Role;
import Entity.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;


@SuppressWarnings("Duplicates")
@Singleton
@javax.ejb.Startup
public class Startup {
    @Inject
    private UserService userService;
    @Inject
    private PostService postService;

    @PostConstruct
    public void init(){
        Role userRole = new Role("user","janpeter@peters.nl");
        Role adminRole = new Role("admin","jeroen1@peters.nl");
        Role moderatorRole = new Role("moderator","jeroen1@peters.nl");

        userService.addRole(userRole);
        userService.addRole(adminRole);
        userService.addRole(moderatorRole);

        List<Role> saved1 = userService.getRoles("janpeter@peters.nl");
        List<Role> saved2 = userService.getRoles("jeroen1@peters.nl");

        Account account = new Account("janpeter@peters.nl","wachtw",saved1);
        Account account1 = new Account("jeroen1@peters.nl" ,"wachtw",saved2);

        User user = new User("Henk","assets/danger_birb.png","Een hele toffe kerel!",account);
        User user2 = new User("Jan","assets/danger_birb.png","Een hele toffe kerel!",account1);

        userService.create(user);
        userService.create(user2);


        //TODO test following with wrong email
        userService.addFollow(userService.findByEmail("janpeter@peters.nl"),userService.findByEmail("jeroen1@peters.nl"));

        Post post = new Post("Hallo daar!",user);
        Post post1 = new Post("Generaal kenobi!",user);
        Post post2 = new Post("U bent een moedige",user);
        Post post3 = new Post("Je had gelijk meester",user);
        Post post4 = new Post("De onderhandelingen waren kort",user);


        postService.create(post);
        postService.create(post1);
        postService.create(post2);
        postService.create(post3);
        postService.create(post4);


        Post post5 = new Post("Marco",user2);
        Post post6 = new Post("Polo",user2);
        Post post7 = new Post("Wat is liefde",user2);
        Post post8 = new Post("Baby doe me geen pijn",user2);
        Post post9 = new Post("Doe me geen pijn",user2);


        postService.create(post5);
        postService.create(post6);
        postService.create(post7);
        postService.create(post8);
        postService.create(post9);
    }
}
