package asset.spy.user.service.service;

import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.exception.UserAlreadyExistsException;
import asset.spy.user.service.exception.UserNotFoundException;
import asset.spy.user.service.mapper.UserMapper;
import asset.spy.user.service.mapper.UserMapperImpl;
import asset.spy.user.service.model.User;
import asset.spy.user.service.repository.UserRepository;
import asset.spy.user.service.specification.UserSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Spy
    private final UserMapper userMapperSpy = new UserMapperImpl();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<Specification<User>> specificationArgumentCaptor;

    private static final UUID userExternalId = UUID.randomUUID();

    @Test
    void addUserIfUserWithThisUsernameExistsTest() {
        when(userRepository
                .save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(getCreatingUserDto()));
    }

    @Test
    void addUserIfUserWithThisUsernameDoesNotExistsTest() {
        User existingUser = getUserEntity();
        UserCreateDto createdUserDto = getCreatingUserDto();

        when(userRepository
                .save(any(User.class)))
                .thenReturn(existingUser);
        UserResponseDto createdUser = userService.createUser(createdUserDto);

        verify(userRepository).save(any(User.class));
        assertNotNull(createdUser);
        assertEquals(createdUserDto.getUsername(), createdUser.getUsername());
        assertEquals(createdUserDto.getDateOfBirth(), createdUser.getDateOfBirth());
        assertEquals(createdUserDto.getDescription(), createdUser.getDescription());
    }

    @Test
    void getUserIfUserExistsTest() {
        User existingUser = getUserEntity();

        when(userRepository
                .findByExternalId(userExternalId))
                .thenReturn(Optional.of(existingUser));
        UserResponseDto actualUser = userService.getUserById(userExternalId);

        verify(userRepository).findByExternalId(userExternalId);
        assertNotNull(actualUser);
        assertEquals(existingUser.getUsername(), actualUser.getUsername());
        assertEquals(existingUser.getDateOfBirth(), actualUser.getDateOfBirth());
        assertEquals(existingUser.getDescription(), actualUser.getDescription());
        assertEquals(existingUser.getExternalId(), actualUser.getExternalId());
    }

    @Test
    void getUserIfUserDoesNotExistsTest() {
        when(userRepository
                .findByExternalId(userExternalId))
                .thenThrow(new UserNotFoundException(""));

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userExternalId));
    }

    @Test
    void updateUserIfUserExistsTest() {
        User existingUser = getUserEntity();
        UserUpdateDto updatingUser = getUpdatingUserDto();

        when(userRepository
                .findByExternalId(userExternalId))
                .thenReturn(Optional.of(existingUser));
        when(userRepository
                .save(existingUser))
                .thenReturn(existingUser);
        UserResponseDto actualUser = userService.updateUser(userExternalId, updatingUser);

        verify(userRepository).save(existingUser);
        assertNotNull(actualUser);
        assertEquals(updatingUser.getUsername(), actualUser.getUsername());
        assertEquals(updatingUser.getDateOfBirth(), actualUser.getDateOfBirth());
        assertEquals(updatingUser.getDescription(), actualUser.getDescription());
    }

    @Test
    void updateUserIfUserDoesNotExistsTest() {
        UserUpdateDto updatingUser = getUpdatingUserDto();

        when(userRepository
                .findByExternalId(userExternalId))
                .thenThrow(new UserNotFoundException(""));

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userExternalId, updatingUser));
    }

    @Test
    void deleteUserIfUserExistsTest() {
        User existingUser = getUserEntity();

        when(userRepository
                .findByExternalId(userExternalId))
                .thenReturn(Optional.of(existingUser));
        doNothing()
                .when(userRepository)
                .delete(existingUser);
        userService.deleteUser(userExternalId);

        verify(userRepository).delete(existingUser);
    }

    @Test
    void deleteUserIfUserDoesNotExistsTest() {
        when(userRepository
                .findByExternalId(userExternalId))
                .thenThrow(new UserNotFoundException(""));

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userExternalId));
    }

    @Test
    void getAllUsersWithoutFiltersTest() {
        Page<User> existingUserPage = getUserPage();

        when(userRepository
                .findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class)))
                .thenReturn(existingUserPage);

        Page<UserResponseDto> actualUsers = userService.getAllUsers(
                PageRequest.of(0, 2),
                null,
                null,
                null,
                null);

        verify(userRepository).findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class));
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.getContent().size());
        assertThat(actualUsers.getContent())
                .usingRecursiveComparison()
                .isEqualTo(existingUserPage.getContent());
    }

    @Test
    void getAllUsersWithFiltersTest() {
        String usernameFilter = "test username";
        String descriptionFilter = "test description";

        Page<User> existingUserPage = getUserPage();

        Specification<User> specification = UserSpecification.initSpecificationWithFilters(
                usernameFilter, descriptionFilter, null, null
        );

        when(userRepository
                .findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class)))
                .thenReturn(existingUserPage);

        Page<UserResponseDto> actualUsers = userService.getAllUsers(
                PageRequest.of(0, 2, Sort.by("dateOfBirth")),
                usernameFilter,
                descriptionFilter,
                null,
                null);

        verify(userRepository).findAll(specificationArgumentCaptor.capture(), any(Pageable.class));

        Specification<User> capturedSpecification = specificationArgumentCaptor.getValue();
        assertNotNull(capturedSpecification);
        assertThat(capturedSpecification)
                .usingRecursiveComparison()
                .isEqualTo(specification);

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.getContent().size());
        assertThat(actualUsers.getContent())
                .usingRecursiveComparison()
                .ignoringFields("dateOfBirth")
                .isEqualTo(existingUserPage.getContent());
        assertTrue(actualUsers.getContent().get(0).getDateOfBirth().isBefore(actualUsers.getContent().get(1).getDateOfBirth()));
    }

    private UserCreateDto getCreatingUserDto() {
        UserCreateDto user = new UserCreateDto();
        user.setUsername("username");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setDescription("description");
        return user;
    }

    private User getUserEntity() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setDescription("description");
        user.setExternalId(userExternalId);
        return user;
    }

    private UserUpdateDto getUpdatingUserDto() {
        UserUpdateDto user = new UserUpdateDto();
        user.setUsername("username test");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setDescription("description test");
        return user;
    }

    private Page<User> getUserPage() {
        User user1 = new User();
        user1.setUsername("test username");
        user1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user1.setDescription("test description");

        User user2 = new User();
        user2.setUsername("test username");
        user2.setDateOfBirth(LocalDate.of(2010, 8, 24));
        user2.setDescription("test description");

        return new PageImpl<>(List.of(user1, user2));
    }
}
