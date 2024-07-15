package banquemisr.challenge05.web.rest;

import banquemisr.challenge05.domain.Task;
import banquemisr.challenge05.domain.dto.PartiallyUpdate;
import banquemisr.challenge05.domain.dto.TaskDTO;
import banquemisr.challenge05.domain.dto.TaskSearchDTO;
import banquemisr.challenge05.domain.dto.UserTaskDTO;
import banquemisr.challenge05.domain.enumeration.Status;
import banquemisr.challenge05.security.SecurityCheck;
import banquemisr.challenge05.security.SecurityUser;
import banquemisr.challenge05.service.TaskService;
import banquemisr.challenge05.util.View;
import banquemisr.challenge05.util.filter.CustomPage;
import banquemisr.challenge05.util.filter.SearchCriteria;
import banquemisr.challenge05.util.filter.TaskSpecificationBuilder;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Bishoy Samir simple task impl
 * @apiNote @PreAuthorize("hasRole('ADMIN')") @Secured() annotations both not working in service layer or controller layer -> seems error with webflux and spring security 6
 */
@RestController
@RequestMapping("/task")
public class TaskResource {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SecurityCheck securityCheck;

    @Autowired
    private EntityManager entityManager;


    /**
     * POSt request to create task
     *
     * @param taskDTO      DTO or data holder for task
     * @param securityUser current login user get from spring security
     * @return should return task data if 200
     * @throws 409 in case conflict or id sent with request
     *             400 bad request in case request missing mandatory data
     */
    @PostMapping
    public Mono<Task> createTask(@Valid @RequestBody TaskDTO taskDTO, @AuthenticationPrincipal SecurityUser securityUser) {
        if (securityCheck.checkIfAdminRole(securityUser))
            Mono.just(taskService.createTask(taskDTO, securityUser));
        return Mono.empty();
    }


    /**
     * PUT request to update task
     *
     * @param taskDTO DTO or data holder for task
     * @return should return task data if 200
     * @throws 409 in case conflict or id sent with request
     *             400 bad request in case request missing mandatory data
     *             404   Not found status code in case id not exist in DB
     */
    @PutMapping
    public Mono<Task> updateTask(@Valid @RequestBody TaskDTO taskDTO, @AuthenticationPrincipal SecurityUser securityUser) {
        if (securityCheck.checkIfAdminRole(securityUser))
            return Mono.just(taskService.updateTask(taskDTO));
        return Mono.empty();
    }

    /**
     * GET to get task by ID
     *
     * @param id of task
     * @return return task data in case 200 success
     * return not found in case id not exist
     */
    @GetMapping("/{id}")
    public Mono<Task> findTaskById(@PathVariable(name = "id") Long id) {
        return Mono.just(taskService.findTaskById(id));
    }

    /**
     * GET request to get ALl tasks
     *
     * @param pageable
     * @return is should return pagination data of task based on request pageable values
     */
    @GetMapping("/all")
    public Mono<Page<Task>> findAllTasks(@PageableDefault(size = 20) final Pageable pageable) {
        return Mono.just(taskService.findAllTasks(pageable));
    }

    /**
     * GET request to get task by status
     *
     * @param pageable
     * @param status   status of task todo done in progress values
     * @return will throw exception if status not correct  illLegalArgument  Exception
     */
    @GetMapping("/status/{status}")
    public Mono<Page<Task>> findAllTasksByStatus(@PageableDefault(size = 20) final Pageable pageable, @RequestParam(value = "status", required = false, defaultValue = "TO_DO") String status) {
        Status taskStatus = Status.valueOf(status.toUpperCase());
        return Mono.just(taskService.findAllTasksByStatus(pageable, taskStatus));
    }

    /**
     * PATCH request to update task  partially
     *
     * @param partiallyUpdateTask used to update assign user and priority of task
     * @return task data or exception
     */
    @PatchMapping
    public Mono<Task> partiallyUpdateTask(@Valid @RequestBody PartiallyUpdate partiallyUpdateTask) {
        return Mono.just(taskService.partiallyUpdateTask(partiallyUpdateTask));
    }

    /**
     * DELETE request to get task by Id
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable(name = "id") Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        if (securityCheck.checkIfAdminRole(securityUser))
            taskService.deleteById(id);
        return Mono.empty();
    }


    /**
     * data request ex:
     * {
     * "searchCriteriaList": [
     * {
     * "filterKey": "status",
     * "value": "TO_DO",
     * "operation": "eq"
     * }
     * ,
     * {
     * "filterKey": "priority",
     * "value": "HIGH",
     * "operation": "eq"
     * }
     * ],
     * "dataOption": "any"
     * }
     * <p>
     * if dataOption : any mena OR if All mean And
     *
     * @param pageNum       page number for data return
     * @param pageSize      size of data in each page
     * @param taskSearchDTO DTO to filter data example above
     * @return pagination data from task table
     */
    @PostMapping("/search")
    @JsonView(View.TaskPage.class)
    public Mono<Page<Task>> searchTasks(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, @RequestBody TaskSearchDTO taskSearchDTO) {
        TaskSpecificationBuilder builder = new TaskSpecificationBuilder();
        List<SearchCriteria> criteriaList = taskSearchDTO.getSearchCriteriaList();
        if (criteriaList != null) {
            criteriaList.forEach(criteria ->
            {
                criteria.setDataOption(taskSearchDTO.getDataOption());
                builder.with(criteria);
            });
        }
        Pageable page = PageRequest.of(pageNum, pageSize, Sort.by("id").ascending().and(Sort.by("status")).ascending().and(Sort.by("priority")).ascending());
        Page<Task> tasksPage = taskService.findBySearchCriteria(builder.build(), page);
        return Mono.just(new CustomPage<>(tasksPage.getContent(),page,tasksPage.getTotalElements()));
    }


    /**
     * Post request to get ALl tasks for specific user and specific status
     *
     * @param pageable
     * @return is should return pagination data of task based on request pageable values
     */
    @PostMapping("/user")
    public Mono<Page<Task>> findAllTasksByUser(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,@Valid @RequestBody UserTaskDTO userTaskDTO) {
        return Mono.just(taskService.findAllTasksByStatusAndUser( PageRequest.of(pageNum, pageSize),userTaskDTO));
    }


}
