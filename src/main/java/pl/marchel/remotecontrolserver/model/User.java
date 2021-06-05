package pl.marchel.remotecontrolserver.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false, unique = true, length = 60)
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 30, message = "Name cannot be longer than {max} chars")
    private String username;
    @Size(min = 6, message = "Password must be at least {min} chars long")
    private String password;
    private int enabled;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles;
}