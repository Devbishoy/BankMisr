package banquemisr.challenge05.domain;


import banquemisr.challenge05.util.Messages;
import banquemisr.challenge05.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bishoy Samir
 */
@Getter
@Setter
@Table(name = "task_user")
@Entity
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @SequenceGenerator(name = "hibernateSequence",initialValue = 1)
    @JsonView(View.BasicUser.class)
    private Long id;

    @NotNull
    @Size(min=5,max = 20,message = Messages.SIZE_MESSAGE)
    @Column(name = "first_name")
    @JsonView(View.BasicUser.class)
    private String firstName;

    @NotNull
    @Size(min=5,max = 20,message = Messages.SIZE_MESSAGE)
    @Column(name = "last_name")
    @JsonView(View.BasicUser.class)
    private String lastName;

    @NotNull
    @Size(min=5,max = 30,message = Messages.SIZE_MESSAGE)
    @Column(name = "email")
    @JsonView(View.BasicUser.class)
    private String email;

    @NotNull
    @Size(min=8,max = 60,message = Messages.SIZE_MESSAGE)
    @Column(name = "password")
    private  String password;


    @JsonView(View.UserAuthority.class)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();


    @Column(name = "failed_tries_attempts")
    @JsonView(View.BasicUser.class)
    private Integer failedTriesAttempts = 0;

    @Column(name = "last_failed_login")
    @JsonView(View.BasicUser.class)
    private ZonedDateTime lastFailedLogin;

    @NotNull
    @Column(name = "activated",nullable = false)
    @JsonView(View.BasicUser.class)
    private boolean activated = true;

}
