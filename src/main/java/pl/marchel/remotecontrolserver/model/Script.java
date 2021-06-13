package pl.marchel.remotecontrolserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "scripts")
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
    @Column(columnDefinition="TEXT")
    private String script;
    private String name;
}
