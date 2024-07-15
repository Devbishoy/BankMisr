package banquemisr.challenge05.domain.dto;


import banquemisr.challenge05.util.Messages;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
@Setter
public class TaskDTO {


    private Long id;

    @NotNull
    @Size(min = 5, max = 100, message = Messages.SIZE_MESSAGE_TITLE)
    private String title;

    @NotNull
    @Size(min = 5, max = 500, message = Messages.SIZE_MESSAGE_DESC)
    private String description;

    @NotNull
    private String status;

    @NotNull
    @Size(min = 5, max = 100, message = Messages.SIZE_MESSAGE)
    private String priority;

    @NotNull
    private Long assignedId;

    @NotNull
    private String duoDate;

}
