package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.UserDto;
import asset.spy.user.service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDTO);

    UserDto toDto(User user);
}
