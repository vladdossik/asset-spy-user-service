package asset.spy.user.service.service;

import asset.spy.user.service.cache.model.CacheName;
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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = CacheName.CONTACT)
public class ContactServiceImpl implements ContactService {

    private static final List<String> ALLOWED_CONTACT_SORT_FIELDS = List.of(
            "id",
            "contactType",
            "priority");
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional
    public ContactResponseDto createContact(ContactCreateDto contactCreateDto, UUID userExternalId) {
        log.info("Creating contact {}", contactCreateDto);
        User user = userRepository.findByExternalId(userExternalId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Contact contact = contactMapper.toEntity(contactCreateDto);
        contact.setUser(user);
        contactRepository.save(contact);
        log.info("Contact {} created", contact);
        return contactMapper.toDto(contact);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "#externalId")
    public ContactResponseDto getContactByExternalId(UUID externalId) {
        log.info("Getting contact {}", externalId);
        Contact contact = getContactOrThrow(externalId);
        return contactMapper.toDto(contact);
    }

    @Override
    @Transactional
    @CachePut(key = "#externalId")
    public ContactResponseDto updateContact(UUID externalId, ContactUpdateDto contactUpdateDto) {
        log.info("Updating contact {}", externalId);
        Contact existingContact = getContactOrThrow(externalId);
        contactMapper.updateContactFromDto(contactUpdateDto, existingContact);
        Contact updatedContact = contactRepository.save(existingContact);
        log.info("Contact updated {}", updatedContact);
        return contactMapper.toDto(updatedContact);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#externalId")
    public void deleteContact(UUID externalId) {
        log.info("Deleting contact {}", externalId);
        Contact contact = getContactOrThrow(externalId);
        contactRepository.delete(contact);
        log.info("Contact deleted {}", contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponseDto> getAllContacts(Pageable pageable,
                                                   String contactType, String contactValue, Integer priority) {
        log.info("Getting all contacts");
        SortingUtil.validateSortField(pageable, ALLOWED_CONTACT_SORT_FIELDS);

        Specification<Contact> specification = ContactSpecification.initSpecificationWithFilters(contactType,
                contactValue, priority);
        Page<Contact> contactPage = contactRepository.findAll(specification, pageable);
        return contactPage.map(contactMapper::toDto);
    }

    private Contact getContactOrThrow(UUID externalId) {
        return contactRepository.findByExternalId(externalId)
                .orElseThrow(() -> new ContactNotFoundException("This contact does not exist"));
    }
}