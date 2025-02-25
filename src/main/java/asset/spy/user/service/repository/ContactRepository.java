package asset.spy.user.service.repository;

import asset.spy.user.service.dto.ContactDto;
import asset.spy.user.service.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c LEFT JOIN FETCH c.user WHERE c.id > :cursor ORDER BY c.id ASC")
    Page<ContactDto> findAllContactsAfterCursor(@Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT c FROM Contact c LEFT JOIN FETCH c.user WHERE c.user.id = :userId " +
            "AND c.id > :cursor ORDER BY c.id ASC")
    Page<ContactDto> findAllContactsForUserAfterCursor(@Param("userId") Long userId, Long cursor, Pageable pageable);
}
