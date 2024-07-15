package banquemisr.challenge05.web.rest;

import banquemisr.challenge05.BankMaserApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = BankMaserApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback
public class UserResourceIntTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    @WithMockUser
    @Transactional
    public void getNonExistingUser() throws Exception {
        // Get the user
        webTestClient.get().uri("/user/{id}", Long.MAX_VALUE)
                .exchange().expectStatus().isNotFound();
    }


    @Test
    @WithMockUser
    @Transactional
    public void getExistingUser() throws Exception {
        // Get the user
        webTestClient.get().uri("/user/{id}", -1)
                .exchange().expectStatus().isEqualTo(200);
    }



}
