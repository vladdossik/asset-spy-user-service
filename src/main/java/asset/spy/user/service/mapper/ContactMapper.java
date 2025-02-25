package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.ContactDTO;
import asset.spy.user.service.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(source = "userId", target = "user.id")
    Contact toEntity(ContactDTO contactDTO);

    @Mapping(source = "user.id", target = "userId")
    ContactDTO toDto(Contact contact);
}
