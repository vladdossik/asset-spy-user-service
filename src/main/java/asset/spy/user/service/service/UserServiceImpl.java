package asset.spy.user.service.service;

import asset.spy.user.service.Specification.UserSpecification;
import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.exception.UserAlreadyExistsException;
import asset.spy.user.service.exception.UserNotFoundException;
import asset.spy.user.service.mapper.UserMapper;
import asset.spy.user.service.model.User;
import asset.spy.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private static final List<String> ALLOWED_USER_SORT_FIELDS = List.of("id",
            "username",
            "createdAt",
            "dateOfBirth");
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        log.info("Creating user: {}", userCreateDto);
        try {
            User user = userMapper.toEntity(userCreateDto);
            user = userRepository.save(user);
            return userMapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User with username" + userCreateDto.getUsername() + "already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        log.info("Retrieving user by id: {}", id);
        User user = getUserOrThrow(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        log.info("Updating user: {}", userUpdateDto);
        User existingUser = getUserOrThrow(id);
        userMapper.updateUserFromDto(userUpdateDto, existingUser);
        User savedUser = userRepository.save(existingUser);
        log.info("Updated user: {}", savedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        User user = getUserOrThrow(id);
        userRepository.delete(user);
        log.info("User with id: {} deleted", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(int page, int size, String sortField, String sortDirection, String username, String description, OffsetDateTime createdAt) {
        log.info("Retrieving all users: page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (!ALLOWED_USER_SORT_FIELDS.contains(sortField)) {
            log.error("Invalid sort field: {}", sortField);
            throw new IllegalArgumentException("Invalid sort field: " + sortField +
                    "Allowed values are: " + ALLOWED_USER_SORT_FIELDS);
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection.toUpperCase()), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<User> specification = UserSpecification.withFilters(username, description, createdAt);
        Page<User> userPage = userRepository.findAll(specification, pageable);
        return userPage.map(userMapper::toDto);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }
}
