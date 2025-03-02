package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "user", ignore = true)
    Contact toEntity(ContactCreateDto contactCreateDto);

    @Mapping(source = "user.id", target = "userId")
    ContactResponseDto toDto(Contact contact);

    @Mapping(target = "user", ignore = true)
    void updateContactFromDto(ContactUpdateDto contactUpdateDto, @MappingTarget Contact contact);
}
