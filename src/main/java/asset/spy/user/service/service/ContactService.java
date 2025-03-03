package asset.spy.user.service.service;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ContactService {

    ContactResponseDto createContact(ContactCreateDto contactCreateDto, UUID userId);

    ContactResponseDto getContactByExternalId(UUID externalId);

    ContactResponseDto updateContact(UUID externalId, ContactUpdateDto contactUpdateDto);

    void deleteContact(UUID externalId);

    Page<ContactResponseDto> getAllContacts(Pageable pageable, String contactType,
                                            String contactValue, Integer priority);
}
