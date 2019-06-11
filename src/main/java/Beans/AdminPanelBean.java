package Beans;

import Entity.Account;
import Entity.Role;
import Entity.User;
import Service.PostService;
import Service.UserService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
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
    private List<Role> role;

    private List<String> selectedRoles;
    private List<String> roles;

    private List<User> userList;

    public AdminPanelBean() {

        roles = new ArrayList<>();
        roles.add("admin");
        roles.add("moderator");
        roles.add("user");
    }


    public List getUsersList() {
        userList = userService.findAll();
        return userList;
    }

    public String createUser() {
        return "create";
    }

    public String deleteuser(User user) {
        userService.delete(user);
        return "";
    }

    public User getToEdit() {
        return toEdit;
    }

    public void setToEdit(User toEdit) {
        this.toEdit = toEdit;
    }

    public String editAction(User user) {
        for (User userL : userList) {
            if (userL.getId().equals(user.getId())) {
                toEdit = userL;
                userEmail = toEdit.getAccount().getUserEmail();
                name = toEdit.getName();
                profilePic = toEdit.getProfilePicture();
                profileDetails = toEdit.getProfileDetails();
                role = toEdit.getAccount().getRoles();
                return "edit";
            }
        }
        return null;
    }


    public String getRolesString(Account user) {
        String a = "";
        for(Role role : user.getRoles()){
            a = a + " " + role.getRolename();
        }
        return a;
    }

    public String save() {
        for (String role : selectedRoles){
            Role createdRole = new Role(role,userEmail);
            userService.addRole(createdRole);
        }
        List<Role> addedRoles = userService.getRoles(userEmail);
        toEdit.getAccount().setRoles(addedRoles);
        toEdit.getAccount().setUserEmail(userEmail);
        toEdit.setName(name);
        toEdit.setProfileDetails(profileDetails);
        toEdit.setProfilePicture(profilePic);
        userService.update(toEdit);
        return "save";
    }
    public void setProfileDetails(String profileDetails) {
        this.profileDetails = profileDetails;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
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

    public List<String> getPossiblePictures() {
       return userService.getPictures();
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}

