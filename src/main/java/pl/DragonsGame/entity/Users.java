package pl.DragonsGame.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity

@Table(name="users")
@EqualsAndHashCode(of = "username")
@ToString(exclude = "password")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String email;


    private String created;

    private String last_update;
    private boolean active;

    @ManyToOne
    private UsersRoles usersRoles;

    private String changeBy;

    private String activateToken;
    public Users(Long id, String username, String password, String created, String last_update, UsersRoles usersRoles, String email, boolean active,  String changeBy, String activateToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.created = created;
        this.last_update = last_update;
        this.usersRoles = usersRoles;
        this.email = email;
        this.active = active;
        this.changeBy = changeBy;
        this.activateToken = activateToken;
    }


}
