package Beans;

import Entity.Account;
import Entity.User;
import Service.UserService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class NewUserBean implements Serializable {
    @Inject
    UserService userService;

    private String userEmail;
    private String pass;
    private String name;
    private String profilePic = "C:\\Users\\jeroe\\Downloads\\btmja1nlv8q21.png";
    private String profileDetails;
    private String role;

    public String saveNewUser(){
        Account account = new Account();
        account.setUserEmail(userEmail);
        account.setPass(pass);
        account.setRole(role);
        User user = new User();
        user.setAccount(account);
        user.setName(name);
        user.setProfilePicture(profilePic);
        user.setProfileDetails(profileDetails);
        userService.create(user); //TODO check null and give user feedback
        return "save";
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public void setProfileDetails(String profileDetails) {
        this.profileDetails = profileDetails;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
