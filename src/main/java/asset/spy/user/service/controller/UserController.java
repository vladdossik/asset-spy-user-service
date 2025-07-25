package asset.spy.user.service.controller;

import asset.spy.user.service.dto.user.UserResponseDto;
import asset.spy.user.service.dto.user.UserUpdateDto;
import asset.spy.user.service.service.UserService;
import asset.spy.user.service.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{externalId}")
    public UserResponseDto getUserById(@PathVariable UUID externalId) {
        return userService.getUserById(externalId);
    }

    @PutMapping("/{externalId}")
    public UserResponseDto updateUser(@PathVariable UUID externalId, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUser(externalId, userUpdateDto);
    }

    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID externalId) {
        userService.deleteUser(externalId);
    }

    @GetMapping
    public Page<UserResponseDto> getAllUsers(Pageable pageable,
                                             @RequestParam(required = false) String username,
                                             @RequestParam(required = false) String description,
                                             @RequestParam(required = false) OffsetDateTime dateFrom,
                                             @RequestParam(required = false) OffsetDateTime dateTo) {
        return userService.getAllUsers(pageable, username, description, dateFrom, dateTo);
    }

    @GetMapping("/profile")
    public UserResponseDto getUserProfile(HttpServletRequest request) {
        return userService.getUserById(RequestUtil.extractExternalId(request));
    }
}
