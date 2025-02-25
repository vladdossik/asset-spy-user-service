package asset.spy.user.service.repository;

import asset.spy.user.service.dto.ContactDTO;
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
    Page<ContactDTO> findAllContactsAfterCursor(@Param("cursor") Long cursor, Pageable pageable);

    Page<Contact> findByUserId(Long userId, Pageable pageable);

    Page<Contact> findByUserIdAndIdGreaterThan(Long userId, Long cursor, Pageable pageable);
}
