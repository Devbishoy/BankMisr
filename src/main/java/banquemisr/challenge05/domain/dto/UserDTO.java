package banquemisr.challenge05.domain.dto;

import banquemisr.challenge05.util.Messages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Null
    private Long id;

    @NotNull
    @Size(min = 5, max = 20, message = Messages.SIZE_MESSAGE)
    private String firstName;

    @NotNull
    @Size(min = 5, max = 20, message = Messages.SIZE_MESSAGE)
    private String lastName;

    @NotNull
    @Size(min = 5, max = 30, message = Messages.SIZE_MESSAGE)
    private String email;

    @NotNull
    @Size(min = 5, max = 20, message = Messages.SIZE_MESSAGE)
    private String password;

    @Null
    private String authoriy;

}
