package asset.spy.user.service.service;

import asset.spy.user.service.dto.ContactDto;
import org.springframework.data.domain.Page;

public interface ContactService {

    ContactDto createContact(ContactDto contactDTO);

    ContactDto getContactById(long id);

    ContactDto updateContact(Long id, ContactDto contactDTO);

    void deleteContact(Long id);

    Page<ContactDto> getAllContacts(Long cursor, int size);

    Page<ContactDto> getAllContactsForUser(Long userId, Long cursor, int size);
}
