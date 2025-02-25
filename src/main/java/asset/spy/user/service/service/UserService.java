package asset.spy.user.service.service;

import asset.spy.user.service.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDto createUser(UserDto userDTO);

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UserDto userDTO);

    void deleteUser(Long id);

    Page<UserDto> getAllUsers(Long cursor, int size);
}
