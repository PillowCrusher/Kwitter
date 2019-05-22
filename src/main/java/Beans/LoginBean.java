package Beans;

import Entity.User;
import Service.UserService;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Level;

@SessionScoped
@Named
public class LoginBean implements Serializable {
    @Inject
    private UserService userService = new UserService();

    private String username;
    private String password;

    public LoginBean(){}

    public String login(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            Principal userPrincipal = request.getUserPrincipal();
            request.login(username, password);
        } catch (ServletException e) {
            java.util.logging.Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, e);
            context.addMessage(null, new FacesMessage("Login failed: ",  e.getMessage()) );
            return "";
        }

        Principal principal = request.getUserPrincipal();
        User user = userService.findByEmail(username);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("user", user);

        if (request.isUserInRole("admin")){
            return "adminpanel";
        }
        if (request.isUserInRole("user")){
            return "userpanel";
        }
        return "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
