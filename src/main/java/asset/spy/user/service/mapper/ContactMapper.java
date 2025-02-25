package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.ContactDto;
import asset.spy.user.service.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(source = "userId", target = "user.id")
    Contact toEntity(ContactDto contactDTO);

    @Mapping(source = "user.id", target = "userId")
    ContactDto toDto(Contact contact);
}
