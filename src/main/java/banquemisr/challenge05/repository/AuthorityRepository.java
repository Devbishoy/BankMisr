package banquemisr.challenge05.repository;

import banquemisr.challenge05.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authority entity.
 */

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    @Query("from Authority where name=:id")
    Authority findOne(@Param("id") String id);

}
