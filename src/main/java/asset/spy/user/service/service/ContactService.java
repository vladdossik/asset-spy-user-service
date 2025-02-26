package asset.spy.user.service.service;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import org.springframework.data.domain.Page;

public interface ContactService {

    ContactResponseDto createContact(ContactCreateDto contactCreateDto, Long userId);

    ContactResponseDto getContactById(long id);

    ContactResponseDto updateContact(Long id, ContactUpdateDto contactUpdateDto);

    void deleteContact(Long id);

    Page<ContactResponseDto> getAllContacts(int page, int size, String sortField, String sortDirection,
                                            String contactType, Long userId, Integer priority);

}
