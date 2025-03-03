package asset.spy.user.service.service;

import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface UserService {

    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto getUserById(UUID externalId);

    UserResponseDto updateUser(UUID externalID, UserUpdateDto userUpdateDto);

    void deleteUser(UUID externalId);

    Page<UserResponseDto> getAllUsers(Pageable pageable, String username,
                                      String description, OffsetDateTime fromDate, OffsetDateTime toDate);
}
