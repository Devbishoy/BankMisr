package banquemisr.challenge05.util.filter;

import banquemisr.challenge05.domain.Task;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecificationBuilder {

    private final List<SearchCriteria> params;

    public TaskSpecificationBuilder(){
        this.params = new ArrayList<>();
    }

    public final TaskSpecificationBuilder with(String key, String operation, Object value){
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final TaskSpecificationBuilder with(SearchCriteria searchCriteria){
        params.add(searchCriteria);
        return this;
    }

    public Specification<Task> build(){
        if(params.isEmpty()){
            return null;
        }
        Specification<Task> result = new TaskSpecification(params.get(0));
        for (int idx = 1; idx < params.size(); idx++){
            SearchCriteria criteria = params.get(idx);
            result =  Condition.getDataOption(criteria.getDataOption()) == Condition.ALL ? Specification.where(result).and(new TaskSpecification(criteria)) : Specification.where(result).or(new TaskSpecification(criteria));
        }
        return result;
    }
}