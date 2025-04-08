package asset.spy.user.service.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

public class RequestUtil {

    public static UUID extractExternalId(HttpServletRequest request) {
        UUID userExternalId = (UUID) request.getAttribute("externalId");
        if (userExternalId == null) {
            throw new IllegalArgumentException("No external id found in request");
        }
        return userExternalId;
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No active HTTP request found");
        }
        return attributes.getRequest();
    }
}
