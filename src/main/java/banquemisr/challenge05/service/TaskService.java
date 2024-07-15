package banquemisr.challenge05.service;

import banquemisr.challenge05.domain.Task;
import banquemisr.challenge05.domain.dto.PartiallyUpdate;
import banquemisr.challenge05.domain.dto.TaskDTO;
import banquemisr.challenge05.domain.dto.UserTaskDTO;
import banquemisr.challenge05.domain.enumeration.Status;
import banquemisr.challenge05.security.SecurityUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;


public interface TaskService {
    Task updateTask(TaskDTO userDTO);

    Task findTaskById(Long id);

    Task createTask(TaskDTO userDTO,SecurityUser securityUser);

    Page<Task> findAllTasks(Pageable pageable);

    Page<Task> findAllTasksByStatus(Pageable pageable,Status taskStatus);

    Task partiallyUpdateTask(PartiallyUpdate partiallyUpdateTask);

    void deleteById(Long id);

    Page<Task> findBySearchCriteria(Specification<Task> spec, Pageable page);

    List<Task> findTaskByDuoDateAndNotDone(LocalDateTime localDateTime);

    Page<Task> findAllTasksByStatusAndUser(Pageable pageable,UserTaskDTO userTaskDTO);
}
