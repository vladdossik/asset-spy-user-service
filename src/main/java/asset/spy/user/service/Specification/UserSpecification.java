package asset.spy.user.service.Specification;

import asset.spy.user.service.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public class UserSpecification {

    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(username)) {
                return null;
            }
            return cb.equal(cb.lower(root.get("username")), username.toLowerCase());
        };
    }

    public static Specification<User> hasDescription(String description) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(description)) {
                return null;
            }
            return cb.equal(cb.lower(root.get("description")), description.toLowerCase());
        };
    }

    public static Specification<User> hasCreatedAt(OffsetDateTime createdAt) {
        return (root, query, cb) -> {
            if (createdAt == null) {
                return null;
            }
            return cb.equal(root.get("createdAt"), createdAt);
        };
    }

    public static Specification<User> initSpecificationWithFilters(String username, String description,
                                                                   OffsetDateTime createdAt) {
        return Specification.where(hasUsername(username).and(hasDescription(description)).and(hasCreatedAt(createdAt)));
    }
}
