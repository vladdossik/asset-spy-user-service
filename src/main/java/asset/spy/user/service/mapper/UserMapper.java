package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.UserDTO;
import asset.spy.user.service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);
}
