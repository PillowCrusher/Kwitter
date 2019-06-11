package Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@NamedQueries(
        {
                @NamedQuery(
                        name = "kwetterRole.getRole",
                        query = "SELECT r FROM kwetterRole r WHERE r.email = :email"
                )

        }
)

@Entity(name = "kwetterRole")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String email;
    @NotNull
    private String rolename;

    public Role() {
    }

    public Role(@NotNull String rolename) {
        this.rolename = rolename;
        this.email = "test";
    }

    public Role(String rolename, String email) {
        this.rolename = rolename;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(getEmail(), role.getEmail()) &&
                Objects.equals(getRolename(), role.getRolename());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getRolename());
    }
}
