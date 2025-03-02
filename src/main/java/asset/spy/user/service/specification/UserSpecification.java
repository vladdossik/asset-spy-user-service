package asset.spy.user.service.specification;

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

    public static Specification<User> hasCreatedAtBetween(OffsetDateTime fromDate, OffsetDateTime toDate) {
        return (root, query, cb) -> {
            if (fromDate == null && toDate == null) {
                return null;
            }

            if (fromDate != null && toDate != null) {
                return cb.between(root.get("createdAt"), fromDate, toDate);
            } else if (fromDate != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
            } else {
                return cb.lessThan(root.get("createdAt"), toDate);
            }
        };
    }

    public static Specification<User> initSpecificationWithFilters(String username, String description,
                                                                   OffsetDateTime fromDate, OffsetDateTime toDate) {
        return Specification.where(hasUsername(username)
                .and(hasDescription(description))
                .and(hasCreatedAtBetween(fromDate, toDate)));
    }
}
