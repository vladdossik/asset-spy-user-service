package asset.spy.user.service.service;

import asset.spy.user.service.cache.model.CacheNames;
import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.exception.UserAlreadyExistsException;
import asset.spy.user.service.exception.UserNotFoundException;
import asset.spy.user.service.mapper.UserMapper;
import asset.spy.user.service.model.User;
import asset.spy.user.service.repository.UserRepository;
import asset.spy.user.service.specification.UserSpecification;
import asset.spy.user.service.util.SortingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = CacheNames.USER)
public class UserServiceImpl implements UserService {
    public static final List<String> ALLOWED_USER_SORT_FIELDS = List.of("id",
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
    @Cacheable(key = "#externalId")
    public UserResponseDto getUserById(UUID externalId) {
        log.info("Retrieving user by id: {}", externalId);
        User user = getUserOrThrow(externalId);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @CachePut(key = "#externalId")
    public UserResponseDto updateUser(UUID externalId, UserUpdateDto userUpdateDto) {
        log.info("Updating user: {}", userUpdateDto);
        User existingUser = getUserOrThrow(externalId);
        userMapper.updateUserFromDto(userUpdateDto, existingUser);
        User savedUser = userRepository.save(existingUser);
        log.info("Updated user: {}", savedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheNames.USER, key = "#externalId"),
            @CacheEvict(value = CacheNames.CONTACT, allEntries = true, key = "#externalId + '_contact'")
    })
    public void deleteUser(UUID externalId) {
        log.info("Deleting user: {}", externalId);
        User user = getUserOrThrow(externalId);
        userRepository.delete(user);
        log.info("User with id: {} deleted", externalId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable, String username, String description,
                                             OffsetDateTime fromDate, OffsetDateTime toDate) {
        log.info("Retrieving all users: {}", username);

        SortingUtil.validateSortField(pageable, ALLOWED_USER_SORT_FIELDS);

        Specification<User> specification = UserSpecification.initSpecificationWithFilters(username,
                description, fromDate, toDate);
        Page<User> userPage = userRepository.findAll(specification, pageable);
        return userPage.map(userMapper::toDto);
    }

    private User getUserOrThrow(UUID externalId) {
        return userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + externalId + " not found"));
    }
}
