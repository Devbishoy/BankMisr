package banquemisr.challenge05.util.filter;

import banquemisr.challenge05.domain.Task;
import banquemisr.challenge05.domain.User;
import banquemisr.challenge05.domain.enumeration.Priority;
import banquemisr.challenge05.domain.enumeration.Status;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;


public class TaskSpecification implements Specification<Task> {
    private final SearchCriteria searchCriteria;

    public TaskSpecification(final SearchCriteria searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        String searchingValue = "";
        if (searchCriteria.getFilterKey().equalsIgnoreCase("status"))
            searchingValue = Status.valueOf(searchCriteria.getValue().toString().toUpperCase()).name();
        else if (searchCriteria.getFilterKey().equalsIgnoreCase("priority"))
            searchingValue = Priority.valueOf(searchCriteria.getValue().toString().toUpperCase()).name();
        else
            searchingValue = searchCriteria.getValue().toString().toLowerCase();

        switch (Objects.requireNonNull(Condition.getSimpleOperation(searchCriteria.getOperation()))) {
            case LIKE:
                if (searchCriteria.getFilterKey().equals("user")) {
                    return cb.like(cb.lower(userJoin(root).<String>get(searchCriteria.getFilterKey())), "%" + searchingValue + "%");
                }
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + searchingValue + "%");

            case NOT_LIKE:
                if (searchCriteria.getFilterKey().equals("user")) {
                    return cb.notLike(cb.lower(userJoin(root).<String>get(searchCriteria.getFilterKey())), "%" + searchingValue + "%");
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + searchingValue + "%");

            case EQUAL:
                return cb.equal(cb.trim(root.get(searchCriteria.getFilterKey())), searchingValue);
            case NOT_EQUAL:
                return cb.notEqual(cb.trim(root.get(searchCriteria.getFilterKey())), searchingValue);
            case BEGINS_WITH:
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + searchingValue);
            case DOES_NOT_BEGIN_WITH:
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + searchingValue);
            case ENDS_WITH:
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), searchingValue + "%");
            case DOES_NOT_END_WITH:
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), searchingValue + "%");

        }

        return null;
    }

    private Join<Task, User> userJoin(Root<Task> root) {
        return root.join("User");
    }
}