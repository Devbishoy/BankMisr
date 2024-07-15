package banquemisr.challenge05.domain;


import banquemisr.challenge05.domain.enumeration.Priority;
import banquemisr.challenge05.domain.enumeration.Status;
import banquemisr.challenge05.util.Messages;
import banquemisr.challenge05.util.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Table(name = "task")
@Entity
public class Task implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @SequenceGenerator(name = "hibernateSequence",initialValue = 1)
    @JsonView(View.BasicTask.class)
    private Long id;

    @NotNull
    @Size(min = 5, max = 100, message = Messages.SIZE_MESSAGE)
    @Column(name = "title")
    @JsonView(View.BasicTask.class)
    private String title;

    @Column(name = "description")
    @JsonView(View.BasicTask.class)
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonView(View.BasicTask.class)
    private Status status = Status.TO_DO;

    @Column(name = "priority")
    @JsonView(View.BasicTask.class)
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.LOW;

    @Column(name = "duo_date")
    @JsonView(View.BasicTask.class)
    private LocalDateTime duoDate;

    @Column(name = "create_date")
    @JsonView(View.BasicTask.class)
    private ZonedDateTime createDate = ZonedDateTime.now();


    @JsonView(View.BasicTask.class)
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee")
    private User assignee;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "create_by")
    @JsonView(View.BasicTask.class)
    @JsonIgnore
    private User createdBy;

}
