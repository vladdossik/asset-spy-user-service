package asset.spy.user.service.mapper;

import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void userDtoToEntityTest() {
        UserCreateDto userDto = new UserCreateDto();
        userDto.setUsername("test username");
        userDto.setDescription("test description");
        userDto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        User userEntity = userMapper.toEntity(userDto);

        assertEquals(userDto.getUsername(), userEntity.getUsername());
        assertEquals(userDto.getDateOfBirth(), userEntity.getDateOfBirth());
        assertEquals(userDto.getDescription(), userEntity.getDescription());
    }

    @Test
    void userEntityToDtoTest() {
        User userEntity = new User();
        userEntity.setUsername("test username");
        userEntity.setDescription("test description");
        userEntity.setDateOfBirth(LocalDate.of(1990, 1, 1));

        UserResponseDto userDto = userMapper.toDto(userEntity);

        assertEquals(userEntity.getUsername(), userDto.getUsername());
        assertEquals(userEntity.getDateOfBirth(), userDto.getDateOfBirth());
        assertEquals(userEntity.getDescription(), userDto.getDescription());
    }

    @Test
    void updateUserEntityFromDtoTest() {
        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setUsername("test username");
        userDto.setDescription("test description");
        userDto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        User userEntity = new User();
        userEntity.setUsername("test username 1");
        userEntity.setDateOfBirth(LocalDate.of(2000, 3, 18));
        userEntity.setDescription("test description 1");

        userMapper.updateUserFromDto(userDto, userEntity);

        assertEquals(userDto.getUsername(), userEntity.getUsername());
        assertEquals(userDto.getDateOfBirth(), userEntity.getDateOfBirth());
        assertEquals(userDto.getDescription(), userEntity.getDescription());
    }
}
