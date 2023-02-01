package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "webuser", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User implements UserDetails {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 30, message = "min 2 max 30")
    @Column(name = "username")
    private String username;
    @Size(min = 5, message = "min 5")
    @Column(name = "password")
    private String password;
    @Transient
    private String passwordConfirm;


    @ManyToMany(fetch = FetchType.EAGER)
    @Getter
    @Setter
    private List<Role> roles;

    public User() {
    }

    public User(String login, String password, List<Role> roles) {
        this.username = login;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public boolean isAccountNonExpired() {
        return true;
    }


    public boolean isAccountNonLocked() {
        return true;
    }


    public boolean isCredentialsNonExpired() {
        return true;
    }


    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }

}