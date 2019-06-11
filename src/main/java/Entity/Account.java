package Entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "kwetterAccount")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Account.findByAcc",
                        query = "SELECT p FROM kwetterAccount p WHERE p.userEmail = :email AND p.pass = :pass"),
                @NamedQuery(
                        name = "Account.checkUniqueId",
                        query = "SELECT COUNT(p) FROM kwetterAccount p WHERE p.userEmail = :email AND p.pass = :pass")
        }
)
public class Account implements Serializable {
    @Email(message = "Email should be valid")
    @Id
    @Size(max = 40)
    private String userEmail;
    @Size(min = 3,message = "Password should be between 3 and 12 characters")
    @NotNull
    private String pass;
    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull
    private List<Role> roles = new ArrayList<>();
    @Transient
    private String token;


    public Account(String userEmail, String pass,Role role) {
        this.userEmail = userEmail;
        this.pass = pass;
        roles.add(role);
    }
    public Account(String userEmail, String pass,List<Role> roles) {
        this.userEmail = userEmail;
        this.pass = pass;
        this.roles = roles;
    }
    public Account() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> role) {
        this.roles = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(userEmail, account.userEmail) &&
                Objects.equals(pass, account.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, pass);
    }
}
