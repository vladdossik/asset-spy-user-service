package asset.spy.user.service.service;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {

    ContactResponseDto createContact(ContactCreateDto contactCreateDto, Long userId);

    ContactResponseDto getContactById(long id);

    ContactResponseDto updateContact(Long id, ContactUpdateDto contactUpdateDto);

    void deleteContact(Long id);

    Page<ContactResponseDto> getAllContacts(Pageable pageable, String contactType, String contactValue,
                                            Long userId, Integer priority);

}
