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
import asset.spy.user.service.specification.ContactSpecification;
import asset.spy.user.service.util.SortingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private static final List<String> ALLOWED_CONTACT_SORT_FIELDS = List.of(
            "id",
            "contactType",
            "priority",
            "user.id");
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional
    public ContactResponseDto createContact(ContactCreateDto contactCreateDto, Long userId) {
        log.info("Creating contact {}", contactCreateDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Contact contact = contactMapper.toEntity(contactCreateDto);
        contact.setUser(user);
        contactRepository.save(contact);
        log.info("Contact {} created", contact);
        return contactMapper.toDto(contact);
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
    public Page<ContactResponseDto> getAllContacts(Pageable pageable,
                                                   String contactType, String contactValue,
                                                   Long userId, Integer priority) {
        log.info("Getting all contacts");
        SortingUtil.validateSortField(pageable, ALLOWED_CONTACT_SORT_FIELDS);

        Specification<Contact> specification = ContactSpecification.initSpecificationWithFilters(contactType,
                contactValue, userId, priority);
        Page<Contact> contactPage = contactRepository.findAll(specification, pageable);
        return contactPage.map(contactMapper::toDto);
    }

    private Contact getContactOrThrow(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("This contact does not exist"));
    }
}