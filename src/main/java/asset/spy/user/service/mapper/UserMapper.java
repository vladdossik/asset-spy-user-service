package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    User toEntity(UserCreateDto userCreateDto);

    UserResponseDto toDto(User user);

    void updateUserFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
