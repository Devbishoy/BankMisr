package banquemisr.challenge05.web.rest;


import banquemisr.challenge05.BankMaserApplication;
import banquemisr.challenge05.domain.dto.TaskDTO;
import banquemisr.challenge05.domain.enumeration.Priority;
import banquemisr.challenge05.domain.enumeration.Status;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(classes = BankMaserApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback
public class TaskResourceIntTest {

    @Autowired
    private WebTestClient webTestClient;

    /**
     * test create task
     * @throws Exception
     */

    //TODO: complete junit test
    @WithMockUser
    @Transactional
    public void createTask() throws Exception {
        // create task
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        TaskDTO task=new TaskDTO();
        task.setAssignedId(-1L);
        task.setDescription("test task");
        task.setPriority(Priority.HIGH.name());
        task.setStatus(Status.TO_DO.name());
        task.setDuoDate("2024-09-20 12:00:00");
        task.setTitle("test");

        webTestClient.post()
                .uri( "/task" )
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(task))
                .exchange().expectStatus().isOk();
    }

}
