package asset.spy.user.service;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.model.Contact;
import asset.spy.user.service.model.User;
import asset.spy.user.service.specification.ContactSpecification;
import asset.spy.user.service.specification.UserSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public abstract class Initializer {
    protected static final UUID USER_EXTERNAL_ID = UUID.randomUUID();
    protected static final UUID CONTACT_EXTERNAL_ID = UUID.randomUUID();
    protected static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "firstname",
            "username",
            "id",
            "dateOfBirth"
    );
    protected static final String CONTACT_TYPE_FILTER = "test type";
    protected static final String CONTACT_VALUE_FILTER = "test value";
    protected static final String USERNAME_FILTER = "test username";
    protected static final String DESCRIPTION_FILTER = "test description";

    protected static ContactCreateDto contactCreateDto;
    protected static ContactUpdateDto contactUpdateDto;
    protected static Contact contact;
    protected static Page<Contact> contactPage;
    protected static Specification<Contact> contactSpecificationWithFilters;
    protected static Specification<Contact> contactSpecificationWithoutFilters;

    protected static UserCreateDto userCreateDto;
    protected static UserUpdateDto userUpdateDto;
    protected static User user;
    protected static Page<User> userPage;
    protected static Specification<User> userSpecificationWithFilters;
    protected static Specification<User> userSpecificationWithoutFilters;

    protected static Pageable pageRequestWithNotValidSortField;
    protected static Pageable pageRequestWithValidSortField;
    protected static Pageable pageRequestWithoutSortField;
    protected static Pageable pageRequestWithContactSorting;
    protected static Pageable pageRequestWithoutContactSorting;
    protected static Pageable pageRequestWithoutUserSorting;
    protected static Pageable pageRequestWithUserSorting;

    @BeforeAll
    public static void init() {
        contactCreateDto = new ContactCreateDto();
        contactCreateDto.setContactType("telegram");
        contactCreateDto.setContactValue("@telegram");
        contactCreateDto.setPriority(1);

        contactUpdateDto = new ContactUpdateDto();
        contactUpdateDto.setContactType("telegram");
        contactUpdateDto.setContactValue("@telegram");
        contactUpdateDto.setPriority(2);

        contact = new Contact();
        contact.setContactType("telegram 1");
        contact.setContactValue("@telegram 1");
        contact.setPriority(2);
        contact.setId(1L);
        contact.setExternalId(CONTACT_EXTERNAL_ID);

        Contact contact1 = new Contact();
        contact1.setContactType("test type");
        contact1.setContactValue("test value");
        contact1.setPriority(0);
        Contact contact2 = new Contact();
        contact2.setContactType("test type");
        contact2.setContactValue("test value");
        contact2.setPriority(1);
        contactPage = new PageImpl<>(List.of(contact1, contact2));

        contactSpecificationWithFilters = ContactSpecification.initSpecificationWithFilters(
                CONTACT_TYPE_FILTER,
                CONTACT_VALUE_FILTER,
                null
        );

        contactSpecificationWithoutFilters = ContactSpecification.initSpecificationWithFilters(
                null,
                null,
                null
        );

        userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("test username");
        userCreateDto.setDescription("test description");
        userCreateDto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setUsername("test username");
        userUpdateDto.setDescription("test description");
        userUpdateDto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        user = new User();
        user.setUsername("test username");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setDescription("test description");
        user.setId(1L);
        user.setExternalId(USER_EXTERNAL_ID);

        User user1 = new User();
        user1.setUsername("test username");
        user1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user1.setDescription("test description");
        User user2 = new User();
        user2.setUsername("test username");
        user2.setDateOfBirth(LocalDate.of(2010, 8, 24));
        user2.setDescription("test description");
        userPage = new PageImpl<>(List.of(user1, user2));

        userSpecificationWithoutFilters = UserSpecification.initSpecificationWithFilters(
                null,
                null,
                null,
                null
        );

        userSpecificationWithFilters = UserSpecification.initSpecificationWithFilters(
                USERNAME_FILTER,
                DESCRIPTION_FILTER,
                null,
                null
        );

        pageRequestWithNotValidSortField = PageRequest.of(0, 2, Sort.by("name"));
        pageRequestWithValidSortField = PageRequest.of(0, 2, Sort.by("username"));
        pageRequestWithoutSortField = PageRequest.of(0, 2);
        pageRequestWithContactSorting = PageRequest.of(0, 2, Sort.by("priority"));
        pageRequestWithoutContactSorting = PageRequest.of(0, 2);
        pageRequestWithoutUserSorting = PageRequest.of(0, 2);
        pageRequestWithUserSorting = PageRequest.of(0, 2, Sort.by("dateOfBirth"));
    }
}
