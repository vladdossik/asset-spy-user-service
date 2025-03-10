package asset.spy.user.service.service;

import asset.spy.user.service.Initializer;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

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
class ContactServiceImplTest extends Initializer {

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

    @Test
    void addContactIfUserDoesNotExistTest() {
        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            contactService.createContact(contactCreateDto, USER_EXTERNAL_ID);
        });
    }

    @Test
    void addContactIfUserExistsTest() {
        User existingUser = user;
        ContactCreateDto creatingContactDto = contactCreateDto;

        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.of(existingUser));
        when(contactRepository
                .save(any(Contact.class)))
                .thenReturn(contact);
        ContactResponseDto createdContact = contactService.createContact(creatingContactDto, USER_EXTERNAL_ID);

        verify(contactRepository).save(any(Contact.class));
        assertThat(creatingContactDto)
                .usingRecursiveComparison()
                .isEqualTo(createdContact);
    }

    @Test
    void getContactIfContactDoesNotExistTest() {
        when(contactRepository
                .findByExternalId(CONTACT_EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> {
            contactService.getContactByExternalId(CONTACT_EXTERNAL_ID);
        });
    }

    @Test
    void getContactIfContactExistsTest() {
        Contact existingContact = contact;

        when(contactRepository
                .findByExternalId(CONTACT_EXTERNAL_ID))
                .thenReturn(Optional.of(existingContact));
        ContactResponseDto actualContact = contactService.getContactByExternalId(CONTACT_EXTERNAL_ID);

        verify(contactRepository).findByExternalId(CONTACT_EXTERNAL_ID);
        assertNotNull(actualContact);
        assertThat(actualContact)
                .usingRecursiveComparison()
                .ignoringFields("userExternalId")
                .isEqualTo(existingContact);
        assertEquals(CONTACT_EXTERNAL_ID, actualContact.getExternalId());
    }

    @Test
    void updateContactIfContactDoesNotExistsTest() {
        ContactUpdateDto updatingContact = contactUpdateDto;

        when(contactRepository
                .findByExternalId(CONTACT_EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updateContact(CONTACT_EXTERNAL_ID, updatingContact);
        });
    }

    @Test
    void updateContactIfContactExistsTest() {
        ContactUpdateDto updatingContact = contactUpdateDto;
        Contact existingContact = contact;

        when(contactRepository
                .findByExternalId(CONTACT_EXTERNAL_ID))
                .thenReturn(Optional.of(existingContact));
        when(contactRepository
                .save(any(Contact.class)))
                .thenReturn(existingContact);
        ContactResponseDto actualContact = contactService.updateContact(CONTACT_EXTERNAL_ID, updatingContact);

        verify(contactRepository).save(existingContact);
        assertNotNull(actualContact);
        assertThat(updatingContact)
                .usingRecursiveComparison()
                .isEqualTo(actualContact);
        assertEquals(CONTACT_EXTERNAL_ID, actualContact.getExternalId());
    }

    @Test
    void deleteContactIfContactDoesNotExistTest() {
        when(contactRepository
                .findByExternalId(CONTACT_EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> {
            contactService.deleteContact(CONTACT_EXTERNAL_ID);
        });
    }

    @Test
    void deleteContactIfContactExistsTest() {
        Contact existingContact = contact;

        when(contactRepository
                .findByExternalId(CONTACT_EXTERNAL_ID))
                .thenReturn(Optional.of(existingContact));
        doNothing()
                .when(contactRepository)
                .delete(existingContact);
        contactService.deleteContact(CONTACT_EXTERNAL_ID);

        verify(contactRepository).delete(existingContact);
    }

    @Test
    void getAllContactsWithoutFiltersTest() {
        Page<Contact> existingContactPage = contactPage;
        Specification<Contact> specification = contactSpecificationWithoutFilters;

        when(contactRepository
                .findAll(ArgumentMatchers.<Specification<Contact>>any(), any(Pageable.class)))
                .thenReturn(existingContactPage);

        Page<ContactResponseDto> actualContacts = contactService.getAllContacts(
                pageRequestWithoutContactSorting,
                null,
                null,
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
    }

    @Test
    void getAllContactsWithFiltersTest() {
        Page<Contact> existingContactPage = contactPage;
        Specification<Contact> specification = contactSpecificationWithFilters;

        when(contactRepository
                .findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(existingContactPage);

        Page<ContactResponseDto> actualContacts = contactService.getAllContacts(
                pageRequestWithContactSorting,
                CONTACT_TYPE_FILTER,
                CONTACT_VALUE_FILTER,
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
}
