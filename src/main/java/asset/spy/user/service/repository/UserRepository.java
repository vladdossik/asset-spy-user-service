package asset.spy.user.service.repository;

import asset.spy.user.service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.contacts WHERE u.id > :cursor ORDER BY u.id ASC")
    Page<User> findAllUsersAfterCursor(@Param("cursor") Long cursor, Pageable pageable);
}
