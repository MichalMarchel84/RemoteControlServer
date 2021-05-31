package pl.marchel.remotecontrolserver.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
    @Transient
    private String connectedWith;
}
