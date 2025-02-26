package asset.spy.user.service.repository;

import asset.spy.user.service.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query(value = "SELECT c FROM Contact c WHERE " +
            "(:contactType IS NULL OR c.contactType LIKE %:contactType%) " +
            "AND (:userId IS NULL OR c.user.id = :userId) " +
            "AND (:priority IS NULL OR c.priority = :priority)")
    Page<Contact> findAllWithOptionalFilters(@Param("contactType") String contactType,
                                             @Param("userId") Long userId,
                                             @Param("priority") Integer priority,
                                             Pageable pageable);
}
