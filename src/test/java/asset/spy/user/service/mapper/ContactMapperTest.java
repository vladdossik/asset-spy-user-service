package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.model.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ContactMapperTest {
    private final ContactMapper contactMapper = new ContactMapperImpl();

    @Test
    void contactDtoToEntityTest() {
        ContactCreateDto contactDto = new ContactCreateDto();
        contactDto.setContactType("telegram");
        contactDto.setContactValue("@telegram");
        contactDto.setPriority(1);

        Contact contactEntity = contactMapper.toEntity(contactDto);

        assertEquals(contactDto.getContactType(), contactEntity.getContactType());
        assertEquals(contactDto.getContactValue(), contactEntity.getContactValue());
        assertEquals(contactDto.getPriority(), contactEntity.getPriority());
    }

    @Test
    void contactEntityToDtoTest() {
        Contact contactEntity = new Contact();
        contactEntity.setContactType("telegram");
        contactEntity.setContactValue("@telegram");
        contactEntity.setPriority(1);

        ContactResponseDto contactDto = contactMapper.toDto(contactEntity);

        assertEquals(contactEntity.getContactType(), contactDto.getContactType());
        assertEquals(contactEntity.getContactValue(), contactDto.getContactValue());
        assertEquals(contactEntity.getPriority(), contactDto.getPriority());
    }

    @Test
    void updateContactEntityFromDtoTest() {
        ContactUpdateDto contactDto = new ContactUpdateDto();
        contactDto.setContactType("telegram");
        contactDto.setContactValue("@telegram");
        contactDto.setPriority(1);

        Contact contactEntity = new Contact();
        contactEntity.setContactType("telegram 1");
        contactEntity.setContactValue("@telegram 1");
        contactEntity.setPriority(2);

        contactMapper.updateContactFromDto(contactDto, contactEntity);

        assertEquals(contactDto.getContactType(), contactEntity.getContactType());
        assertEquals(contactDto.getContactValue(), contactEntity.getContactValue());
        assertEquals(contactDto.getPriority(), contactEntity.getPriority());
    }
}
