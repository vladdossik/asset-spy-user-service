package asset.spy.user.service.mapper;

import asset.spy.user.service.Initializer;
import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest extends Initializer {
    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void userDtoToEntityTest() {
        UserCreateDto userDto = userCreateDto;

        User userEntity = userMapper.toEntity(userDto);

        assertThat(userDto)
                .usingRecursiveComparison()
                .isEqualTo(userEntity);
    }

    @Test
    void ifUserCreateDtoIsNullTest() {
        User userEntity = userMapper.toEntity(null);

        assertThat(userEntity).isNull();
    }

    @Test
    void userEntityToDtoTest() {
        User userEntity = user;

        UserResponseDto userDto = userMapper.toDto(userEntity);

        assertThat(userEntity)
                .usingRecursiveComparison()
                .ignoringFields("id", "contacts")
                .isEqualTo(userDto);
    }

    @Test
    void ifUserEntityIsNullTest() {
        UserResponseDto userDto = userMapper.toDto(null);

        assertThat(userDto).isNull();
    }

    @Test
    void updateUserEntityFromDtoTest() {
        UserUpdateDto userDto = userUpdateDto;
        User userEntity = user;

        userMapper.updateUserFromDto(userDto, userEntity);

        assertThat(userDto)
                .usingRecursiveComparison()
                .isEqualTo(userEntity);
    }
}
