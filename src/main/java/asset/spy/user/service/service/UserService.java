package asset.spy.user.service.service;

import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

public interface UserService {

    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto);

    void deleteUser(Long id);

    Page<UserResponseDto> getAllUsers(int page, int size, String sortField, String sortDirection, String username,
                                      String description, OffsetDateTime createdAt);
}
