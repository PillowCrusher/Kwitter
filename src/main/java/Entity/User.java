package Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "kwetterUser")
@NamedQueries(
        {
                @NamedQuery(
                        name = "User.findAll",
                        query = "SELECT p FROM kwetterUser p"),
                @NamedQuery(
                        name = "User.findByUsername",
                        query = "SELECT p FROM kwetterUser p WHERE p.name LIKE CONCAT('%', :username, '%')"
                ),
                @NamedQuery(
                        name = "User.findByEmail",
                        query = "SELECT p FROM kwetterUser p WHERE p.account.userEmail = :email"
                ),
                @NamedQuery(
                        name = "User.findById",
                        query = "SELECT p FROM kwetterUser p WHERE p.id = :id"
                ),
                @NamedQuery(
                        name = "User.deleteById",
                        query = "DELETE FROM kwetterUser p WHERE p.id = :id"
                ),
                @NamedQuery(
                        name = "User.checkUniqueEmail",
                        query = "SELECT COUNT(p) FROM kwetterUser p WHERE p.account.userEmail = :email"
                ),
                @NamedQuery(
                        name = "User.checkUniqueId",
                        query = "SELECT COUNT(p) FROM kwetterUser p WHERE p.id = :id"
                ),
                @NamedQuery(
                        name = "User.findByAcc",
                        query = "SELECT p FROM kwetterUser p WHERE p.account.userEmail = :email AND p.account.pass = :pass"
                )
        }
)
public class User implements Serializable {
    @Id
    @Size(max = 50)
    private String id;
    @Size(max = 14)
    @NotNull
    private String name;
    @Size(max = 100)
    private String profilePicture;
    @Size(max = 150)
    private String profileDetails;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> followedUsers = new ArrayList<>();
    @OneToOne
    @NotNull
    private Account account;

    public User(@Size(max = 14) @NotNull String name, @Size(max = 100) String profilePicture, @Size(max = 100) String profileDetails, Account account) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.profileDetails = profileDetails;
        this.account = account;
    }

    public User() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(String profileDetails) {
        this.profileDetails = profileDetails;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<User> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(List<User> followedUsers) {
        this.followedUsers = followedUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(profilePicture, user.profilePicture) &&
                Objects.equals(profileDetails, user.profileDetails) &&
                Objects.equals(account, user.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profilePicture, profileDetails, account);
    }
}