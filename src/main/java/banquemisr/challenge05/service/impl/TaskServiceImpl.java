package banquemisr.challenge05.service.impl;


import banquemisr.challenge05.domain.Task;
import banquemisr.challenge05.domain.User;
import banquemisr.challenge05.domain.dto.PartiallyUpdate;
import banquemisr.challenge05.domain.dto.TaskDTO;
import banquemisr.challenge05.domain.dto.UserTaskDTO;
import banquemisr.challenge05.domain.enumeration.Priority;
import banquemisr.challenge05.domain.enumeration.Status;
import banquemisr.challenge05.repository.TaskRepository;
import banquemisr.challenge05.repository.UserRepository;
import banquemisr.challenge05.security.SecurityUser;
import banquemisr.challenge05.service.TaskService;
import banquemisr.challenge05.util.Helper;
import banquemisr.challenge05.util.Messages;
import banquemisr.challenge05.util.errors.CustomParameterizedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    @Lazy
    private UserRepository userRepository;


    @Override
    public Task createTask(TaskDTO taskDTO, SecurityUser securityUser) {
        Task task = new Task();
        User assignUser = userRepository.findById(taskDTO.getAssignedId()).orElseThrow(() -> new EntityNotFoundException(Messages.ID_NOT_FOUND));

        //TODO: need another exception type to throw it should be security problem
        User createUser = userRepository.findById(securityUser.getId()).orElseThrow(() -> new ConcurrencyFailureException(Messages.ID_VALUE));

        //validations
        if (null != taskDTO.getId())
            throw new ConcurrencyFailureException(Messages.ID_VALUE);

        if (null == taskDTO.getAssignedId())
            throw new ConcurrencyFailureException(Messages.ASSIGNE_ID_VALUE);
        if(null!=taskDTO.getDuoDate()&&LocalDateTime.parse(taskDTO.getDuoDate(), Helper.formatter).isBefore(LocalDateTime.now()))
            throw new ConcurrencyFailureException(Messages.TIME_IN_FUTURE);

        //create
        task.setCreatedBy(createUser);
        task.setDuoDate(LocalDateTime.parse(taskDTO.getDuoDate(), Helper.formatter));
        task.setDescription(taskDTO.getDescription());
        task.setTitle(taskDTO.getTitle());
        task.setAssignee(assignUser);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(TaskDTO taskDTO) {
        //validations
        if (null == taskDTO.getId())
            throw new ConcurrencyFailureException(Messages.ID_VALUE);

        Task task = taskRepository.findById(taskDTO.getId()).orElseThrow(()-> new EntityNotFoundException(Messages.ID_NOT_FOUND));
        User assignUser = userRepository.findById(taskDTO.getAssignedId()).orElseThrow(()-> new EntityNotFoundException(Messages.ID_NOT_FOUND));

        //update
        task.setPriority(Priority.valueOf(taskDTO.getPriority().toUpperCase()));
        task.setStatus(Status.valueOf(taskDTO.getStatus().toUpperCase()));
        task.setAssignee(assignUser);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        return taskRepository.save(task);
    }

    @Override
    public Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Messages.ID_NOT_FOUND));
    }

    @Override
    public Page<Task> findAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> findAllTasksByStatus(Pageable pageable, Status taskStatus) {
        return taskRepository.findByStatus(pageable, taskStatus);
    }

    @Override
    public Task partiallyUpdateTask(PartiallyUpdate partiallyUpdateTask) {
        Optional<Task> taskOptional = taskRepository.findById(partiallyUpdateTask.getId());
        //validations
        if (taskOptional.isEmpty())
            throw new EntityNotFoundException(Messages.ID_NOT_FOUND);
        //TODO:update duo date should be by admin to
        Task task = taskOptional.get();
        if (partiallyUpdateTask.getAttribute().equalsIgnoreCase("priority"))
            task.setPriority(Priority.valueOf(partiallyUpdateTask.getValue().toUpperCase()));
        else if (partiallyUpdateTask.getAttribute().equalsIgnoreCase("assignee")) {
            User user = userRepository.findById(Long.valueOf(partiallyUpdateTask.getValue())).orElseThrow(() -> new CustomParameterizedException(Messages.ID_MISSING));
            task.setAssignee(user);
        }
        return taskRepository.save(task);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }


    @Override
    public Page<Task> findBySearchCriteria(Specification<Task> spec, Pageable page){
        Page<Task> searchResult = taskRepository.findAll(spec, page);
        return searchResult;
    }

    @Override
    public List<Task> findTaskByDuoDateAndNotDone(LocalDateTime localDateTime) {
         List<Task> tasks=taskRepository.findTaskDuoDateIn(localDateTime,localDateTime.plusDays(1));
        return tasks;
    }

    @Override
    public Page<Task> findAllTasksByStatusAndUser(Pageable pageRequest, UserTaskDTO userTaskDTO) {
        return taskRepository.findByStatusAndAssignee_Id(pageRequest,Status.valueOf(userTaskDTO.getStatus().toUpperCase()),userTaskDTO.getAssignedUser());
    }
}
