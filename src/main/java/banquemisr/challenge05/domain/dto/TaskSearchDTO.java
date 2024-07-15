package banquemisr.challenge05.domain.dto;

import banquemisr.challenge05.util.filter.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchDTO {

    private List<SearchCriteria> searchCriteriaList;
    private String dataOption;

}