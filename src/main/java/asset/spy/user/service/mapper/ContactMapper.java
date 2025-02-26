package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContactMapper {

    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user", expression = "java(createUser(userId))")
    Contact toEntity(ContactCreateDto contactCreateDto, Long userId);

    @Mapping(source = "user.id", target = "userId")
    ContactResponseDto toDto(Contact contact);

    @Mapping(target = "user", ignore = true)
    void updateContactFromDto(ContactUpdateDto contactUpdateDto, @MappingTarget Contact contact);
}
