package banquemisr.challenge05.repository;

import banquemisr.challenge05.domain.Task;
import banquemisr.challenge05.domain.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findByStatus(Pageable pageable, Status taskStatus);

    @Query(nativeQuery = false, value = "select T from Task T where T.duoDate BETWEEN :start AND :end  And T.status <> 'DONE' ")
    List<Task> findTaskDuoDateIn(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Page<Task> findByStatusAndAssignee_Id(Pageable pageRequest,Status status, Long assignedUser);
}
