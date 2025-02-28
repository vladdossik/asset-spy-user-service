package asset.spy.user.service.Specification;

import asset.spy.user.service.model.Contact;
import asset.spy.user.service.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ContactSpecification {

    public static Specification<Contact> hasContactType(String contactType) {
        return (root, query, cb) -> {

            if (StringUtils.isEmpty(contactType)) {
                return null;
            }
            return cb.equal(cb.lower(root.get("contactType")), contactType.toLowerCase());
        };
    }

    public static Specification<Contact> hasContactValue(String contactValue) {
        return (root, query, cb) -> {

            if (StringUtils.isNotEmpty(contactValue)) {
                return null;
            }
            return cb.like(cb.lower(root.get("contactValue")), "%" + contactValue.toLowerCase() + "%");
        };
    }

    public static Specification<Contact> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return null;
            }
            Join<Contact, User> join = root.join("contacts", JoinType.INNER);
            return cb.equal(join.get("userId"), userId);
        };
    }

    public static Specification<Contact> hasPriority(Integer priority) {
        return (root, query, cb) -> {
            if (priority == null) {
                return null;
            }
            return cb.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Contact> initSpecificationWithFilters(String contactType, String contactValue,
                                                                      Long userId, Integer priority) {
        return Specification.where(hasContactType(contactType)
                .and(hasContactValue(contactValue))
                .and(hasUserId(userId))
                .and(hasPriority(priority)));
    }
}
