package banquemisr.challenge05.jobs;


import banquemisr.challenge05.domain.Task;
import banquemisr.challenge05.service.NotifyService;
import banquemisr.challenge05.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author bishoy samir
 * job to notfiy users about duo date tasks
 */
@Service
@Transactional
@Slf4j
public class NotifyJob {

    @Autowired
    private TaskService taskService;

    @Autowired
    private NotifyService notifyService;

    /**
     * job run once every day  run Async for each email
     */
    @Scheduled(cron = "@daily")
    public void notfiyUserTasks() {
        log.info(" --- start email job ---");
        try {
            //select all tomorrow tasks in_progress and TO_DO status task that soon reach duoDate
            List<Task> listOfDuo = taskService.findTaskByDuoDateAndNotDone(ZonedDateTime.now().toLocalDateTime().plusDays(1));
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> listOfDuo.parallelStream().forEachOrdered(l -> sendEmail(l.getAssignee().getEmail())));
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        log.info(" --- end email job ---");
    }

    public void sendEmail(String email) {
        notifyService.sendMessage(email, " Task will reach duo date soon ", " please notice that task will reach duo date soon !! complete it ASAP ");
    }


}
