package asset.spy.user.service.service;

import asset.spy.user.service.dto.ContactDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional
    public ContactDto createContact(ContactDto contactDTO) {
        log.info("Creating contact {}", contactDTO);
        User user = userRepository.findById(contactDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(contactDTO.getUserId()));
        Contact contact = contactMapper.toEntity(contactDTO);
        contact.setUser(user);
        Contact savedContact = contactRepository.save(contact);
        log.info("Contact created {}", savedContact);
        return contactMapper.toDto(savedContact);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto getContactById(long id) {
        log.info("Getting contact {}", id);
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Contact {} not found", id);
                    return new ContactNotFoundException(id);
                });
        return contactMapper.toDto(contact);
    }

    @Override
    @Transactional
    public ContactDto updateContact(Long id, ContactDto contactDTO) {
        log.info("Updating contact {}", id);

        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Contact {} not found", id);
                    return new ContactNotFoundException(id);
                });
        User user = userRepository.findById(contactDTO.getUserId())
                .orElseThrow(() -> {
                    log.warn("User {} not found", contactDTO.getUserId());
                    return new UserNotFoundException(contactDTO.getUserId());
                });

        existingContact.setUser(user);
        existingContact.setContactType(contactDTO.getContactType());
        existingContact.setContactValue(contactDTO.getContactValue());
        existingContact.setPriority(contactDTO.getPriority());

        Contact updatedContact = contactRepository.save(existingContact);
        log.info("Contact updated {}", updatedContact);

        return contactMapper.toDto(updatedContact);
    }

    @Override
    @Transactional
    public void deleteContact(Long id) {
        log.info("Deleting contact {}", id);
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Contact {} not found", id);
                    return new ContactNotFoundException(id);
                });
        contactRepository.delete(contact);
        log.info("Contact deleted {}", contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDto> getAllContacts(Long cursor, int size) {
        log.info("Getting all contacts");
        Pageable pageable = PageRequest.of(0, size);
        return contactRepository.findAllContactsAfterCursor(cursor, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDto> getAllContactsForUser(Long userId, Long cursor, int size) {
        log.info("Getting all contacts for user {}", userId);
        Pageable pageable = PageRequest.of(0, size);
        return contactRepository.findAllContactsForUserAfterCursor(userId, cursor, pageable);
    }
}