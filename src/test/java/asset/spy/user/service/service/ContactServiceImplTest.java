package asset.spy.user.service.service;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.exception.ContactNotFoundException;
import asset.spy.user.service.exception.UserNotFoundException;
import asset.spy.user.service.mapper.ContactMapper;
import asset.spy.user.service.mapper.ContactMapperImpl;
import asset.spy.user.service.model.Contact;
import asset.spy.user.service.model.User;
import asset.spy.user.service.repository.ContactRepository;
import asset.spy.user.service.repository.UserRepository;
import asset.spy.user.service.specification.ContactSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Spy
    private final ContactMapper contactMapper = new ContactMapperImpl();

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Captor
    private ArgumentCaptor<Specification<Contact>> specificationArgumentCaptor;

    private static final UUID contactExternalId = UUID.randomUUID();
    private static final UUID userExternalId = UUID.randomUUID();

    @Test
    void addContactIfUserDoesNotExistTest() {
        when(userRepository
                .findByExternalId(userExternalId))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> {
            contactService.createContact(getCreatingContactDto(), userExternalId);
        });
    }

    @Test
    void addContactIfUserExistsTest() {
        User existingUser = new User();
        existingUser.setExternalId(userExternalId);
        existingUser.setUsername("username");
        existingUser.setId(1L);
        existingUser.setDateOfBirth(LocalDate.of(2020, 1, 1));

        ContactCreateDto creatingContactDto = getCreatingContactDto();

        when(userRepository
                .findByExternalId(userExternalId))
                .thenReturn(Optional.of(existingUser));
        when(contactRepository
                .save(any(Contact.class)))
                .thenReturn(getContactEntity());
        ContactResponseDto createdContact = contactService.createContact(creatingContactDto, userExternalId);

        verify(contactRepository).save(any(Contact.class));
        assertEquals(creatingContactDto.getContactType(), createdContact.getContactType());
        assertEquals(creatingContactDto.getContactValue(), createdContact.getContactValue());
        assertEquals(creatingContactDto.getPriority(), createdContact.getPriority());
    }

    @Test
    void getContactIfContactDoesNotExistTest() {
        when(contactRepository
                .findByExternalId(contactExternalId))
                .thenThrow(new ContactNotFoundException("Contact not found"));

        assertThrows(ContactNotFoundException.class, () -> {
            contactService.getContactByExternalId(contactExternalId);
        });
    }

    @Test
    void getContactIfContactExistsTest() {
        Contact existingContact = getContactEntity();

        when(contactRepository
                .findByExternalId(contactExternalId))
                .thenReturn(Optional.of(existingContact));
        ContactResponseDto actualContact = contactService.getContactByExternalId(contactExternalId);

        verify(contactRepository).findByExternalId(contactExternalId);
        assertNotNull(actualContact);
        assertEquals(actualContact.getContactType(), existingContact.getContactType());
        assertEquals(actualContact.getContactValue(), existingContact.getContactValue());
        assertEquals(actualContact.getPriority(), existingContact.getPriority());
        assertEquals(contactExternalId, actualContact.getExternalId());
    }

    @Test
    void updateContactIfContactDoesNotExistsTest() {
        ContactUpdateDto updatingContact = getUpdatingContactDto();

        when(contactRepository
                .findByExternalId(contactExternalId))
                .thenThrow(new ContactNotFoundException("Contact not found"));

        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updateContact(contactExternalId, updatingContact);
        });
    }

    @Test
    void updateContactIfContactExistsTest() {
        ContactUpdateDto updatingContact = getUpdatingContactDto();
        Contact existingContact = getContactEntity();

        when(contactRepository
                .findByExternalId(contactExternalId))
                .thenReturn(Optional.of(existingContact));
        when(contactRepository
                .save(any(Contact.class)))
                .thenReturn(existingContact);
        ContactResponseDto actualContact = contactService.updateContact(contactExternalId, updatingContact);

        verify(contactRepository).save(existingContact);
        assertNotNull(actualContact);
        assertEquals(updatingContact.getContactType(), actualContact.getContactType());
        assertEquals(updatingContact.getContactValue(), actualContact.getContactValue());
        assertEquals(updatingContact.getPriority(), actualContact.getPriority());
        assertEquals(contactExternalId, actualContact.getExternalId());
    }

    @Test
    void deleteContactIfContactDoesNotExistTest() {
        when(contactRepository
                .findByExternalId(contactExternalId))
                .thenThrow(new ContactNotFoundException("Contact not found"));

        assertThrows(ContactNotFoundException.class, () -> {
            contactService.deleteContact(contactExternalId);
        });
    }

    @Test
    void deleteContactIfContactExistsTest() {
        Contact existingContact = getContactEntity();

        when(contactRepository
                .findByExternalId(contactExternalId))
                .thenReturn(Optional.of(existingContact));
        doNothing()
                .when(contactRepository)
                .delete(existingContact);
        contactService.deleteContact(contactExternalId);

        verify(contactRepository).delete(existingContact);
    }

    @Test
    void getAllContactsWithoutFiltersTest() {
        Page<Contact> existingContactPage = getContactPage();

        when(contactRepository
                .findAll(ArgumentMatchers.<Specification<Contact>>any(), any(Pageable.class)))
                .thenReturn(existingContactPage);

        Page<ContactResponseDto> actualContacts = contactService.getAllContacts(
                PageRequest.of(0, 2),
                null,
                null,
                null);

        verify(contactRepository).findAll(ArgumentMatchers.<Specification<Contact>>any(), any(Pageable.class));
        assertNotNull(actualContacts);
        assertEquals(2, actualContacts.getContent().size());
        assertThat(actualContacts.getContent())
                .usingRecursiveComparison()
                .ignoringFields("userExternalId")
                .isEqualTo(existingContactPage.getContent());
    }

    @Test
    void getAllContactsWithFiltersTest() {
        String contactTypeFilter = "test type";
        String contactValueFilter = "test value";
        Page<Contact> existingContactPage = getContactPage();

        Specification<Contact> specification = ContactSpecification
                .initSpecificationWithFilters(
                        contactTypeFilter,
                        contactValueFilter,
                        null);

        when(contactRepository
                .findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(existingContactPage);

        Page<ContactResponseDto> actualContacts = contactService.getAllContacts(
                PageRequest.of(0, 2, Sort.by("priority")),
                contactTypeFilter,
                contactValueFilter,
                null);

        verify(contactRepository).findAll(specificationArgumentCaptor.capture(), any(Pageable.class));

        Specification<Contact> capturedSpecification = specificationArgumentCaptor.getValue();
        assertNotNull(capturedSpecification);
        assertThat(capturedSpecification)
                .usingRecursiveComparison()
                .isEqualTo(specification);

        assertNotNull(actualContacts);
        assertEquals(2, actualContacts.getContent().size());
        assertThat(actualContacts.getContent())
                .usingRecursiveComparison()
                .ignoringFields("userExternalId")
                .isEqualTo(existingContactPage.getContent());
        assertTrue(actualContacts.getContent().get(0).getPriority() < actualContacts.getContent().get(1).getPriority());
    }

    private ContactCreateDto getCreatingContactDto() {
        ContactCreateDto contact = new ContactCreateDto();
        contact.setContactType("telegram");
        contact.setContactValue("@telegram");
        contact.setPriority(1);
        return contact;
    }

    private Contact getContactEntity() {
        Contact contact = new Contact();
        contact.setContactType("telegram");
        contact.setContactValue("@telegram");
        contact.setPriority(1);
        contact.setId(1L);
        contact.setExternalId(contactExternalId);
        return contact;
    }

    private ContactUpdateDto getUpdatingContactDto() {
        ContactUpdateDto contact = new ContactUpdateDto();
        contact.setContactType("telegram test");
        contact.setContactValue("@telegram test");
        contact.setPriority(2);
        return contact;
    }

    private Page<Contact> getContactPage() {
        Contact contact1 = new Contact();
        contact1.setContactType("test type");
        contact1.setContactValue("test value");
        contact1.setPriority(0);
        Contact contact2 = new Contact();
        contact2.setContactType("test type");
        contact2.setContactValue("test value");
        contact2.setPriority(1);
        return new PageImpl<>(List.of(contact1, contact2));
    }
}
