package banquemisr.challenge05.repository;

import banquemisr.challenge05.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {




    @Query("update User set failedTriesAttempts=0 , lastFailedLogin=null where email=:email")
    @Modifying
    @Transactional
    void resetFailedTriesAttempts(@Param("email") String email);

    @Query("update User set failedTriesAttempts=failedTriesAttempts+1 , lastFailedLogin=:time where email=:email")
    @Modifying
    @Transactional
    void incrementTriesAttempts(@Param("email") String email, @Param("time") ZonedDateTime time);

    @EntityGraph(attributePaths = {"authorities"})
    User findOneWithAuthoritiesByEmail(String email);

    List<User> findByAuthoritiesName(String role);
}
