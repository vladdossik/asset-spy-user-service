package asset.spy.user.service.mapper;

import asset.spy.user.service.Initializer;
import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.model.Contact;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContactMapperTest extends Initializer {
    private final ContactMapper contactMapper = new ContactMapperImpl();

    @Test
    void contactDtoToEntityTest() {
        ContactCreateDto contactDto = contactCreateDto;

        Contact contactEntity = contactMapper.toEntity(contactDto);

        assertThat(contactDto)
                .usingRecursiveComparison()
                .isEqualTo(contactEntity);
    }

    @Test
    void ifContactCreateDtoIsNullTest() {
        Contact contactEntity = contactMapper.toEntity(null);

        assertThat(contactEntity).isNull();
    }

    @Test
    void contactEntityToDtoTest() {
        Contact contactEntity = contact;

        ContactResponseDto contactDto = contactMapper.toDto(contactEntity);

        assertThat(contactEntity)
                .usingRecursiveComparison()
                .ignoringFields("user", "id")
                .isEqualTo(contactDto);
    }

    @Test
    void ifContactEntityIsNullTest() {
        ContactResponseDto contactDto = contactMapper.toDto(null);

        assertThat(contactDto).isNull();
    }

    @Test
    void updateContactEntityFromDtoTest() {
        ContactUpdateDto contactDto = contactUpdateDto;
        Contact contactEntity = contact;

        contactMapper.updateContactFromDto(contactDto, contactEntity);

        assertThat(contactDto)
                .usingRecursiveComparison()
                .isEqualTo(contactEntity);
    }
}
