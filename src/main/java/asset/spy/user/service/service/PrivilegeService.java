package asset.spy.user.service.service;

import asset.spy.user.service.repository.ContactRepository;
import asset.spy.user.service.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

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
        HttpServletRequest request = RequestUtil.getCurrentHttpRequest();
        return RequestUtil.extractExternalId(request);
    }
}
