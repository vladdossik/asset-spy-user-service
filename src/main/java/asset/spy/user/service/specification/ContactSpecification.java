package asset.spy.user.service.specification;

import asset.spy.user.service.model.Contact;
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

            if (StringUtils.isEmpty(contactValue)) {
                return null;
            }
            return cb.like(cb.lower(root.get("contactValue")), "%" + contactValue.toLowerCase() + "%");
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
                                                                      Integer priority) {
        return Specification.where(hasContactType(contactType)
                .and(hasContactValue(contactValue))
                .and(hasPriority(priority)));
    }
}
