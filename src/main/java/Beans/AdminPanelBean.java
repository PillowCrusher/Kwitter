package Beans;

import Entity.Post;
import Entity.User;
import Service.PostService;
import Service.UserService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named
public class AdminPanelBean implements Serializable {
    @Inject
    private UserService userService;
    @Inject
    private PostService postService;

    private User toEdit;

    private String userEmail;
    private String name;
    private String profilePic;
    private String profileDetails;
    private String role;

    private List<Post> postsList;
    private List<User> userList;

    public AdminPanelBean(){

    }

    public List getUsersList(){
            userList = userService.findAll();
        return userList;
    }

    public List<Post> getPostsList(){
        this.postsList = postService.findAll();
        return this.postsList;
    }

    public String createUser(){
        return "create";
    }

    public String deleteuser(User user){
        userService.delete(user);
        return "";
    }

    public String saveAction(){
        System.out.println("wtf?");
        toEdit.getAccount().setRole(role);
        toEdit.getAccount().setUserEmail(userEmail);
        toEdit.setName(name);
        toEdit.setProfileDetails(profileDetails);
        toEdit.setProfilePicture(profilePic);
        userService.update(toEdit);
        for (User userL : userList){
            if(userL.getId().equals(toEdit.getId())){
                userL = toEdit;
            }
        }
        return "save";
    }
    public String editAction(User user){
        for (User userL : userList){
            if(userL.getId().equals(user.getId())){
                toEdit = userL;
                break;
            }
        }
        if(toEdit!=null){
            userEmail = toEdit.getAccount().getUserEmail();
            name = toEdit.getName();
            profilePic = toEdit.getProfilePicture();
            profileDetails = toEdit.getProfileDetails();
            role = toEdit.getAccount().getRole();
            return "edit";
        }
        return null;
    }
    public void setProfileDetails(String profileDetails) {
        this.profileDetails = profileDetails;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfileDetails() {
        return profileDetails;
    }

}
