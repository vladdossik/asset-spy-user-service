package asset.spy.user.service.service;

import asset.spy.user.service.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    Page<UserDTO> getAllUsers(Long cursor, int size);
}
