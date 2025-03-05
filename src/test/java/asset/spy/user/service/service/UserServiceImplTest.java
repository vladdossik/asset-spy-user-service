package asset.spy.user.service.service;

import asset.spy.user.service.Initializer;
import asset.spy.user.service.dto.user.UserCreateDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.exception.UserAlreadyExistsException;
import asset.spy.user.service.exception.UserNotFoundException;
import asset.spy.user.service.mapper.UserMapper;
import asset.spy.user.service.mapper.UserMapperImpl;
import asset.spy.user.service.model.User;
import asset.spy.user.service.repository.UserRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

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
class UserServiceImplTest extends Initializer {

    @Spy
    private final UserMapper userMapperSpy = new UserMapperImpl();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<Specification<User>> specificationArgumentCaptor;

    @Test
    void addUserIfUserWithThisUsernameExistsTest() {
        when(userRepository
                .save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDto));
    }

    @Test
    void addUserIfUserWithThisUsernameDoesNotExistsTest() {
        User existingUser = user;
        UserCreateDto createdUserDto = userCreateDto;

        when(userRepository
                .save(any(User.class)))
                .thenReturn(existingUser);
        UserResponseDto createdUser = userService.createUser(createdUserDto);

        verify(userRepository).save(any(User.class));
        assertNotNull(createdUser);
        assertThat(createdUserDto)
                .usingRecursiveComparison()
                .isEqualTo(createdUser);
    }

    @Test
    void getUserIfUserExistsTest() {
        User existingUser = user;

        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.of(existingUser));
        UserResponseDto actualUser = userService.getUserById(USER_EXTERNAL_ID);

        verify(userRepository).findByExternalId(USER_EXTERNAL_ID);
        assertNotNull(actualUser);
        assertThat(existingUser)
                .usingRecursiveComparison()
                .ignoringFields("id", "contacts")
                .isEqualTo(actualUser);
    }

    @Test
    void getUserIfUserDoesNotExistsTest() {
        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(USER_EXTERNAL_ID));
    }

    @Test
    void updateUserIfUserExistsTest() {
        User existingUser = user;
        UserUpdateDto updatingUser = userUpdateDto;

        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.of(existingUser));
        when(userRepository
                .save(existingUser))
                .thenReturn(existingUser);
        UserResponseDto actualUser = userService.updateUser(USER_EXTERNAL_ID, updatingUser);

        verify(userRepository).save(existingUser);
        assertNotNull(actualUser);
        assertThat(updatingUser)
                .usingRecursiveComparison()
                .isEqualTo(actualUser);
    }

    @Test
    void updateUserIfUserDoesNotExistsTest() {
        UserUpdateDto updatingUser = userUpdateDto;

        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(USER_EXTERNAL_ID, updatingUser));
    }

    @Test
    void deleteUserIfUserExistsTest() {
        User existingUser = user;

        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.of(existingUser));
        doNothing()
                .when(userRepository)
                .delete(existingUser);
        userService.deleteUser(USER_EXTERNAL_ID);

        verify(userRepository).delete(existingUser);
    }

    @Test
    void deleteUserIfUserDoesNotExistsTest() {
        when(userRepository
                .findByExternalId(USER_EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(USER_EXTERNAL_ID));
    }

    @Test
    void getAllUsersWithoutFiltersTest() {
        Page<User> existingUserPage = userPage;
        Specification<User> specification = userSpecificationWithoutFilters;

        when(userRepository
                .findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class)))
                .thenReturn(existingUserPage);

        Page<UserResponseDto> actualUsers = userService.getAllUsers(
                pageRequestWithoutUserSorting,
                null,
                null,
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
                .isEqualTo(existingUserPage.getContent());
    }

    @Test
    void getAllUsersWithFiltersTest() {
        Page<User> existingUserPage = userPage;

        Specification<User> specification = userSpecificationWithFilters;

        when(userRepository
                .findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class)))
                .thenReturn(existingUserPage);

        Page<UserResponseDto> actualUsers = userService.getAllUsers(
                pageRequestWithUserSorting,
                USERNAME_FILTER,
                DESCRIPTION_FILTER,
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
}
