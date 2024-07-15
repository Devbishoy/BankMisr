package banquemisr.challenge05.domain.dto;


import banquemisr.challenge05.util.Messages;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PartiallyUpdate {

    @NotNull(message = Messages.ID_MISSING)
    private Long id;
    @NotNull(message = Messages.ATTRIBUTE_MISSING)
    private String attribute;
    @NotNull(message = Messages.VALUE_MISSING)
    private String value;

}
