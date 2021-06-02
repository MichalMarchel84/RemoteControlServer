package pl.marchel.remotecontrolserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "scripts")
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition="TEXT")
    private String script;
}
