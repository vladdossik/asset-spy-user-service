package asset.spy.user.service.service;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.exception.ContactNotFoundException;
import asset.spy.user.service.exception.UserNotFoundException;
import asset.spy.user.service.mapper.ContactMapper;
import asset.spy.user.service.model.Contact;
import asset.spy.user.service.model.User;
import asset.spy.user.service.repository.ContactRepository;
import asset.spy.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;
    private static final List<String> ALLOWED_CONTACT_SORT_FIELDS = List.of(
            "id",
            "contactType",
            "priority",
            "user.id");

    @Transactional
    public Contact getContactOrThrow(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("This contact does not exist"));
    }

    @Override
    @Transactional
    public ContactResponseDto createContact(ContactCreateDto contactCreateDto, Long userId) {
        log.info("Creating contact {}", contactCreateDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
        Contact contact = contactMapper.toEntity(contactCreateDto, userId);
        contact.setUser(user);
        Contact savedContact = contactRepository.save(contact);
        log.info("Contact created {}", savedContact);
        return contactMapper.toDto(savedContact);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponseDto getContactById(long id) {
        log.info("Getting contact {}", id);
        Contact contact = getContactOrThrow(id);
        return contactMapper.toDto(contact);
    }

    @Override
    @Transactional
    public ContactResponseDto updateContact(Long id, ContactUpdateDto contactUpdateDto) {
        log.info("Updating contact {}", id);
        Contact existingContact = getContactOrThrow(id);
        contactMapper.updateContactFromDto(contactUpdateDto, existingContact);
        Contact updatedContact = contactRepository.save(existingContact);
        log.info("Contact updated {}", updatedContact);
        return contactMapper.toDto(updatedContact);
    }

    @Override
    @Transactional
    public void deleteContact(Long id) {
        log.info("Deleting contact {}", id);
        Contact contact = getContactOrThrow(id);
        contactRepository.delete(contact);
        log.info("Contact deleted {}", contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponseDto> getAllContacts(int page, int size, String sortField, String sortDirectionStr,
                                                   String contactType, Long userId, Integer priority) {
        log.info("Getting all contacts");

        if (!ALLOWED_CONTACT_SORT_FIELDS.contains(sortField)) {
            throw new IllegalArgumentException("Invalid sort field " + sortField + "Allowed fields are " + ALLOWED_CONTACT_SORT_FIELDS);
        }

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(sortDirectionStr);
        } catch (IllegalArgumentException e) {
            log.error("Error parsing sort direction {}", sortDirectionStr);
            throw new IllegalArgumentException("Invalid sort direction " + e.getMessage());
        }

        Sort sort = Sort.by(sortDirection, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Contact> contactPage = contactRepository.findAllWithOptionalFilters(
                contactType,
                userId,
                priority,
                pageable
        );
        return contactPage.map(contactMapper::toDto);
    }
}