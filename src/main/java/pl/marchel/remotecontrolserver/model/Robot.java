package pl.marchel.remotecontrolserver.model;

import lombok.Data;
import pl.marchel.remotecontrolserver.utils.ConfigurationConverter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "robots")
@Data
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
    @ManyToOne(fetch = FetchType.LAZY)
    private Script script;
    @Transient
    private String connectedWith;
    @Convert(converter = ConfigurationConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Configuration> configurations;
}
