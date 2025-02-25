package asset.spy.user.service.service;

import asset.spy.user.service.dto.ContactDTO;
import org.springframework.data.domain.Page;

public interface ContactService {

    ContactDTO createContact(ContactDTO contactDTO);

    ContactDTO getContactById(long id);

    ContactDTO updateContact(Long id, ContactDTO contactDTO);

    void deleteContact(Long id);

    Page<ContactDTO> getAllContacts(Long cursor, int size);

    Page<ContactDTO> getAllContactsForUser(Long userId, Long cursor, int size);
}
