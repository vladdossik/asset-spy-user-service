package asset.spy.user.service.service;

import asset.spy.user.service.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionAccessService {
    private final ContactRepository contactRepository;

    public boolean hasAccessToUser(UUID userExternalId) {
        UUID currentExternalId = getCurrentUserExternalId();
        return currentExternalId.equals(userExternalId);
    }

    public boolean hasAccessToContact(UUID contactExternalId) {
        if (isAdmin()) {
            return true;
        }
        UUID currentExternalId = getCurrentUserExternalId();
        return contactRepository.existsByExternalIdAndUserExternalId(contactExternalId, currentExternalId);
    }

    public boolean isAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    private UUID getCurrentUserExternalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(authentication.getName());
    }
}
