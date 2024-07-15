package banquemisr.challenge05.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserTaskDTO {
    @NotNull
    private Long assignedUser;
    @NotNull
    private String status;
}
