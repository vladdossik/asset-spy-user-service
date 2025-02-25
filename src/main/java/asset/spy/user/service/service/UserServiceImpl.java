package asset.spy.user.service.service;

import asset.spy.user.service.dto.UserDTO;
import asset.spy.user.service.exception.UserNotFoundException;
import asset.spy.user.service.mapper.UserMapper;
import asset.spy.user.service.model.User;
import asset.spy.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user: {}", userDTO);
        User user = userMapper.toEntity(userDTO);
        user.setCreatedAt(Instant.now());
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.info("Retrieving user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with id: {} not found", id);
                    return new UserNotFoundException(id);
                });
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user: {}", userDTO);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with id: {} not found", id);
                    return new UserNotFoundException(id);
                });

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setDescription(userDTO.getDescription());
        existingUser.setDateOfBirth(userDTO.getDateOfBirth());
        User savedUser = userRepository.save(existingUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with id: {} not found", id);
                    return new UserNotFoundException(id);
                });
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Long cursor, int size) {
        log.info("Retrieving all users");
        Pageable pageable = PageRequest.of(0, size);
        Page<User> userPage = userRepository.findAllUsersAfterCursor(cursor, pageable);
        return userPage.map(userMapper::toDto);
    }
}
